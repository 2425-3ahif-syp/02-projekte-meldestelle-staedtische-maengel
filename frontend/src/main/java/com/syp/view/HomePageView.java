package com.syp.view;

import com.syp.model.Complaint;
import com.syp.service.ComplaintService;
import com.syp.util.Config;
import com.syp.util.Toast;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class HomePageView {
    private final Stage primaryStage;
    private final ComplaintService complaintService = new ComplaintService();

    private TextField searchField;
    private ComboBox<String> categoryFilter;
    private ComboBox<String> statusFilter;
    private ObservableList<Complaint> dataList;

    private VBox cardsContainer;

    private ImageView detailImageView;
    private Label detailSubject, detailCategory, detailAddress, detailStatus;
    private TextArea detailDescription;

    public HomePageView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("CityCare – Mängelmeldesystem");

        StackPane root = new StackPane();
        root.getStyleClass().add("root");

        HBox header = new HBox();
        header.getStyleClass().add("header");
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.CENTER_LEFT);

        Label lblCityName = new Label(Config.getCityName());
        lblCityName.getStyleClass().add("header-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnLogin = new Button("🔐 Anmelden");
        btnLogin.setTooltip(new Tooltip("Als Gemeinde anmelden"));
        btnLogin.getStyleClass().add("button");
        btnLogin.setOnAction(e -> openLoginDialog());

        header.getChildren().addAll(lblCityName, spacer, btnLogin);

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setPadding(new Insets(10));

        searchField = new TextField();
        searchField.setPromptText("Suchbegriff");

        categoryFilter = new ComboBox<>();
        categoryFilter.getItems().addAll("Alle", "Straße", "Laterne", "Vandalismus");
        categoryFilter.setValue("Alle");

        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("Alle", "Offen", "In Bearbeitung", "Abgeschlossen");
        statusFilter.setValue("Alle");

        Button btnSearch = new Button("🔍 Suchen");
        btnSearch.getStyleClass().add("button");
        btnSearch.setOnAction(e -> loadFilteredData());

        Button btnCreate = new Button("➕ Neue Meldung");
        btnCreate.getStyleClass().add("button");
        btnCreate.setOnAction(e -> openCreateComplaintDialog());

        filterBox.getChildren().addAll(searchField, categoryFilter, statusFilter, btnSearch, btnCreate);

        cardsContainer = new VBox(10);
        cardsContainer.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(600);
        scrollPane.setStyle("-fx-background-color: transparent;");


        VBox detailBox = new VBox(10);
        detailBox.getStyleClass().add("detail-container");
        detailBox.setPadding(new Insets(10));

        Label lblDetailsTitle = new Label("Detailansicht");
        lblDetailsTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        detailImageView = new ImageView();
        detailImageView.setFitWidth(250);
        detailImageView.setPreserveRatio(true);
        detailImageView.setSmooth(true);

        detailSubject = new Label("Betreff: ");
        detailCategory = new Label("Kategorie: ");
        detailAddress = new Label("Standort: ");
        detailStatus = new Label("Status: ");
        detailDescription = new TextArea();
        detailDescription.setWrapText(true);
        detailDescription.setEditable(false);
        detailDescription.setPrefRowCount(5);

        detailBox.getChildren().addAll(lblDetailsTitle, detailImageView, detailSubject, detailCategory, detailAddress, detailStatus, new Label("Beschreibung:"), detailDescription);

        SplitPane splitPane = new SplitPane(scrollPane, detailBox);
        splitPane.setDividerPositions(0.55);
        splitPane.setPadding(new Insets(10));


        HBox footer = new HBox();
        footer.getStyleClass().add("footer");
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        Label lblFooter = new Label("© 2025 CityCare | Alle Rechte vorbehalten");
        lblFooter.getStyleClass().add("footer-label");
        footer.getChildren().add(lblFooter);

        BorderPane layout = new BorderPane();
        layout.setTop(header);
        layout.setCenter(new VBox(filterBox, splitPane));
        layout.setBottom(footer);

        root.getChildren().add(layout);

        Scene scene = new Scene(root, 1100, 650);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        loadFilteredData();
    }

    private void loadFilteredData() {
        String text = searchField.getText().trim();
        String category = categoryFilter.getValue().equals("Alle") ? null : categoryFilter.getValue();
        String status = statusFilter.getValue().equals("Alle") ? null : statusFilter.getValue();

        try {
            List<Complaint> list = complaintService.getFilteredComplaints(
                    text.isEmpty() ? null : text, category, status
            );
            dataList = FXCollections.observableArrayList(list);
            updateCards();
        } catch (Exception ex) {
            Toast.show(primaryStage, "Fehler beim Laden der Daten.");
            ex.printStackTrace();
        }
    }

    private void updateCards() {
        cardsContainer.getChildren().clear();

        for (Complaint c : dataList) {
            VBox card = new VBox(5);
            card.getStyleClass().add("card");
            card.setPadding(new Insets(10));
            card.setOnMouseClicked(e -> showDetails(c));

            String relPath = c.getImagePath();
            if (relPath != null) {
                File file = new File(System.getProperty("user.dir") + File.separator + relPath);
                if (file.exists()) {
                    Image img = new Image(file.toURI().toString(), 120, 90, true, true);
                    ImageView imageView = new ImageView(img);
                    imageView.setSmooth(true);
                    card.getChildren().add(imageView);
                }
            }

            Label lblSubject = new Label("📌 " + c.getSubject());
            lblSubject.getStyleClass().add("card-label-title");

            Label lblCategory = new Label("📂 " + c.getCategory());
            Label lblAddress = new Label("📍 " + c.getAddress());
            Label lblStatus = new Label("📊 " + c.getStatus());
            Label lblDate = new Label("📅 " + (c.getCreatedAt() != null ? c.getCreatedAt().toString() : ""));

            lblCategory.getStyleClass().add("card-label");
            lblAddress.getStyleClass().add("card-label");
            lblStatus.getStyleClass().add("card-label");
            lblDate.getStyleClass().add("card-label");

            card.getChildren().addAll(lblSubject, lblCategory, lblAddress, lblStatus, lblDate);
            cardsContainer.getChildren().add(card);
        }
    }

    private void openCreateComplaintDialog() {
        CreateComplaintView createView = new CreateComplaintView();
        createView.showAndWait();
        loadFilteredData();
    }

    private void openLoginDialog() {
        LoginView loginView = new LoginView();
        if (loginView.showAndWait()) {
            AdminDashboardView adminView = new AdminDashboardView();
            adminView.show();
            loadFilteredData();
        }
    }

    private void showDetails(Complaint c) {
        if (c == null) {
            detailImageView.setImage(null);
            detailSubject.setText("Betreff: ");
            detailCategory.setText("Kategorie: ");
            detailAddress.setText("Standort: ");
            detailStatus.setText("Status: ");
            detailDescription.clear();
            return;
        }

        String relPath = c.getImagePath();
        if (relPath != null) {
            File file = new File(System.getProperty("user.dir") + File.separator + relPath);
            if (file.exists()) {
                Image img = new Image(file.toURI().toString());
                detailImageView.setImage(img);
            } else {
                detailImageView.setImage(null);
            }
        } else {
            detailImageView.setImage(null);
        }

        detailSubject.setText("Betreff: " + c.getSubject());
        detailCategory.setText("Kategorie: " + c.getCategory());
        detailAddress.setText("Standort: " + c.getAddress());
        detailStatus.setText("Status: " + c.getStatus());
        detailDescription.setText(c.getDescription() != null ? c.getDescription() : "");
    }
}
