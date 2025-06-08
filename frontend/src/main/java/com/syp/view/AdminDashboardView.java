package com.syp.view;

import com.syp.model.Complaint;
import com.syp.service.ComplaintService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class AdminDashboardView {
    private final ComplaintService complaintService = new ComplaintService();
    private ObservableList<Complaint> dataList;
    private ImageView imageView;
    private Label lblNoImage;

    private Stage stage;

    public void show() {
        stage = new Stage();
        stage.setTitle("Admin Dashboard");

        BorderPane root = new BorderPane();

        HBox header = new HBox();
        header.setPadding(new Insets(10));
        Label lblHeader = new Label("Admin Dashboard");
        lblHeader.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        header.getChildren().add(lblHeader);
        root.setTop(header);

        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.6);

        VBox leftBox = new VBox();
        leftBox.setPadding(new Insets(10));
        leftBox.setSpacing(10);

        TableView<Complaint> table = new TableView<>();
        TableColumn<Complaint, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> cell.getValue().idProperty());
        colId.setPrefWidth(50);

        TableColumn<Complaint, String> colSubject = new TableColumn<>("Betreff");
        colSubject.setCellValueFactory(cell -> cell.getValue().subjectProperty());
        colSubject.setPrefWidth(200);

        TableColumn<Complaint, String> colCategory = new TableColumn<>("Kategorie");
        colCategory.setCellValueFactory(cell -> cell.getValue().categoryProperty());
        colCategory.setPrefWidth(100);

        TableColumn<Complaint, String> colAddress = new TableColumn<>("Standort");
        colAddress.setCellValueFactory(cell -> cell.getValue().addressProperty());
        colAddress.setPrefWidth(150);

        TableColumn<Complaint, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(cell -> cell.getValue().statusProperty());
        colStatus.setPrefWidth(120);

        TableColumn<Complaint, String> colCreatedAt = new TableColumn<>("Erstellt am");
        colCreatedAt.setCellValueFactory(cell -> {
            if (cell.getValue().getCreatedAt() != null) {
                return new SimpleStringProperty(cell.getValue().getCreatedAt().toString());
            } else {
                return new SimpleStringProperty("");
            }
        });
        colCreatedAt.setPrefWidth(150);

        table.getColumns().addAll(
                colId, colSubject, colCategory, colAddress, colStatus, colCreatedAt
        );

        dataList = FXCollections.observableArrayList();
        loadData();
        table.setItems(dataList);

        HBox statusBox = new HBox();
        statusBox.setSpacing(10);
        statusBox.setPadding(new Insets(10, 0, 0, 0));
        Label lblNewStatus = new Label("Status ändern zu:");
        ComboBox<String> cbNewStatus = new ComboBox<>();
        cbNewStatus.getItems().addAll("Offen", "In Bearbeitung", "Abgeschlossen");
        cbNewStatus.setValue("Offen");
        Button btnDelete = new Button("Löschen");
        Button btnUpdate = new Button("Status aktualisieren");
        statusBox.getChildren().addAll(lblNewStatus, cbNewStatus, btnUpdate, btnDelete);

        leftBox.getChildren().addAll(table, statusBox);

        VBox rightBox = new VBox();
        rightBox.setPadding(new Insets(10));
        rightBox.setSpacing(10);

        Label lblImageTitle = new Label("Bild zur ausgewählten Meldung:");
        imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        lblNoImage = new Label("Keine Meldung ausgewählt.");

        rightBox.getChildren().addAll(lblImageTitle, imageView, lblNoImage);

        splitPane.getItems().addAll(leftBox, rightBox);
        root.setCenter(splitPane);

        HBox footer = new HBox();
        footer.setPadding(new Insets(10));
        footer.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        Button btnLogout = new Button("Abmelden");
        footer.getChildren().add(btnLogout);
        root.setBottom(footer);

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        btnUpdate.setOnAction(e -> {
            Complaint selected = table.getSelectionModel().getSelectedItem();
            String newStatus = cbNewStatus.getValue();
            if (selected != null && newStatus != null) {
                complaintService.updateComplaintStatus(selected.getId(), newStatus);
                loadData();
            }
        });

        btnDelete.setOnAction(e -> {
            Complaint selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Löschen bestätigen");
                confirm.setHeaderText("Meldung wirklich löschen?");
                confirm.setContentText("Diese Aktion kann nicht rückgängig gemacht werden.");

                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        complaintService.deleteComplaintById(selected.getId());
                        loadData();
                        imageView.setImage(null);
                        lblNoImage.setText("Keine Meldung ausgewählt.");
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte wähle eine Meldung aus.");
                alert.showAndWait();
            }
        });


        btnLogout.setOnAction(e -> stage.close());


        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null && newSel.getImagePath() != null) {
                String relPath = newSel.getImagePath();
                File file = new File(System.getProperty("user.dir") + File.separator + relPath);
                if (file.exists()) {
                    Image img = new Image(file.toURI().toString());
                    imageView.setImage(img);
                    lblNoImage.setText("");
                } else {
                    imageView.setImage(null);
                    lblNoImage.setText("Bilddatei nicht gefunden.");
                }
            } else {
                imageView.setImage(null);
                lblNoImage.setText("Kein Bild zur Meldung vorhanden.");
            }
        });
    }

    private void loadData() {
        List<Complaint> list = complaintService.getAllComplaints();
        dataList.setAll(list);
    }
}
