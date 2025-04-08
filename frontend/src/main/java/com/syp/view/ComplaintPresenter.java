package com.syp.view;

import com.syp.model.Complaint;
import com.syp.database.ComplaintRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.time.LocalDateTime;

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
        view.getBtnCreate().setOnAction(_ -> createComplaint());
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
            view.getTfSubjectText().setText(complaint.getSubject().get());
            view.getCbCategory().setValue(complaint.getCategory().get());
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

    private void createComplaint() {
        complaint = null;
        clearFields();
        setEditMode(true);
    }

    private void saveComplaint() {
        int id = Integer.parseInt(view.getTfIdText().getText());
        String subject = view.getTfSubjectText().getText();
        String category = view.getCbCategory().getValue();
        String address = view.getTfAddressText().getText();
        String description = view.getTfDescriptionText().getText();
        String imagePath = view.getTfImagePathText().getText();

        if (subject.isEmpty() || category == null || address.isEmpty() || description.isEmpty()) {
            showAlert("Please fill all fields!", AlertType.WARNING);
            return;
        }

        Complaint newComplaint = new Complaint(
                id, subject, category, address, description, imagePath, "Open", LocalDateTime.now(), null
        );
        repo.addComplaint(newComplaint);
        complaintList.add(newComplaint);
        view.getLvComplaints().getSelectionModel().select(newComplaint);

        setEditMode(false);
    }

    private void deleteComplaint() {
        if (complaint != null) {
            repo.deleteComplaint(complaint.getId().get());
            complaintList.remove(complaint);
            complaint = null;
            clearFields();
        }
    }

    private void clearFields() {
        view.getTfSubjectText().clear();
        view.getCbCategory().getSelectionModel().clearSelection();
        view.getTfAddressText().clear();
        view.getTfDescriptionText().clear();
        view.getTfImagePathText().clear();
    }

    private void setEditMode(boolean editMode) {
        view.getTfSubjectText().setDisable(!editMode);
        view.getCbCategory().setDisable(!editMode);
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

        Scene scene = new Scene(view.getRoot());
        stage.setTitle("Complaint Manager");
        stage.setScene(scene);
        stage.show();
    }
}
