package com.syp.view;

import com.syp.service.AuthenticationService;
import com.syp.util.Toast;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class LoginView {
    private final AuthenticationService authService = new AuthenticationService();
    private boolean loginSuccess = false;

    public boolean showAndWait() {
        Stage stage = new Stage();
        stage.setTitle("Gemeinde Login");
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox();
        root.setPadding(new Insets(15));
        root.setSpacing(10);

        Label lblHeader = new Label("Gemeinde Login");
        lblHeader.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label lblUser = new Label("Benutzername:");
        TextField tfUsername = new TextField();

        Label lblPass = new Label("Passwort:");
        PasswordField pfPassword = new PasswordField();

        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: red;");

        grid.add(lblUser, 0, 0);
        grid.add(tfUsername, 1, 0);
        grid.add(lblPass, 0, 1);
        grid.add(pfPassword, 1, 1);
        grid.add(lblError, 1, 2);

        Button btnCancel = new Button("Abbrechen");
        Button btnLogin = new Button("Anmelden");
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(btnCancel, btnLogin);

        root.getChildren().addAll(lblHeader, grid, buttonBox);

        Scene scene = new Scene(root, 400, 250);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);

        btnCancel.setOnAction(e -> {
            loginSuccess = false;
            stage.close();
        });

        btnLogin.setOnAction(e -> {
            String user = tfUsername.getText().trim();
            String pass = pfPassword.getText();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.show(stage, "Bitte Benutzername und Passwort eingeben.");
                return;
            }
            if (authService.login(user, pass)) {
                loginSuccess = true;
                stage.close();
            } else {
                Toast.show(stage, "Ungültige Anmeldedaten.");
            }
        });

        stage.showAndWait();
        return loginSuccess;
    }
}
