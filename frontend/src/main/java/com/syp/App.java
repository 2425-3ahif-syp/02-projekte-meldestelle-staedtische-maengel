package com.syp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class App extends Application {

    private List<Complaint> complaints = new ArrayList<>();
    private Stage primaryStage;
    private Scene homeScene;
    private Scene createScene;
    private TextField searchField;
    private VBox complaintsList;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Testdaten
        complaints.add(new Complaint("Defekte Ampel", "Infrastruktur", "Kreuzung Hauptstraße/Lindenweg"));
        complaints.add(new Complaint("Müll im Park", "Environment", "Stadtpark Nähe Spielplatz"));
        complaints.add(new Complaint("Schlagloch", "Infrastruktur", "Bahnstraße Höhe Nr. 42"));
        complaints.add(new Complaint("Verkehrsschild beschädigt", "Public Safety", "Ecke Marktplatz/Kirchstraße"));

        createHomeScene();
        createComplaintScene();

        primaryStage.setTitle("Städtische Mängelmelder");
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }

    private void createHomeScene() {
        // Hauptcontainer
        BorderPane mainLayout = new BorderPane();
        mainLayout.setBackground(new Background(new BackgroundFill(Color.web("#f0f0f0"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Header mit Titel und Suchleiste
        VBox header = new VBox(15);
        header.setPadding(new Insets(20, 20, 30, 20));
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #2c3e50;");

        Label titleLabel = new Label("Städtische Mängelmelder");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.WHITE);

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);

        searchField = new TextField();
        searchField.setPromptText("Meldungen suchen...");
        searchField.setPrefWidth(300);

        Button searchBtn = new Button("Suchen");
        searchBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        searchBtn.setOnAction(e -> filterComplaints());

        searchBox.getChildren().addAll(searchField, searchBtn);
        header.getChildren().addAll(titleLabel, searchBox);
        mainLayout.setTop(header);

        // Meldungsliste
        complaintsList = new VBox(15);
        complaintsList.setPadding(new Insets(20));
        complaintsList.setAlignment(Pos.TOP_CENTER);
        refreshComplaintsList();

        ScrollPane scrollPane = new ScrollPane(complaintsList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        mainLayout.setCenter(scrollPane);

        // Footer mit Button
        Button newComplaintBtn = new Button("Neue Meldung erstellen");
        newComplaintBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 16px;");
        newComplaintBtn.setOnAction(e -> primaryStage.setScene(createScene));

        HBox footer = new HBox(newComplaintBtn);
        footer.setPadding(new Insets(20));
        footer.setAlignment(Pos.CENTER);
        mainLayout.setBottom(footer);

        homeScene = new Scene(mainLayout, 800, 600);
    }

    private void refreshComplaintsList() {
        complaintsList.getChildren().clear();

        if (complaints.isEmpty()) {
            Label emptyLabel = new Label("Keine Meldungen vorhanden");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
            complaintsList.getChildren().add(emptyLabel);
        } else {
            for (Complaint complaint : complaints) {
                complaintsList.getChildren().add(createComplaintCard(complaint));
            }
        }
    }

    private void filterComplaints() {
        String searchText = searchField.getText().toLowerCase();
        complaintsList.getChildren().clear();

        List<Complaint> filtered = complaints.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(searchText) ||
                        c.getCategory().toLowerCase().contains(searchText) ||
                        c.getLocation().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            Label noResults = new Label("Keine passenden Meldungen gefunden");
            noResults.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
            complaintsList.getChildren().add(noResults);
        } else {
            for (Complaint complaint : filtered) {
                complaintsList.getChildren().add(createComplaintCard(complaint));
            }
        }
    }

    private VBox createComplaintCard(Complaint complaint) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setMaxWidth(600);

        // Titel
        Label titleLabel = new Label(complaint.getTitle());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Kategorie mit farbigem Label
        HBox categoryBox = new HBox(5);
        Label categoryTag = new Label(complaint.getCategory());
        categoryTag.setStyle(getCategoryStyle(complaint.getCategory()));

        categoryBox.getChildren().addAll(new Label("Kategorie:"), categoryTag);

        // Ort
        Label locationLabel = new Label("Ort: " + complaint.getLocation());
        locationLabel.setStyle("-fx-text-fill: #34495e;");

        // Datum (wenn Sie dieses Feld hinzufügen)
        // Label dateLabel = new Label("Gemeldet am: " + complaint.getDate());
        // dateLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");

        // Status (optional)
        Label statusLabel = new Label("Status: Offen");
        statusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");

        VBox details = new VBox(5, categoryBox, locationLabel, statusLabel);

        card.getChildren().addAll(titleLabel, details);
        return card;
    }

    private String getCategoryStyle(String category) {
        return switch (category) {
            case "Infrastruktur" -> "-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 2 5; -fx-background-radius: 3;";
            case "Environment" -> "-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-padding: 2 5; -fx-background-radius: 3;";
            case "Public Safety" -> "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 2 5; -fx-background-radius: 3;";
            case "Health" -> "-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-padding: 2 5; -fx-background-radius: 3;";
            default -> "-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-padding: 2 5; -fx-background-radius: 3;";
        };
    }

    private void createComplaintScene() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("Neue Meldung erstellen");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Formularfelder
        TextField titleField = new TextField();
        titleField.setPromptText("Titel der Meldung");

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("None", "Health", "Infrastruktur", "Environment", "Public Safety");
        categoryBox.setValue("None");
        categoryBox.setPromptText("Kategorie auswählen");

        TextField locationField = new TextField();
        locationField.setPromptText("Standort");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Beschreibung");
        descriptionArea.setPrefHeight(100);

        // Bildauswahl
        ImageView imageView = new ImageView();
        imageView.setFitHeight(150);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        Button selectImageBtn = new Button("Bild auswählen");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Bilder", "*.png", "*.jpg", "*.jpeg"));

        selectImageBtn.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                imageView.setImage(new Image(file.toURI().toString()));
            }
        });

        // Submit Button
        Button submitBtn = new Button("Meldung absenden");
        submitBtn.setOnAction(e -> {
            Complaint newComplaint = new Complaint(
                    titleField.getText(),
                    categoryBox.getValue(),
                    locationField.getText()
            );

            complaints.add(newComplaint);
            createHomeScene(); // Aktualisiere die Home-Szene
            primaryStage.setScene(homeScene);
        });

        // Zurück Button
        Button backBtn = new Button("Zurück");
        backBtn.setOnAction(e -> primaryStage.setScene(homeScene));

        HBox buttons = new HBox(10, submitBtn, backBtn);
        buttons.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(
                titleLabel, titleField, categoryBox, locationField,
                descriptionArea, selectImageBtn, imageView, buttons
        );

        createScene = new Scene(layout, 600, 700);
    }

    public static void main(String[] args) {
        launch(args);
    }

    class Complaint {
        private String title;
        private String category;
        private String location;

        public Complaint(String title, String category, String location) {
            this.title = title;
            this.category = category;
            this.location = location;
        }

        // Getter methods
        public String getTitle() { return title; }
        public String getCategory() { return category; }
        public String getLocation() { return location; }


    }
}