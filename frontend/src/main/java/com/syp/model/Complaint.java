package com.syp.model;

import javafx.beans.property.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Complaint {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty subject = new SimpleStringProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty imagePath = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> completedAt = new SimpleObjectProperty<>();
    private final IntegerProperty userId = new SimpleIntegerProperty();

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    // Konstruktoren
    public Complaint() {}

    public Complaint(int id, String subject, String category, String address,
                     String description, String imagePath, String status,
                     LocalDateTime createdAt, LocalDateTime completedAt, int userId) {
        setId(id);
        setSubject(subject);
        setCategory(category);
        setAddress(address);
        setDescription(description);
        setImagePath(imagePath);
        setStatus(status);
        setCreatedAt(createdAt);
        setCompletedAt(completedAt);
        setUserId(userId);
    }

    // Property Getter für JavaFX Binding
    public IntegerProperty idProperty() { return id; }
    public StringProperty subjectProperty() { return subject; }
    public StringProperty categoryProperty() { return category; }
    public StringProperty addressProperty() { return address; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty imagePathProperty() { return imagePath; }
    public StringProperty statusProperty() { return status; }
    public ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }
    public ObjectProperty<LocalDateTime> completedAtProperty() { return completedAt; }
    public IntegerProperty userIdProperty() { return userId; }

    // Standard Getter/Setter
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getSubject() { return subject.get(); }
    public void setSubject(String subject) { this.subject.set(subject); }

    public String getCategory() { return category.get(); }
    public void setCategory(String category) { this.category.set(category); }

    public String getAddress() { return address.get(); }
    public void setAddress(String address) { this.address.set(address); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public String getImagePath() { return imagePath.get(); }
    public void setImagePath(String imagePath) { this.imagePath.set(imagePath); }

    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }

    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt.set(createdAt); }

    public LocalDateTime getCompletedAt() { return completedAt.get(); }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt.set(completedAt); }

    public int getUserId() { return userId.get(); }
    public void setUserId(int userId) { this.userId.set(userId); }

    // Formatierte Datumsstrings
    public String getFormattedCreatedAt() {
        return getCreatedAt() != null ? getCreatedAt().format(DATE_FORMATTER) : "";
    }

    public String getFormattedCompletedAt() {
        return getCompletedAt() != null ? getCompletedAt().format(DATE_FORMATTER) : "";
    }

    // Hilfsmethoden
    public boolean isCompleted() {
        String status = getStatus();
        return "ERLEDIGT".equalsIgnoreCase(status) ||
                "CLOSED".equalsIgnoreCase(status) ||
                "RESOLVED".equalsIgnoreCase(status);
    }

    public String getShortDescription() {
        String desc = getDescription();
        if (desc == null || desc.isEmpty()) return "";
        return desc.length() > 50 ? desc.substring(0, 47) + "..." : desc;
    }

    // Mapping-Methode (sollte eigentlich im Repository sein)
    public static Complaint fromResultSet(ResultSet rs) throws SQLException {
        Complaint complaint = new Complaint();
        complaint.setId(rs.getInt("id"));
        complaint.setSubject(rs.getString("subject"));
        complaint.setCategory(rs.getString("category"));
        complaint.setAddress(rs.getString("address"));
        complaint.setDescription(rs.getString("description"));
        complaint.setImagePath(rs.getString("image_path"));
        complaint.setStatus(rs.getString("status"));
        complaint.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        if (rs.getTimestamp("completed_at") != null) {
            complaint.setCompletedAt(rs.getTimestamp("completed_at").toLocalDateTime());
        }

        complaint.setUserId(rs.getInt("user_id"));
        return complaint;
    }

    @Override
    public String toString() {
        return String.format("Meldung #%d: %s (%s, %s)",
                getId(), getSubject(), getCategory(), getStatus());
    }
}