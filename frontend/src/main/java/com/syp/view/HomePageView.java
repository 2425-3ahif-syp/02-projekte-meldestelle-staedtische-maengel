package com.syp.view;

import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Objects;

public class HomePageView {
    private final VBox root;
    private final HBox header;
    private final HBox footer;
    private final TableView<String> table;
    private final Button searchButton;
    private final Button createReportButton;
    private final TextField searchField;

    public HomePageView() {

        root = new VBox();
        header = new HBox();
        footer = new HBox();
        table = new TableView<>();
        searchButton = new Button("Search");
        createReportButton = new Button("Create Report");
        searchField = new TextField();

        root.getChildren().addAll(header, searchField, searchButton, createReportButton, table, footer);
        footer.getStyleClass().add("footer");
        header.getStyleClass().add("header");
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/syp/view/stylesheet/styles.css")).toExternalForm());
    }

    public TableView<String> getTable() {
        return table;
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

    public VBox getRoot() {
        return root;
    }

    public HBox getHeader() {
        return header;
    }

    public HBox getFooter() {
        return footer;
    }
}