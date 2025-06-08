package com.syp.model;

import javafx.beans.property.*;
import java.sql.Timestamp;

public class Complaint {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty subject = new SimpleStringProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty imagePath = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty userEmail = new SimpleStringProperty();

    private final ObjectProperty<Timestamp> createdAt = new SimpleObjectProperty<>();
    private final ObjectProperty<Timestamp> completedAt = new SimpleObjectProperty<>();

    public Complaint(int id, String subject, String category, String address,
                     String description, String imagePath, String status, String userEmail,
                     Timestamp createdAt, Timestamp completedAt) {
        this.id.set(id);
        this.subject.set(subject);
        this.category.set(category);
        this.address.set(address);
        this.description.set(description);
        this.imagePath.set(imagePath);
        this.status.set(status);
        this.userEmail.set(userEmail);
        this.createdAt.set(createdAt);
        this.completedAt.set(completedAt);
    }

    public IntegerProperty idProperty() { return id; }
    public int getId()           { return id.get(); }
    public void setId(int id)    { this.id.set(id); }

    public StringProperty subjectProperty() { return subject; }
    public String getSubject()              { return subject.get(); }
    public void setSubject(String s)        { subject.set(s); }

    public StringProperty categoryProperty() { return category; }
    public String getCategory()              { return category.get(); }
    public void setCategory(String c)        { category.set(c); }

    public StringProperty addressProperty() { return address; }
    public String getAddress()              { return address.get(); }
    public void setAddress(String a)        { address.set(a); }

    public StringProperty descriptionProperty() { return description; }
    public String getDescription()               { return description.get(); }
    public void setDescription(String d)         { description.set(d); }

    public StringProperty imagePathProperty() { return imagePath; }
    public String getImagePath()              { return imagePath.get(); }
    public void setImagePath(String p)        { imagePath.set(p); }

    public StringProperty statusProperty() { return status; }
    public String getStatus()              { return status.get(); }
    public void setStatus(String s)        { status.set(s); }

    public ObjectProperty<Timestamp> createdAtProperty() { return createdAt; }
    public Timestamp getCreatedAt()                     { return createdAt.get(); }
    public void setCreatedAt(Timestamp t)                { createdAt.set(t); }

    public ObjectProperty<Timestamp> completedAtProperty() { return completedAt; }
    public Timestamp getCompletedAt()                      { return completedAt.get(); }
    public void setCompletedAt(Timestamp t)                { completedAt.set(t); }

    public StringProperty userEmailProperty() { return userEmail; }
    public String getUserEmail() { return userEmail.get(); }
    public void setUserEmail(String email) { this.userEmail.set(email); }
}
