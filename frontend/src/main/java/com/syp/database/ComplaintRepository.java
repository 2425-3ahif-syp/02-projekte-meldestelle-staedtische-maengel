package com.syp.database;

import com.syp.model.Complaint;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintRepository {
    private Connection connection;

    public ComplaintRepository() {
        connection = Database.getInstance().getConnection();
    }

    public List<Complaint> getAllComplaints() {
        List<Complaint> complaintList = new ArrayList<>();
        String sql = "SELECT * FROM complaint";

        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql)
        ) {
            while (rs.next()) {
                complaintList.add(new Complaint(
                                rs.getInt("id"),
                                rs.getString("subject"),
                                rs.getString("category"),
                                rs.getString("address"),
                                rs.getString("description"),
                                rs.getString("imagePath")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return complaintList;
    }

    public void addComplaint(Complaint complaint) {
        String sql = "INSERT INTO complaint (subject, category, address, description, imagePath, status, createdAt, completedAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, complaint.getSubject().get());
            pstmt.setString(2, complaint.getCategory().get());
            pstmt.setString(3, complaint.getAddress().get());
            pstmt.setString(4, complaint.getDescription().get());
            pstmt.setString(5, complaint.getImagePath().get());
            pstmt.setString(6, complaint.getStatus().get());
            pstmt.setTimestamp(7, Timestamp.valueOf(complaint.getCreatedAt().get()));
            pstmt.setTimestamp(8, complaint.getCompletedAt().get() != null ? Timestamp.valueOf(complaint.getCompletedAt().get()) : null);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateComplaint(Complaint complaint) {
        String sql = "UPDATE complaint " +
                "SET subject = ?, category = ?, address = ?, description = ?, imagePath = ?, status = ?, createdAt = ?, completedAt = ? " +
                "WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, complaint.getSubject().get());
            pstmt.setString(2, complaint.getCategory().get());
            pstmt.setString(3, complaint.getAddress().get());
            pstmt.setString(4, complaint.getDescription().get());
            pstmt.setString(5, complaint.getImagePath().get());
            pstmt.setString(6, complaint.getStatus().get());
            pstmt.setTimestamp(7, Timestamp.valueOf(complaint.getCreatedAt().get()));
            pstmt.setTimestamp(8, complaint.getCompletedAt().get() != null ? Timestamp.valueOf(complaint.getCompletedAt().get()) : null);
            pstmt.setInt(9, complaint.getId().get());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteComplaint(int id) {
        String sql = "DELETE FROM complaint WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
