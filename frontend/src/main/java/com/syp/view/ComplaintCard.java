package com.syp.view;

import com.syp.model.Complaint;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ComplaintCard extends HBox {
    public ComplaintCard(Complaint complaint) {
        setSpacing(10);
        setPadding(new Insets(10));
        getStyleClass().add("complaint-card");

        ImageView imageView = new ImageView();
        if (!complaint.getImagePath().get().isEmpty()) {
            Image image = new Image(complaint.getImagePath().get());
            imageView.setImage(image);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
        }
        imageView.getStyleClass().add("complaint-image");

        VBox details = new VBox();
        details.setSpacing(5);
        details.getStyleClass().add("complaint-details");

        Label subject = new Label(complaint.getSubject().get());
        subject.getStyleClass().add("complaint-subject");

        Label category = new Label("Category: " + complaint.getCategory().get());
        Label address = new Label("Address: " + complaint.getAddress().get());
        Label description = new Label("Description: " + complaint.getDescription().get());
        Label status = new Label("Status: " + complaint.getStatus().get());
        Label createdAt = new Label("Created At: " + complaint.getCreatedAt().get());
        Label completedAt = new Label("Completed At: " + (complaint.getCompletedAt().get() != null ? complaint.getCompletedAt().get() : "Not done yet"));

        details.getChildren().addAll(subject, category, address, description, status, createdAt, completedAt);

        getChildren().addAll(imageView, details);
        HBox.setHgrow(details, Priority.ALWAYS);
    }
}