package com.syp.view;

import com.syp.model.Complaint;
import com.syp.service.ComplaintService;
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
    private final VBox cardContainer = new VBox(10);
    private Stage stage;

    public void show() {
        stage = new Stage();
        stage.setTitle("🛠️ Admin Dashboard");

        // Header
        Label headerLabel = new Label("Admin Dashboard");
        headerLabel.getStyleClass().add("header-label");
        HBox header = new HBox(headerLabel);
        header.setPadding(new Insets(20, 20, 10, 20));

        // Card-Container im ScrollPane
        ScrollPane scroll = new ScrollPane(cardContainer);
        scroll.setFitToWidth(true);
        cardContainer.setPadding(new Insets(10));
        cardContainer.setPrefWidth(800);

        // Footer & Logout
        Button btnLogout = new Button("🚪 Abmelden");
        btnLogout.getStyleClass().add("card-button");
        btnLogout.setTooltip(new Tooltip("Abmelden und Dashboard schließen"));
        btnLogout.setOnAction(e -> stage.close());
        Label copy = new Label("© 2025 CityCare");
        copy.setStyle("-fx-text-fill: #888;");
        HBox footer = new HBox(10, copy, btnLogout);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(10, 20, 20, 20));

        // Zusammenbauen
        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(scroll);
        root.setBottom(footer);

        Scene scene = new Scene(root, 900, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        loadCards();
    }

    private void loadCards() {
        cardContainer.getChildren().clear();
        List<Complaint> complaints = complaintService.getAllComplaints();

        for (Complaint c : complaints) {
            VBox card = new VBox(8);
            card.getStyleClass().add("card");
            card.setPadding(new Insets(12));
            // Karte immer volle Breite
            card.prefWidthProperty().bind(cardContainer.widthProperty().subtract(20));

            // Bild (optional)
            if (c.getImagePath() != null) {
                File imgFile = new File(c.getImagePath());
                if (imgFile.exists()) {
                    ImageView iv = new ImageView(new Image(imgFile.toURI().toString(), 300, 0, true, true));
                    card.getChildren().add(iv);
                }
            }

            // Textinfos
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

            // Status-Control
            ComboBox<String> cbStatus = new ComboBox<>();
            cbStatus.getItems().addAll("Offen", "In Bearbeitung", "Abgeschlossen");
            cbStatus.setValue(c.getStatus() != null ? c.getStatus() : "Offen");

            Button btnUpdate = new Button("🔄 Aktualisieren");
            btnUpdate.getStyleClass().add("card-button");
            btnUpdate.setOnAction(e -> {
                String newStatus = cbStatus.getValue();
                complaintService.updateComplaintStatus(c.getId(), newStatus);
                Toast.show(stage, "Status geändert zu: " + newStatus);
                loadCards();
            });

            Button btnDelete = new Button("🗑️ Löschen");
            btnDelete.getStyleClass().addAll("card-button", "danger");
            btnDelete.setOnAction(e -> {
                complaintService.deleteComplaintById(c.getId());
                Toast.show(stage, "Meldung gelöscht.");
                loadCards();
            });

            HBox actionBox = new HBox(8, lblStatus, cbStatus, btnUpdate, btnDelete);
            actionBox.setAlignment(Pos.CENTER_LEFT);

            card.getChildren().addAll(
                    lblSubject,
                    lblCategory,
                    lblAddress,
                    lblDate,
                    actionBox
            );

            cardContainer.getChildren().add(card);
        }
    }
}
