package com.syp.view;

import com.syp.model.Complaint;
import com.syp.service.ComplaintService;
import com.syp.util.Config;
import com.syp.util.Toast;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
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

    private TableView<Complaint> tableComplaints;
    private TextField searchField;
    private ComboBox<String> categoryFilter;
    private ComboBox<String> statusFilter;
    private ObservableList<Complaint> dataList;

    // Detailbereich rechts
    private ImageView detailImageView;
    private Label detailSubject;
    private Label detailCategory;
    private Label detailAddress;
    private Label detailStatus;
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

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        Button btnLogin = new Button("Anmelden");
        btnLogin.getStyleClass().add("button");
        btnLogin.setOnAction(e -> openLoginDialog());

        header.getChildren().addAll(lblCityName, headerSpacer, btnLogin);

        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.55);
        splitPane.setPadding(new Insets(10));

        VBox leftBox = new VBox();
        leftBox.setSpacing(10);

        HBox filterBox = new HBox();
        filterBox.setSpacing(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Suchbegriff");

        categoryFilter = new ComboBox<>();
        categoryFilter.getItems().addAll("Alle", "Straße", "Laterne", "Vandalismus");
        categoryFilter.setValue("Alle");

        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("Alle", "Offen", "In Bearbeitung", "Abgeschlossen");
        statusFilter.setValue("Alle");

        Button btnSearch = new Button("Suchen");
        btnSearch.getStyleClass().add("button");
        btnSearch.setOnAction(e -> loadFilteredData());

        Button btnCreateReport = new Button("Neue Meldung");
        btnCreateReport.getStyleClass().add("button");
        btnCreateReport.setOnAction(e -> openCreateComplaintDialog());

        filterBox.getChildren().addAll(
                searchField, categoryFilter, statusFilter, btnSearch, btnCreateReport
        );

        tableComplaints = new TableView<>();
        tableComplaints.setPlaceholder(new Label("Keine Meldungen vorhanden"));
        tableComplaints.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Complaint, ImageView> colImage = new TableColumn<>("Bild");
        colImage.setCellValueFactory(cell -> {
            String relPath = cell.getValue().getImagePath();
            if (relPath != null) {
                File file = new File(System.getProperty("user.dir") + File.separator + relPath);
                if (file.exists()) {
                    Image img = new Image(file.toURI().toString(), 50, 50, true, true);
                    ImageView iv = new ImageView(img);
                    iv.setSmooth(true);
                    return new ReadOnlyObjectWrapper<>(iv);
                }
            }
            return new ReadOnlyObjectWrapper<>(null);
        });
        colImage.setPrefWidth(60);
        colImage.setResizable(false);

        TableColumn<Complaint, String> colSubject = new TableColumn<>("Betreff");
        colSubject.setCellValueFactory(cell -> cell.getValue().subjectProperty());
        colSubject.setPrefWidth(180);

        TableColumn<Complaint, String> colCategory = new TableColumn<>("Kategorie");
        colCategory.setCellValueFactory(cell -> cell.getValue().categoryProperty());
        colCategory.setPrefWidth(100);

        TableColumn<Complaint, String> colAddress = new TableColumn<>("Standort");
        colAddress.setCellValueFactory(cell -> cell.getValue().addressProperty());
        colAddress.setPrefWidth(140);

        TableColumn<Complaint, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(cell -> cell.getValue().statusProperty());
        colStatus.setPrefWidth(120);

        TableColumn<Complaint, String> colCreatedAt = new TableColumn<>("Erstellt am");
        colCreatedAt.setCellValueFactory(cell -> {
            if (cell.getValue().getCreatedAt() != null) {
                return new SimpleStringProperty(cell.getValue().getCreatedAt().toString());
            }
            return new SimpleStringProperty("");
        });
        colCreatedAt.setPrefWidth(140);

        tableComplaints.getColumns().addAll(
                colImage, colSubject, colCategory, colAddress, colStatus, colCreatedAt
        );

        dataList = FXCollections.observableArrayList();
        tableComplaints.setItems(dataList);

        tableComplaints.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            showDetails(newSel);
        });

        leftBox.getChildren().addAll(filterBox, tableComplaints);

        VBox rightBox = new VBox();
        rightBox.getStyleClass().add("detail-container");
        rightBox.setSpacing(10);
        rightBox.setPadding(new Insets(10));

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
        detailDescription.setPrefRowCount(4);
        detailDescription.setPrefColumnCount(20);

        rightBox.getChildren().addAll(
                lblDetailsTitle,
                detailImageView,
                detailSubject,
                detailCategory,
                detailAddress,
                detailStatus,
                new Label("Beschreibung:"),
                detailDescription
        );

        splitPane.getItems().addAll(leftBox, rightBox);

        HBox footer = new HBox();
        footer.getStyleClass().add("footer");
        footer.setPadding(new Insets(10));
        footer.setAlignment(Pos.CENTER);
        Label lblFooter = new Label("© 2025 CityCare | Alle Rechte vorbehalten");
        lblFooter.getStyleClass().add("footer-label");
        footer.getChildren().add(lblFooter);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(header);
        borderPane.setCenter(splitPane);
        borderPane.setBottom(footer);

        root.getChildren().add(borderPane);

        Scene scene = new Scene(root, 1100, 650);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        loadFilteredData();
    }

    private void loadFilteredData() {
        String text = searchField.getText().trim();
        String category = categoryFilter.getValue();
        if ("Alle".equals(category)) category = null;
        String status = statusFilter.getValue();
        if ("Alle".equals(status)) status = null;

        try {
            List<Complaint> list = complaintService.getFilteredComplaints(
                    text.isEmpty() ? null : text, category, status
            );
            dataList.setAll(list);
        } catch (Exception ex) {
            Toast.show(primaryStage, "Fehler beim Laden der Daten.");
            ex.printStackTrace();
        }
    }


    private void openCreateComplaintDialog() {
        CreateComplaintView createView = new CreateComplaintView();
        createView.showAndWait();
        loadFilteredData();
    }


    private void openLoginDialog() {
        LoginView loginView = new LoginView();
        boolean success = loginView.showAndWait();
        if (success) {
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
