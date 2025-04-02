package com.syp.view;

import com.syp.database.HomePageRepository;
import com.syp.model.Complaint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomePagePresenter {
    private final HomePageView view;
    private final HomePageRepository repo;
    private final ObservableList<Complaint> dataList = FXCollections.observableArrayList();

    public HomePagePresenter(HomePageView view) {
        this.view = view;
        this.repo = new HomePageRepository();
        bindViewToModel();
        attachEvents();
        init();
    }

    private void bindViewToModel() {
        view.setComplaints(dataList);
    }

    private void attachEvents() {
        view.getSearchButton().setOnAction(_ -> search());
        view.getCreateReportButton().setOnAction(_ -> createReport());
    }

    private void init() {
        reloadDataList();
        updateHeader("Home Page - [Stadt Name]");
        updateFooter("Meldungen.com");
    }

    private void reloadDataList() {
        dataList.clear();
        dataList.addAll(repo.getAllComplaints());
        view.setComplaints(dataList);
    }

    private void search() {
        String searchText = view.getSearchField().getText().toLowerCase();
        if (!searchText.isEmpty()) {
            dataList.setAll(repo.searchData(searchText));
        } else {
            reloadDataList();
        }
        view.setComplaints(dataList);
    }

    private void createReport() {
        // Logic to create a report - Not implemented
    }

    private void updateHeader(String text) {
        view.getHeader().getChildren().clear();
        view.getHeader().getChildren().add(new Text(text));
    }

    private void updateFooter(String text) {
        view.getFooter().getChildren().clear();
        view.getFooter().getChildren().add(new Text(text));
    }

    public static void show(Stage stage) {
        HomePageView view = new HomePageView(new HomePageRepository().getAllComplaints());
        HomePagePresenter presenter = new HomePagePresenter(view);

        Scene scene = new Scene(view.getRoot());
        stage.setTitle("Home Page");
        stage.setScene(scene);
        stage.show();
    }
}