package com.syp.view;

import com.syp.model.Complaint;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class HomePageView extends BorderPane{
    private final BorderPane root = new BorderPane();
    private final HBox headerBox = new HBox();
    private final HBox footerBox = new HBox();
    private final TableView<Complaint> complaintTable = new TableView<>();
    private final TextField searchField = new TextField();
    private final ComboBox<String> categoryFilter = new ComboBox<>();
    private final ComboBox<String> statusFilter = new ComboBox<>();
    private final Button searchButton = new Button("Suchen");
    private final Button createReportButton = new Button("Neue Meldung");
    private final Pagination pagination = new Pagination();

    public HomePageView() {
        setupUI();
    }

    private void setupUI() {
        // Header konfigurieren
        headerBox.getStyleClass().add("header");
        headerBox.setSpacing(20);

        // Footer konfigurieren
        footerBox.getStyleClass().add("footer");

        // Filterleiste
        HBox filterBox = new HBox(10, searchField, categoryFilter, statusFilter, searchButton);

        // Hauptlayout
        VBox centerBox = new VBox(10, filterBox, complaintTable, pagination, createReportButton);
        centerBox.setPadding(new Insets(10));

        root.setTop(headerBox);
        root.setCenter(centerBox);
        root.setBottom(footerBox);
    }

    // Getter-Methoden mit den korrekten Namen
    public BorderPane getRoot() { return root; }
    public HBox getHeaderBox() { return headerBox; }
    public HBox getFooterBox() { return footerBox; }
    public TableView<Complaint> getComplaintTable() { return complaintTable; }
    public TextField getSearchField() { return searchField; }
    public ComboBox<String> getCategoryFilter() { return categoryFilter; }
    public ComboBox<String> getStatusFilter() { return statusFilter; }
    public Button getSearchButton() { return searchButton; }
    public Button getCreateReportButton() { return createReportButton; }
    public Pagination getPagination() { return pagination; }

    public void setComplaints(ObservableList<Complaint> complaints) {
        complaintTable.setItems(complaints);
    }

    public void setPagination(int totalItems, int currentPage, int itemsPerPage) {
        int pageCount = (int) Math.ceil((double) totalItems / itemsPerPage);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(currentPage - 1);
    }
}