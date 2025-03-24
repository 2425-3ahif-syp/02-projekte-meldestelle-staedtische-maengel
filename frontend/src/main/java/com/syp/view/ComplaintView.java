package com.syp.view;

import com.syp.model.Complaint;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ComplaintView {
    // root
    private final VBox root = new VBox();

    // Search
    private final HBox hBoxSearch = new HBox();
    private final TextField tfSearchText = new TextField();
    private final Button btnSearch = new Button("Search");

    // Details (For Reporting a Complaint)
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
    private final TextField tfCategoryText = new TextField();
    private final TextField tfAddressText = new TextField();
    private final TextField tfDescriptionText = new TextField();
    private final TextField tfImagePathText = new TextField();
    private final Button btnNew = new Button("New Complaint");
    private final Button btnEdit = new Button("Edit");
    private final Button btnSave = new Button("Save");
    private final Button btnDelete = new Button("Delete");

    // Complaint List
    private final ListView<Complaint> lvComplaints = new ListView<>();

    public ComplaintView() {
        init();
    }

    private void init() {
        // Root
        root.setSpacing(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        // Search
        hBoxSearch.setSpacing(10);
        hBoxSearch.getChildren().addAll(tfSearchText, btnSearch);

        // Complaint List
        lvComplaints.setPrefHeight(200);

        // Generate root view
        root.getChildren().addAll(hBoxSearch, lvComplaints);

        // Details (Reporting a Complaint)
        vBoxDetails.setSpacing(10);
        vBoxDetails.getChildren().addAll(hBoxId, hBoxSubject, hBoxCategory, hBoxAddress, hBoxDescription, hBoxImagePath, hBoxButton);

        hBoxId.setSpacing(10);
        hBoxId.getChildren().addAll(new Label("ID: "), tfIdText);

        hBoxSubject.setSpacing(10);
        hBoxSubject.getChildren().addAll(new Label("Subject: "), tfSubjectText);

        hBoxCategory.setSpacing(10);
        hBoxCategory.getChildren().addAll(new Label("Category: "), tfCategoryText);

        hBoxAddress.setSpacing(10);
        hBoxAddress.getChildren().addAll(new Label("Address: "), tfAddressText);

        hBoxDescription.setSpacing(10);
        hBoxDescription.getChildren().addAll(new Label("Description: "), tfDescriptionText);

        hBoxImagePath.setSpacing(10);
        hBoxImagePath.getChildren().addAll(new Label("Image Path: "), tfImagePathText);

        hBoxButton.setSpacing(10);
        hBoxButton.getChildren().addAll(btnNew, btnEdit, btnSave, btnDelete);
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
    public TextField getTfCategoryText() { return tfCategoryText; }
    public TextField getTfAddressText() { return tfAddressText; }
    public TextField getTfDescriptionText() { return tfDescriptionText; }
    public TextField getTfImagePathText() { return tfImagePathText; }
    public Button getBtnNew() { return btnNew; }
    public Button getBtnEdit() { return btnEdit; }
    public Button getBtnSave() { return btnSave; }
    public Button getBtnDelete() { return btnDelete; }
}
