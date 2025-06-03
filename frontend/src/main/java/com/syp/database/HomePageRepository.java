package com.syp.database;

import com.syp.model.Complaint;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HomePageRepository {
    private final Connection connection;

    public HomePageRepository() {
        this.connection = Database.getInstance().getConnection();
        initializeDatabase();
    }
    public List<Complaint> getAllComplaints() {
        return getFilteredComplaints(null, null, null, 1, Integer.MAX_VALUE);
    }

    private void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS complaints ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "subject VARCHAR(255) NOT NULL,"
                + "category VARCHAR(100) NOT NULL,"
                + "address VARCHAR(255) NOT NULL,"
                + "description TEXT,"
                + "image_path VARCHAR(255),"
                + "status VARCHAR(50) DEFAULT 'OPEN',"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "completed_at TIMESTAMP,"
                + "user_id INT)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }

    // Hauptmethode für gefilterte Abfragen mit Paginierung
    public List<Complaint> getFilteredComplaints(String searchTerm, String category,
                                                 String status, int page, int pageSize) {
        List<Complaint> complaints = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM complaints WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        // Filterbedingungen hinzufügen
        if (searchTerm != null && !searchTerm.isEmpty()) {
            sql.append(" AND (LOWER(subject) LIKE ? OR LOWER(description) LIKE ?)");
            parameters.add("%" + searchTerm.toLowerCase() + "%");
            parameters.add("%" + searchTerm.toLowerCase() + "%");
        }

        if (category != null && !category.isEmpty() && !category.equalsIgnoreCase("ALL")) {
            sql.append(" AND category = ?");
            parameters.add(category);
        }

        if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("ALL")) {
            sql.append(" AND status = ?");
            parameters.add(status);
        }

        // Sortierung und Paginierung
        sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");
        parameters.add(pageSize);
        parameters.add((page - 1) * pageSize);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    complaints.add(mapResultSetToComplaint(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching complaints: " + e.getMessage());
        }

        return complaints;
    }

    // Zählt die Gesamtanzahl der Meldungen mit gleichen Filtern
    public int getTotalComplaintCount(String searchTerm, String category, String status) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM complaints WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (searchTerm != null && !searchTerm.isEmpty()) {
            sql.append(" AND (LOWER(subject) LIKE ? OR LOWER(description) LIKE ?)");
            parameters.add("%" + searchTerm.toLowerCase() + "%");
            parameters.add("%" + searchTerm.toLowerCase() + "%");
        }

        if (category != null && !category.isEmpty() && !category.equalsIgnoreCase("ALL")) {
            sql.append(" AND category = ?");
            parameters.add(category);
        }

        if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("ALL")) {
            sql.append(" AND status = ?");
            parameters.add(status);
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting complaints: " + e.getMessage());
        }

        return 0;
    }

    // Neue Meldung erstellen
    public int createComplaint(Complaint complaint) {
        String sql = "INSERT INTO complaints (subject, category, address, "
                + "description, image_path, status, user_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, complaint.getSubject());
            stmt.setString(2, complaint.getCategory());
            stmt.setString(3, complaint.getAddress());
            stmt.setString(4, complaint.getDescription());
            stmt.setString(5, complaint.getImagePath());
            stmt.setString(6, complaint.getStatus());
            stmt.setObject(7, complaint.getId()); // Kann null sein

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating complaint: " + e.getMessage());
        }
        return -1;
    }

    // Meldung aktualisieren
    public boolean updateComplaint(Complaint complaint) {
        String sql = "UPDATE complaints SET "
                + "subject = ?, category = ?, address = ?, "
                + "description = ?, image_path = ?, status = ?, "
                + "completed_at = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, complaint.getSubject());
            stmt.setString(2, complaint.getCategory());
            stmt.setString(3, complaint.getAddress());
            stmt.setString(4, complaint.getDescription());
            stmt.setString(5, complaint.getImagePath());
            stmt.setString(6, complaint.getStatus());
            stmt.setTimestamp(7, complaint.getCompletedAt() != null ?
                    Timestamp.valueOf(complaint.getCompletedAt()) : null);
            stmt.setInt(8, complaint.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating complaint: " + e.getMessage());
        }
        return false;
    }

    // Hilfsmethode zur Umwandlung ResultSet → Complaint
    private Complaint mapResultSetToComplaint(ResultSet rs) throws SQLException {
        Complaint complaint = new Complaint();
        complaint.setId(rs.getInt("id"));
        complaint.setSubject(rs.getString("subject"));
        complaint.setCategory(rs.getString("category"));
        complaint.setAddress(rs.getString("address"));
        complaint.setDescription(rs.getString("description"));
        complaint.setImagePath(rs.getString("image_path"));
        complaint.setStatus(rs.getString("status"));
        complaint.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        Timestamp completedAt = rs.getTimestamp("completed_at");
        if (completedAt != null) {
            complaint.setCompletedAt(completedAt.toLocalDateTime());
        }

        complaint.setId(rs.getInt("user_id"));

        return complaint;
    }

    // Weitere nützliche Methoden
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM complaints";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching categories: " + e.getMessage());
        }
        return categories;
    }

    public List<String> getAllStatuses() {
        return List.of("OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED");
    }
}