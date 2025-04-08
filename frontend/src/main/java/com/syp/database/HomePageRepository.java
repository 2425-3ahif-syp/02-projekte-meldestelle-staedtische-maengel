package com.syp.database;

import com.syp.model.Complaint;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HomePageRepository {
    private Connection connection;

    public HomePageRepository() {
        connection = Database.getInstance().getConnection();
    }

    public List<Complaint> getAllComplaints() {
        List<Complaint> complaintList = new ArrayList<>();
        String sql = "SELECT * FROM complaint";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                complaintList.add(new Complaint(
                        rs.getInt("id"),
                        rs.getString("subject"),
                        rs.getString("category"),
                        rs.getString("address"),
                        rs.getString("description"),
                        rs.getString("imagePath"),
                        rs.getString("status"),
                        rs.getTimestamp("createdAt").toLocalDateTime(),
                        rs.getTimestamp("completedAt") != null ? rs.getTimestamp("completedAt").toLocalDateTime() : null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return complaintList;
    }

    //search function
    public List<Complaint> searchData(String subject) {
        List<Complaint> complaintList = new ArrayList<>();
        String sql = "SELECT * FROM complaint WHERE LOWER(subject) LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + subject + "%");
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    complaintList.add(new Complaint(
                            rs.getInt("id"),
                            rs.getString("subject"),
                            rs.getString("category"),
                            rs.getString("address"),
                            rs.getString("description"),
                            rs.getString("imagePath"),
                            rs.getString("status"),
                            rs.getTimestamp("createdAt").toLocalDateTime(),
                            rs.getTimestamp("completedAt") != null ? rs.getTimestamp("completedAt").toLocalDateTime() : null
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return complaintList;
    }
}