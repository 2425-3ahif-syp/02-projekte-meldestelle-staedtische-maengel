package com.syp.view;

import com.syp.service.ComplaintService;
import com.syp.util.Toast;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;

public class CreateComplaintView {
    private final ComplaintService complaintService = new ComplaintService();

    public void showAndWait() {
        Stage stage = new Stage();
        stage.setTitle("Neue Mängelmeldung");
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(15);
        root.getStyleClass().add("root");

        Label lblHeader = new Label("Neue Mängelmeldung");
        lblHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        Label lblSubject = new Label("Betreff*:");
        TextField tfSubject = new TextField();
        tfSubject.setPromptText("Kurze Beschreibung");

        Label lblCategory = new Label("Kategorie*:");
        ComboBox<String> cbCategory = new ComboBox<>();
        cbCategory.getItems().addAll("Straße", "Laterne", "Vandalismus");
        cbCategory.setPromptText("Wähle Kategorie");

        Label lblLocation = new Label("Standort*:");
        TextField tfLocation = new TextField();
        tfLocation.setPromptText("Adresse oder Ort");

        Label lblDescription = new Label("Beschreibung:");
        TextArea taDescription = new TextArea();
        taDescription.setWrapText(true);
        taDescription.setPromptText("Details zum Mangel");

        Label lblImage = new Label("Bild hinzufügen:");
        HBox imageBox = new HBox();
        imageBox.setSpacing(10);
        Button btnUpload = new Button("Bild auswählen");
        Label lblImagePath = new Label("keine Datei");
        imageBox.getChildren().addAll(btnUpload, lblImagePath);
        imageBox.setAlignment(Pos.CENTER_LEFT);

        grid.add(lblSubject, 0, 0);
        grid.add(tfSubject, 1, 0);
        grid.add(lblCategory, 0, 1);
        grid.add(cbCategory, 1, 1);
        grid.add(lblLocation, 0, 2);
        grid.add(tfLocation, 1, 2);
        grid.add(lblDescription, 0, 3);
        grid.add(taDescription, 1, 3);
        grid.add(lblImage, 0, 4);
        grid.add(imageBox, 1, 4);

        Button btnBack = new Button("Abbrechen");
        btnBack.getStyleClass().addAll("button", "cancel");
        Button btnSubmit = new Button("Meldung abschicken");
        btnSubmit.getStyleClass().add("button");

        HBox buttonBox = new HBox(btnBack, btnSubmit);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        root.getChildren().addAll(lblHeader, grid, buttonBox);

        Scene scene = new Scene(root, 550, 450);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);

        final File[] selectedFile = { null };

        btnUpload.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Bilddateien", "*.png", "*.jpg", "*.jpeg")
            );
            File file = fc.showOpenDialog(stage);
            if (file != null) {
                selectedFile[0] = file;
                lblImagePath.setText(file.getName());
            }
        });

        btnBack.setOnAction(e -> {
            stage.close();
        });

        btnSubmit.setOnAction(e -> {
            String subject = tfSubject.getText().trim();
            String category = cbCategory.getValue();
            String location = tfLocation.getText().trim();
            String description = taDescription.getText().trim();

            if (subject.isEmpty() || category == null || location.isEmpty()) {
                Toast.show(stage, "Betreff, Kategorie und Standort sind Pflichtfelder.");
                return;
            }

            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String relativeImagePath = null;
            if (selectedFile[0] != null) {
                try {
                    String originalName = selectedFile[0].getName();
                    String timestamp = String.valueOf(Instant.now().toEpochMilli());
                    String newName = timestamp + "_" + originalName;
                    Path dest = Paths.get(uploadDir, newName);
                    Files.copy(selectedFile[0].toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                    relativeImagePath = "uploads" + File.separator + newName;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Toast.show(stage, "Fehler beim Speichern des Bildes.");
                    return;
                }
            }

            try {
                complaintService.registerComplaint(
                        subject, category, location, description, relativeImagePath
                );
                Toast.show(stage, "Meldung erfolgreich abgeschickt.");
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.show(stage, "Fehler beim Speichern der Meldung.");
            }
        });

        stage.showAndWait();
    }
}
