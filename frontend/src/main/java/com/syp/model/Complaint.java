package com.syp.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Complaint {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty subject = new SimpleStringProperty();
    private StringProperty category = new SimpleStringProperty();
    private StringProperty address = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private StringProperty imagePath = new SimpleStringProperty();
    private StringProperty status = new SimpleStringProperty();
    private ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDateTime> completedAt = new SimpleObjectProperty<>();

    public Complaint(int id, String subject, String category, String address, String description, String imagePath) {
        setId(id);
        setSubject(subject);
        setCategory(category);
        setAddress(address);
        setDescription(description);
        setImagePath(imagePath);
        setStatus("Open");
        setCreatedAt(LocalDateTime.now());
        setCompletedAt(null);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String createdAtFormatted = (createdAt.get() != null) ? createdAt.get().format(formatter) : "N/A";
        String completedAtFormatted = (completedAt.get() != null) ? completedAt.get().format(formatter) : "Nicht erledigt";

        return "Complaint #" + id.get() +
                "\nSubject: " + subject.get() +
                "\nCategory: " + category.get() +
                "\nAddress: " + address.get() +
                "\nDescription: " + description.get() +
                "\nImage Path: " + (imagePath.get().isEmpty() ? "Kein Bild" : imagePath.get()) +
                "\nStatus: " + status.get() +
                "\nCreated at: " + createdAtFormatted +
                "\nDone at: " + completedAtFormatted;
    }

    public IntegerProperty getId() { return id; }
    public StringProperty getSubject() { return subject; }
    public StringProperty getCategory() { return category; }
    public StringProperty getAddress() { return address; }
    public StringProperty getDescription() { return description; }
    public StringProperty getImagePath() { return imagePath; }
    public StringProperty getStatus() { return status; }
    public ObjectProperty<LocalDateTime> getCreatedAt() { return createdAt; }
    public ObjectProperty<LocalDateTime> getCompletedAt() { return completedAt; }

    public void setId(int id) { this.id.set(id); }
    public void setSubject(String subject) { this.subject.set(subject); }
    public void setCategory(String category) { this.category.set(category); }
    public void setAddress(String address) { this.address.set(address); }
    public void setDescription(String description) { this.description.set(description); }
    public void setImagePath(String imagePath) { this.imagePath.set(imagePath); }
    public void setStatus(String status) { this.status.set(status); }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt.set(createdAt); }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt.set(completedAt); }
}
