package com.syp.view;

import com.syp.model.Complaint;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;

public class HomePageView {
    private final VBox root;
    private final HBox header;
    private final HBox footer;
    private final ListView<Complaint> complaintListView;
    private final ScrollPane scrollPane;
    private final Button searchButton;
    private final Button createReportButton;
    private final TextField searchField;

    public HomePageView(List<Complaint> complaints) {
        root = new VBox();
        header = new HBox();
        footer = new HBox();
        complaintListView = new ListView<>();
        scrollPane = new ScrollPane(complaintListView);
        searchButton = new Button("Search");
        createReportButton = new Button("Create Report");
        searchField = new TextField();

        root.getChildren().addAll(header, searchField, searchButton, createReportButton, scrollPane, footer);
        footer.getStyleClass().add("footer");
        header.getStyleClass().add("header");
        scrollPane.getStyleClass().add("scroll-pane"); // Apply the scroll-pane style class
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/syp/view/stylesheet/styles.css")).toExternalForm());

        complaintListView.setCellFactory(new ComplaintCellFactory());
        setComplaints(complaints);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
    }

    public void setComplaints(List<Complaint> complaints) {
        complaintListView.getItems().setAll(complaints);
    }

    public VBox getRoot() {
        return root;
    }

    public HBox getHeader() {
        return header;
    }

    public HBox getFooter() {
        return footer;
    }

    public ListView<Complaint> getComplaintListView() {
        return complaintListView;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public Button getCreateReportButton() {
        return createReportButton;
    }

    public TextField getSearchField() {
        return searchField;
    }
}