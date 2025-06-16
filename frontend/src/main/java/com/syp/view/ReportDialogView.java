package com.syp.view;

import com.syp.model.Complaint;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class ReportDialogView {
    private final Stage stage;
    private Optional<String> selectedReason = Optional.empty();

    public ReportDialogView(Complaint complaint) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Meldung erstellen");

        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(15);
        root.getStyleClass().add("root");

        Label header = new Label("Beschwerde melden: " + complaint.getSubject());
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ComboBox<String> reasonComboBox = new ComboBox<>();
        reasonComboBox.getItems().addAll(
                "Unangemessener Inhalt",
                "Spam",
                "Doppelte Meldung",
                "Falsche Kategorie"
        );
        reasonComboBox.setPromptText("Grund auswählen");

        HBox buttons = new HBox();
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = new Button("Abbrechen");
        btnCancel.getStyleClass().addAll("button", "cancel");
        btnCancel.setOnAction(e -> {
            selectedReason = Optional.empty();
            stage.close();
        });

        Button btnSubmit = new Button("Senden");
        btnSubmit.getStyleClass().add("button");
        btnSubmit.setOnAction(e -> {
            String reason = reasonComboBox.getValue();
            if (reason == null || reason.trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Bitte wählen Sie einen Grund.").showAndWait();
            } else {
                selectedReason = Optional.of(reason);
                stage.close();
            }
        });

        buttons.getChildren().addAll(btnCancel, btnSubmit);

        root.getChildren().addAll(header, reasonComboBox, buttons);

        Scene scene = new Scene(root, 400, 180);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
    }

    public Optional<String> showAndWait() {
        stage.showAndWait();
        return selectedReason;
    }
}
