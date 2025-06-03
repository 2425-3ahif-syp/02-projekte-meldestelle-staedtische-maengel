package com.syp.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class CreateComplaintView extends VBox {
    public CreateComplaintView() {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource(".//src/main/resources/create-complaint.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error loading create-complaint.fxml", e);
        }
    }
}