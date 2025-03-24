package com.syp.view;

import com.syp.model.Complaint;
import com.syp.database.ComplaintRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class ComplaintPresenter {
    private Complaint complaint;
    private final ComplaintView view;
    private final ComplaintRepository repo;
    private final ObservableList<Complaint> complaintList = FXCollections.observableArrayList();

    public ComplaintPresenter(ComplaintView view) {
        this.view = view;
        this.repo = new ComplaintRepository();
        this.complaint = null;
        bindViewToModel();
        attachEvents();
        addListeners();
        init();
    }

    private void bindViewToModel() {
        view.getLvComplaints().setItems(complaintList);
        setEditMode(false);
    }

    private void attachEvents() {
        view.getBtnSearch().setOnAction(_ -> searchComplaint());
        view.getBtnNew().setOnAction(_ -> newComplaint());
        view.getBtnEdit().setOnAction(_ -> editComplaint());
        view.getBtnSave().setOnAction(_ -> saveComplaint());
        view.getBtnDelete().setOnAction(_ -> deleteComplaint());
    }

    private void addListeners() {
        view.getLvComplaints().getSelectionModel().selectedItemProperty().addListener(
                (_, _, newSelection) -> showComplaintDetails(newSelection)
        );
    }

    private void showComplaintDetails(Complaint complaint) {
        if (complaint != null) {
            this.complaint = complaint;
            view.getTfIdText().setText(String.valueOf(complaint.getId().get()));
            view.getTfSubjectText().setText(complaint.getSubject().get());
            view.getTfCategoryText().setText(complaint.getCategory().get());
            view.getTfAddressText().setText(complaint.getAddress().get());
            view.getTfDescriptionText().setText(complaint.getDescription().get());
            view.getTfImagePathText().setText(complaint.getImagePath().get());
        } else {
            clearFields();
        }
    }

    private void init() {
        reloadComplaintList();
    }

    private void reloadComplaintList() {
        complaintList.clear();
        complaintList.addAll(repo.getAllComplaints());
    }

    private void searchComplaint() {
        String searchText = view.getTfSearchText().getText().toLowerCase();

        if (!searchText.isEmpty()) {
            for (Complaint complaint : complaintList) {
                if (complaint.getSubject().get().toLowerCase().contains(searchText) ||
                        complaint.getCategory().get().toLowerCase().contains(searchText)) {
                    view.getLvComplaints().getSelectionModel().select(complaint);
                    view.getLvComplaints().scrollTo(complaint);
                    break;
                }
            }
        }
    }

    private void newComplaint() {
        complaint = null;
        clearFields();
        setEditMode(true);
    }

    private void editComplaint() {
        if (complaint != null) {
            setEditMode(true);
        }
    }

    private void saveComplaint() {
        String subject = view.getTfSubjectText().getText();
        String category = view.getTfCategoryText().getText();
        String address = view.getTfAddressText().getText();
        String description = view.getTfDescriptionText().getText();
        String imagePath = view.getTfImagePathText().getText();

        if (subject.isEmpty() || category.isEmpty() || address.isEmpty() || description.isEmpty()) {
            showAlert("Please fill all fields!", AlertType.WARNING);
            return;
        }

        if (complaint == null) {
            Complaint newComplaint = new Complaint(0, subject, category, address, description, imagePath);
            repo.addComplaint(newComplaint);
            complaintList.add(newComplaint);
            view.getLvComplaints().getSelectionModel().select(newComplaint);
        } else {
            complaint.setSubject(subject);
            complaint.setCategory(category);
            complaint.setAddress(address);
            complaint.setDescription(description);
            complaint.setImagePath(imagePath);
            repo.updateComplaint(complaint);
            view.getLvComplaints().refresh();
        }

        setEditMode(false);
    }

    private void deleteComplaint() {
        if (complaint != null) {
            if (showConfirmationDialog()) {
                repo.deleteComplaint(complaint.getId().get());
                complaintList.remove(complaint);
                complaint = null;
                clearFields();
            }
        }
    }

    private void clearFields() {
        view.getTfIdText().clear();
        view.getTfSubjectText().clear();
        view.getTfCategoryText().clear();
        view.getTfAddressText().clear();
        view.getTfDescriptionText().clear();
        view.getTfImagePathText().clear();
    }

    private void setEditMode(boolean editMode) {
        view.getTfSubjectText().setDisable(!editMode);
        view.getTfCategoryText().setDisable(!editMode);
        view.getTfAddressText().setDisable(!editMode);
        view.getTfDescriptionText().setDisable(!editMode);
        view.getTfImagePathText().setDisable(!editMode);
    }

    private void showAlert(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete this complaint?");
        return alert.showAndWait().get() == ButtonType.OK;
    }

    public static void show(Stage stage) {
        ComplaintView view = new ComplaintView();
        ComplaintPresenter controller = new ComplaintPresenter(view);

        stage.setScene(new javafx.scene.Scene(view.getRoot()));
        stage.setTitle("Complaint Manager");
        stage.show();
    }
}
