package com.syp.view;

import com.syp.model.Complaint;
import com.syp.model.Report;
import com.syp.service.ComplaintService;
import com.syp.service.ReportService;
import com.syp.util.Toast;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private final ReportService reportService = new ReportService();

    private Stage stage;

    private VBox complaintsBox = new VBox(10);
    private VBox reportsBox = new VBox(10);

    public void show() {
        stage = new Stage();
        stage.setTitle("🛠️ Admin Dashboard");

        Label headerLabel = new Label("Admin Dashboard");
        headerLabel.getStyleClass().add("header-label");
        HBox header = new HBox(headerLabel);
        header.setPadding(new Insets(20, 20, 10, 20));

        // TabPane mit 2 Tabs
        TabPane tabPane = new TabPane();

        Tab tabComplaints = new Tab("Beschwerden");
        complaintsBox.setPadding(new Insets(10));
        ScrollPane complaintsScroll = new ScrollPane(complaintsBox);
        complaintsScroll.setFitToWidth(true);
        tabComplaints.setContent(complaintsScroll);
        tabComplaints.setClosable(false);

        Tab tabReports = new Tab("Meldungen");
        reportsBox.setPadding(new Insets(10));
        ScrollPane reportsScroll = new ScrollPane(reportsBox);
        reportsScroll.setFitToWidth(true);
        tabReports.setContent(reportsScroll);
        tabReports.setClosable(false);

        tabPane.getTabs().addAll(tabComplaints, tabReports);

        Button btnLogout = new Button("🚪 Abmelden");
        btnLogout.getStyleClass().add("card-button");
        btnLogout.setTooltip(new Tooltip("Abmelden und Dashboard schließen"));
        btnLogout.setOnAction(e -> stage.close());

        Label copy = new Label("© 2025 CityCare");
        copy.setStyle("-fx-text-fill: #888;");

        HBox footer = new HBox(10, copy, btnLogout);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(10, 20, 20, 20));

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(tabPane);
        root.setBottom(footer);

        Scene scene = new Scene(root, 900, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.show();

        loadComplaints();
        loadReports();
    }

    private void loadComplaints() {
        complaintsBox.getChildren().clear();
        List<Complaint> complaints = complaintService.getAllComplaints();

        if (complaints.isEmpty()) {
            Label noComplaints = new Label("Keine Beschwerden vorhanden.");
            noComplaints.getStyleClass().add("info-label");
            complaintsBox.getChildren().add(noComplaints);
            return;
        }

        for (Complaint c : complaints) {
            VBox card = new VBox(8);
            card.getStyleClass().add("card");
            card.setPadding(new Insets(12));
            card.prefWidthProperty().bind(complaintsBox.widthProperty().subtract(20));

            if (c.getImagePath() != null) {
                File imgFile = new File(c.getImagePath());
                if (imgFile.exists()) {
                    ImageView iv = new ImageView(new Image(imgFile.toURI().toString(), 300, 0, true, true));
                    card.getChildren().add(iv);
                }
            }

            Label lblSubject = new Label("📌 " + c.getSubject());
            lblSubject.getStyleClass().add("card-label-title");

            Label lblCategory = new Label("Kategorie: " + c.getCategory());
            lblCategory.getStyleClass().add("card-label");

            Label lblAddress = new Label("Adresse: " + c.getAddress());
            lblAddress.getStyleClass().add("card-label");

            Label lblDate = new Label("Gemeldet am: " +
                    (c.getCreatedAt() != null ? c.getCreatedAt().toString() : "-"));
            lblDate.getStyleClass().add("card-label");

            Label lblStatus = new Label("Aktueller Status:");
            lblStatus.getStyleClass().add("card-label");

            ComboBox<String> cbStatus = new ComboBox<>();
            cbStatus.getItems().addAll("Offen", "In Bearbeitung", "Abgeschlossen");
            cbStatus.setValue(c.getStatus() != null ? c.getStatus() : "Offen");

            Button btnUpdate = new Button("🔄 Aktualisieren");
            btnUpdate.getStyleClass().add("card-button");
            btnUpdate.setOnAction(e -> {
                String newStatus = cbStatus.getValue();
                complaintService.updateComplaintStatus(c.getId(), newStatus);
                Toast.show(stage, "Status geändert zu: " + newStatus);
                loadComplaints();
            });

            Button btnDelete = new Button("🗑️ Löschen");
            btnDelete.getStyleClass().addAll("card-button", "danger");
            btnDelete.setOnAction(e -> {
                complaintService.deleteComplaintById(c.getId());
                Toast.show(stage, "Beschwerde gelöscht.");
                loadComplaints();
            });

            HBox actionBox = new HBox(8, lblStatus, cbStatus, btnUpdate, btnDelete);
            actionBox.setAlignment(Pos.CENTER_LEFT);

            card.getChildren().addAll(lblSubject, lblCategory, lblAddress, lblDate, actionBox);
            complaintsBox.getChildren().add(card);
        }
    }

    private void loadReports() {
        reportsBox.getChildren().clear();
        List<Report> reports = reportService.getAllReports();

        if (reports.isEmpty()) {
            Label noReports = new Label("Keine Meldungen vorhanden.");
            noReports.getStyleClass().add("info-label");
            reportsBox.getChildren().add(noReports);
            return;
        }

        for (Report r : reports) {
            Complaint complaint = complaintService.findById(r.getComplaintId());
            VBox card = new VBox(8);
            card.getStyleClass().add("card");
            card.setPadding(new Insets(12));
            card.prefWidthProperty().bind(reportsBox.widthProperty().subtract(20));

            Label lblReason = new Label("Grund: " + r.getReason());
            lblReason.getStyleClass().add("card-label-title");

            Label lblTime = new Label("Gemeldet am: " + r.getReportTime().toString());
            lblTime.getStyleClass().add("card-label");

            Label lblComplaint = new Label("Beschwerde: " + (complaint != null ? complaint.getSubject() : "unbekannt"));
            lblComplaint.getStyleClass().add("card-label");

            card.getChildren().addAll(lblReason, lblTime, lblComplaint);
            reportsBox.getChildren().add(card);
        }
    }
}
