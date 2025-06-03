package com.syp.view;

import com.syp.database.HomePageRepository;
import com.syp.model.Complaint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomePagePresenter {
    private static final int ITEMS_PER_PAGE = 10;

    private final HomePageView view;
    private final HomePageRepository repo;
    private final ObservableList<Complaint> dataList = FXCollections.observableArrayList();

    private int currentPage = 1;
    private int totalItems = 0;

    public HomePagePresenter(HomePageView view) {
        this.view = view;
        this.repo = new HomePageRepository();
        initialize();
    }

    private void initialize() {
        setupEventHandlers();
        loadInitialData();
        setupHeaderFooter();
    }

    private void setupHeaderFooter() {
        // Header mit Titel und Untertitel
        view.getHeaderBox().getChildren().addAll(
                new Label("Mängelmeldesystem"),
                new Label("Bürgerportal")
        );

        // Footer mit Copyright-Informationen
        view.getFooterBox().getChildren().add(
                new Label("© 2023 Meldungen.com | Alle Rechte vorbehalten")
        );
    }

    private void setupEventHandlers() {
        view.getSearchButton().setOnAction(e -> {
            currentPage = 1;
            loadFilteredData();
        });

        view.getCreateReportButton().setOnAction(e -> showCreateReportDialog());

        view.getPagination().currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPage = newIndex.intValue() + 1;
            loadFilteredData();
        });

        view.getCategoryFilter().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            currentPage = 1;
            loadFilteredData();
        });

        view.getStatusFilter().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            currentPage = 1;
            loadFilteredData();
        });
    }

    private void loadInitialData() {
        currentPage = 1;
        loadFilteredData();
    }

    private void loadFilteredData() {
        String searchText = view.getSearchField().getText().trim();
        String category = view.getCategoryFilter().getValue();
        String status = view.getStatusFilter().getValue();

        dataList.setAll(repo.getFilteredComplaints(
                searchText.isEmpty() ? null : searchText,
                "Alle".equals(category) ? null : category,
                "Alle".equals(status) ? null : status,
                currentPage,
                ITEMS_PER_PAGE
        ));

        totalItems = repo.getTotalComplaintCount(
                searchText.isEmpty() ? null : searchText,
                "Alle".equals(category) ? null : category,
                "Alle".equals(status) ? null : status
        );

        view.setComplaints(dataList);
        view.setPagination(totalItems, currentPage, ITEMS_PER_PAGE);
    }

    private void showCreateReportDialog() {
        // Implementierung des Dialogs zur Meldungserstellung
        System.out.println("Dialog zur Meldungserstellung würde geöffnet werden");

        // Beispiel:
        // CreateReportDialog dialog = new CreateReportDialog();
        // dialog.showAndWait().ifPresent(complaint -> {
        //     repo.addComplaint(complaint);
        //     loadFilteredData();
        // });
    }

    public static void show(Stage stage) {
        HomePageView view = new HomePageView();
        HomePagePresenter presenter = new HomePagePresenter(view);

        Scene scene = new Scene(view.getRoot(), 1024, 768);
        stage.setTitle("Mängelmeldesystem");
        stage.setScene(scene);
        stage.show();
    }
}