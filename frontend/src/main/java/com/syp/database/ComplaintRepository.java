package com.syp.database;

import com.syp.model.Complaint;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintRepository {
    private final Connection connection;

    public ComplaintRepository() {
        this.connection = Database.getInstance().getConnection();
    }

    // Hauptmethoden mit Paginierung
    public List<Complaint> getAllComplaints(int page, int pageSize) {
        return getFilteredComplaints(null, null, null, page, pageSize);
    }

    public List<Complaint> getFilteredComplaints(String searchTerm, String category,
                                                 String status, int page, int pageSize) {
        List<Complaint> complaints = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM complaint WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        // Filterbedingungen
        if (searchTerm != null && !searchTerm.isEmpty()) {
            sql.append(" AND (LOWER(subject) LIKE ? OR LOWER(description) LIKE ?)");
            parameters.add("%" + searchTerm.toLowerCase() + "%");
            parameters.add("%" + searchTerm.toLowerCase() + "%");
        }

        if (category != null && !category.isEmpty()) {
            sql.append(" AND category = ?");
            parameters.add(category);
        }

        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
            parameters.add(status);
        }

        // Sortierung und Paginierung
        sql.append(" ORDER BY createdAt DESC LIMIT ? OFFSET ?");
        parameters.add(pageSize);
        parameters.add((page - 1) * pageSize);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            setParameters(stmt, parameters);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    complaints.add(mapResultSetToComplaint(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error fetching complaints", e);
        }
        return complaints;
    }

    // CRUD-Operationen
    public int createComplaint(Complaint complaint) {
        String sql = "INSERT INTO complaint (subject, category, address, description, " +
                "imagePath, status, createdAt, userId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, complaint.getSubject());
            stmt.setString(2, complaint.getCategory());
            stmt.setString(3, complaint.getAddress());
            stmt.setString(4, complaint.getDescription());
            stmt.setString(5, complaint.getImagePath());
            stmt.setString(6, complaint.getStatus());
            stmt.setTimestamp(7, Timestamp.valueOf(complaint.getCreatedAt()));
            stmt.setInt(8, complaint.getUserId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error creating complaint", e);
        }
        return -1;
    }

    public boolean updateComplaint(Complaint complaint) {
        String sql = "UPDATE complaint SET " +
                "subject = ?, category = ?, address = ?, " +
                "description = ?, imagePath = ?, status = ?, " +
                "completedAt = ? WHERE id = ?";

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
            handleSQLException("Error updating complaint", e);
        }
        return false;
    }

    public boolean deleteComplaint(int id) {
        String sql = "DELETE FROM complaint WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException("Error deleting complaint", e);
        }
        return false;
    }

    // Hilfsmethoden
    private Complaint mapResultSetToComplaint(ResultSet rs) throws SQLException {
        Complaint complaint = new Complaint();
        complaint.setId(rs.getInt("id"));
        complaint.setSubject(rs.getString("subject"));
        complaint.setCategory(rs.getString("category"));
        complaint.setAddress(rs.getString("address"));
        complaint.setDescription(rs.getString("description"));
        complaint.setImagePath(rs.getString("imagePath"));
        complaint.setStatus(rs.getString("status"));
        complaint.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());

        Timestamp completedAt = rs.getTimestamp("completedAt");
        if (completedAt != null) {
            complaint.setCompletedAt(completedAt.toLocalDateTime());
        }

        complaint.setUserId(rs.getInt("userId"));
        return complaint;
    }

    private void setParameters(PreparedStatement stmt, List<Object> parameters)
            throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            stmt.setObject(i + 1, parameters.get(i));
        }
    }

    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }

    // Zusätzliche nützliche Methoden
    public int getTotalComplaintCount(String searchTerm, String category, String status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM complaint WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (searchTerm != null && !searchTerm.isEmpty()) {
            sql.append(" AND (LOWER(subject) LIKE ? OR LOWER(description) LIKE ?)");
            parameters.add("%" + searchTerm.toLowerCase() + "%");
            parameters.add("%" + searchTerm.toLowerCase() + "%");
        }

        if (category != null && !category.isEmpty()) {
            sql.append(" AND category = ?");
            parameters.add(category);
        }

        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
            parameters.add(status);
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            setParameters(stmt, parameters);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error counting complaints", e);
        }
        return 0;
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM complaint";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            handleSQLException("Error fetching categories", e);
        }
        return categories;
    }
}