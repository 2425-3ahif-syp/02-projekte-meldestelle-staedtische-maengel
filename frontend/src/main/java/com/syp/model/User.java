package com.syp.model;

import javafx.beans.property.*;

public class User {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty passwordHash = new SimpleStringProperty();
    private final StringProperty role = new SimpleStringProperty();

    public User(int id, String username, String passwordHash, String role) {
        this.id.set(id);
        this.username.set(username);
        this.passwordHash.set(passwordHash);
        this.role.set(role);
    }

    public IntegerProperty idProperty()    { return id; }
    public int getId()                     { return id.get(); }

    public StringProperty usernameProperty() { return username; }
    public String getUsername()               { return username.get(); }

    public StringProperty passwordHashProperty() { return passwordHash; }
    public String getPasswordHash()               { return passwordHash.get(); }

    public StringProperty roleProperty() { return role; }
    public String getRole()              { return role.get(); }
}
