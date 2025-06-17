package com.syp.view;

import com.syp.model.Complaint;
import com.syp.service.ComplaintService;
import com.syp.util.Config;
import com.syp.util.Toast;
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
    private VBox complaintsBox = new VBox(12);

    private ImageView detailImageView;
    private Label detailSubject;
    private Label detailCategory;
    private Label detailAddress;
    private Label detailStatus;
    private TextArea detailDescription;

    public HomePageView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("CityCare – Mängelmeldesystem");

        // Root und Header
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        HBox header = new HBox(10);
        header.getStyleClass().add("header");
        header.setPadding(new Insets(15,20,15,20));
        header.setAlignment(Pos.CENTER_LEFT);
        Label lblCity = new Label(Config.getCityName());
        lblCity.getStyleClass().add("header-label");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnLogin = new Button("🔒 Anmelden");
        btnLogin.getStyleClass().add("button");
        btnLogin.setOnAction(e -> openLoginDialog());
        header.getChildren().addAll(lblCity, spacer, btnLogin);
        root.setTop(header);

        // Links: Filter + Card-Liste
        VBox leftPane = new VBox(12);
        leftPane.setPadding(new Insets(10));

        HBox filterBox = new HBox(8);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Suchbegriff");

        categoryFilter = new ComboBox<>();
        categoryFilter.getItems().setAll("Alle","Straße","Laterne","Vandalismus");
        categoryFilter.setValue("Alle");

        statusFilter = new ComboBox<>();
        statusFilter.getItems().setAll("Alle","Offen","In Bearbeitung","Abgeschlossen");
        statusFilter.setValue("Alle");

        Button btnSearch = new Button("🔍 Suchen");
        btnSearch.getStyleClass().add("button");
        btnSearch.setOnAction(e -> loadFilteredData());

        Button btnNew = new Button("➕ Neue Meldung");
        btnNew.getStyleClass().add("button");
        btnNew.setOnAction(e -> {
            new CreateComplaintView().showAndWait();
            loadFilteredData();
        });

        filterBox.getChildren().addAll(
                searchField, categoryFilter, statusFilter, btnSearch, btnNew
        );

        ScrollPane scroll = new ScrollPane(complaintsBox);
        scroll.setFitToWidth(true);
        complaintsBox.setPadding(new Insets(10));

        leftPane.getChildren().addAll(filterBox, scroll);

        // Rechts: Detail-Ansicht
        VBox rightPane = new VBox(10);
        rightPane.getStyleClass().add("detail-container");
        rightPane.setPadding(new Insets(15));

        Label lblDetail = new Label("Detailansicht");
        lblDetail.getStyleClass().add("card-label-title");
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

        rightPane.getChildren().addAll(
                lblDetail,
                detailImageView,
                detailSubject,
                detailCategory,
                detailAddress,
                detailStatus,
                new Label("Beschreibung:"),
                detailDescription
        );

        // SplitPane
        SplitPane sp = new SplitPane(leftPane, rightPane);
        sp.setDividerPositions(0.5);
        root.setCenter(sp);

        // Footer
        HBox footer = new HBox();
        footer.getStyleClass().add("footer");
        footer.setPadding(new Insets(10));
        footer.setAlignment(Pos.CENTER);
        Label lblFoot = new Label("© 2025 CityCare | Alle Rechte vorbehalten");
        lblFoot.getStyleClass().add("footer-label");
        footer.getChildren().add(lblFoot);
        root.setBottom(footer);

        // Scene
        Scene scene = new Scene(root, 1100, 650);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        loadFilteredData();
    }

    private void loadFilteredData() {
        complaintsBox.getChildren().clear();
        String text = searchField.getText().trim();
        String cat  = "Alle".equals(categoryFilter.getValue()) ? null : categoryFilter.getValue();
        String stat = "Alle".equals(statusFilter.getValue())   ? null : statusFilter.getValue();

        List<Complaint> list = complaintService.getFilteredComplaints(
                text.isEmpty() ? null : text,
                cat, stat
        );
        if (list.isEmpty()) {
            Label none = new Label("Keine Meldungen gefunden.");
            none.getStyleClass().add("info-label");
            complaintsBox.getChildren().add(none);
            return;
        }

        for (Complaint c : list) {
            // Card erzeugen
            VBox card = new VBox(6);
            card.getStyleClass().add("card");
            card.setPadding(new Insets(12));
            card.prefWidthProperty().bind(complaintsBox.widthProperty().subtract(20));

            // Bild (Thumbnail)
            if (c.getImagePath() != null) {
                File imgF = new File(System.getProperty("user.dir"), c.getImagePath());
                if (imgF.exists()) {
                    ImageView iv = new ImageView(
                            new Image(imgF.toURI().toString(), 300, 0, true, true)
                    );
                    card.getChildren().add(iv);
                }
            }

            // Text-Labels
            Label lSubj = new Label("📌 " + c.getSubject());
            lSubj.getStyleClass().add("card-label-title");
            Label lCat  = new Label("Kategorie: " + c.getCategory());
            lCat.getStyleClass().add("card-label");
            Label lAddr = new Label("Standort: " + c.getAddress());
            lAddr.getStyleClass().add("card-label");
            Label lDate = new Label("Erstellt am: " +
                    (c.getCreatedAt() != null ? c.getCreatedAt().toString() : "-"));
            lDate.getStyleClass().add("card-label");

            // „Melden“-Button
            Button btnReport = new Button("🚩 Melden");
            btnReport.getStyleClass().add("card-button");
            btnReport.setOnAction(e -> {
                new ReportDialogView(c).showAndWait()
                        .ifPresent(reason -> {
                            // speichere Report
                            new com.syp.service.ReportService()
                                    .saveReport(c.getId(), reason);
                            Toast.show(primaryStage, "Vielen Dank für dein Feedback!");
                        });
            });

            // Klick auf Card lädt Detail
            card.setOnMouseClicked(e -> showDetails(c));

            card.getChildren().addAll(lSubj, lCat, lAddr, lDate, btnReport);
            complaintsBox.getChildren().add(card);
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

        // Bild groß
        if (c.getImagePath() != null) {
            File f = new File(System.getProperty("user.dir"), c.getImagePath());
            if (f.exists()) {
                detailImageView.setImage(new Image(f.toURI().toString()));
            } else {
                detailImageView.setImage(null);
            }
        } else {
            detailImageView.setImage(null);
        }

        detailSubject.setText("Betreff: "   + c.getSubject());
        detailCategory.setText("Kategorie: " + c.getCategory());
        detailAddress.setText("Standort: "   + c.getAddress());
        detailStatus.setText("Status: "      + c.getStatus());
        detailDescription.setText(
                c.getDescription() != null ? c.getDescription() : ""
        );
    }

    private void openLoginDialog() {
        boolean ok = new LoginView().showAndWait();
        if (ok) {
            new AdminDashboardView().show();
            loadFilteredData();
        }
    }
}
