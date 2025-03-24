package com.syp.view;

import com.syp.model.Complaint;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.Arrays;
import java.util.List;

public class ComplaintView {
    private final VBox root = new VBox();

    private final HBox hBoxSearch = new HBox();
    private final TextField tfSearchText = new TextField();
    private final Button btnSearch = new Button("Search");

    private final VBox vBoxDetails = new VBox();
    private final HBox hBoxId = new HBox();
    private final HBox hBoxSubject = new HBox();
    private final HBox hBoxCategory = new HBox();
    private final HBox hBoxAddress = new HBox();
    private final HBox hBoxDescription = new HBox();
    private final HBox hBoxImagePath = new HBox();
    private final HBox hBoxButton = new HBox();

    private final TextField tfIdText = new TextField();
    private final TextField tfSubjectText = new TextField();
    private final ComboBox<String> cbCategory = new ComboBox<>();
    private final TextField tfAddressText = new TextField();
    private final TextField tfDescriptionText = new TextField();
    private final TextField tfImagePathText = new TextField();

    private final Button btnSave = new Button("Save");
    private final Button btnCreate = new Button("Create Complaint");
    private final Button btnDelete = new Button("Delete");

    private final ListView<Complaint> lvComplaints = new ListView<>();

    public ComplaintView() {
        init();
    }

    private void init() {
        root.setSpacing(15);
        root.setPadding(new Insets(20, 20, 20, 20));

        hBoxSearch.setSpacing(10);
        hBoxSearch.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10;");
        hBoxSearch.getChildren().addAll(tfSearchText, btnSearch);

        lvComplaints.setPrefHeight(300);
        lvComplaints.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px;");
        lvComplaints.setMaxWidth(Double.MAX_VALUE);

        vBoxDetails.setSpacing(10);
        vBoxDetails.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10;");
        vBoxDetails.getChildren().addAll(hBoxId, hBoxSubject, hBoxCategory, hBoxAddress, hBoxDescription, hBoxImagePath, hBoxButton);

        setupInputField(hBoxId, "ID: ", tfIdText);
        setupInputField(hBoxSubject, "Subject: ", tfSubjectText);

        List<String> categories = Arrays.asList("Environment", "Infrastructure", "Health", "Public Safety");
        cbCategory.getItems().addAll(categories);
        hBoxCategory.setSpacing(10);
        hBoxCategory.getChildren().addAll(new Label("Category: "), cbCategory);

        setupInputField(hBoxAddress, "Address: ", tfAddressText);
        setupInputField(hBoxDescription, "Description: ", tfDescriptionText);

        setupInputField(hBoxImagePath, "Image Path (Optional): ", tfImagePathText);

        hBoxButton.setSpacing(15);
        hBoxButton.getChildren().addAll(btnCreate, btnSave, btnDelete);

        root.getChildren().addAll(hBoxSearch, lvComplaints, vBoxDetails);
    }

    private void setupInputField(HBox hBox, String labelText, TextField textField) {
        hBox.setSpacing(10);
        Label label = new Label(labelText);
        textField.setMaxWidth(200);
        hBox.getChildren().addAll(label, textField);
    }

    public VBox getRoot() { return root; }
    public HBox getHBoxSearch() { return hBoxSearch; }
    public TextField getTfSearchText() { return tfSearchText; }
    public Button getBtnSearch() { return btnSearch; }
    public ListView<Complaint> getLvComplaints() { return lvComplaints; }
    public HBox getHBoxId() { return hBoxId; }
    public HBox getHBoxSubject() { return hBoxSubject; }
    public HBox getHBoxCategory() { return hBoxCategory; }
    public HBox getHBoxAddress() { return hBoxAddress; }
    public HBox getHBoxDescription() { return hBoxDescription; }
    public HBox getHBoxImagePath() { return hBoxImagePath; }
    public TextField getTfIdText() { return tfIdText; }
    public TextField getTfSubjectText() { return tfSubjectText; }
    public ComboBox<String> getCbCategory() { return cbCategory; }
    public TextField getTfAddressText() { return tfAddressText; }
    public TextField getTfDescriptionText() { return tfDescriptionText; }
    public TextField getTfImagePathText() { return tfImagePathText; }
    public Button getBtnCreate() { return btnCreate; }
    public Button getBtnSave() { return btnSave; }
    public Button getBtnDelete() { return btnDelete; }
}
