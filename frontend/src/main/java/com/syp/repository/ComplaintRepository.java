package com.syp.repository;

import com.syp.model.Complaint;
import com.syp.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintRepository {

    public List<Complaint> findAll() {
        String sql = "SELECT * FROM COMPLAINT ORDER BY CREATEDAT DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Complaint> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapResultSetToComplaint(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Complaint> findFiltered(String searchText, String category, String status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM COMPLAINT WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (searchText != null && !searchText.isBlank()) {
            sql.append(" AND LOWER(SUBJECT) LIKE ?");
            params.add("%" + searchText.toLowerCase() + "%");
        }
        if (category != null && !category.isBlank()) {
            sql.append(" AND CATEGORY = ?");
            params.add(category);
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND STATUS = ?");
            params.add(status);
        }
        sql.append(" ORDER BY CREATEDAT DESC");

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            List<Complaint> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapResultSetToComplaint(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Complaint c) {
        String sql = "INSERT INTO COMPLAINT "
                + "(SUBJECT, CATEGORY, ADDRESS, DESCRIPTION, IMAGEPATH, STATUS, CREATEDAT, USER_EMAIL) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getSubject());
            ps.setString(2, c.getCategory());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getDescription());
            ps.setString(5, c.getImagePath());
            ps.setString(6, "Offen");
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            ps.setString(8, c.getUserEmail());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                c.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStatus(int id, String newStatus) {
        String sql = "UPDATE COMPLAINT SET STATUS = ?, COMPLETEDAT = ? WHERE ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            if ("Abgeschlossen".equals(newStatus)) {
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            } else {
                ps.setTimestamp(2, null);
            }
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Complaint findById(int id) {
        String sql = "SELECT * FROM COMPLAINT WHERE ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToComplaint(rs);
            } else {
                return null; // oder Optional<Complaint> verwenden, wenn gewünscht
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM COMPLAINT WHERE ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Complaint mapResultSetToComplaint(ResultSet rs) throws SQLException {
        return new Complaint(
                rs.getInt("ID"),
                rs.getString("SUBJECT"),
                rs.getString("CATEGORY"),
                rs.getString("ADDRESS"),
                rs.getString("DESCRIPTION"),
                rs.getString("IMAGEPATH"),
                rs.getString("STATUS"),
                rs.getString("USER_EMAIL"),
                rs.getTimestamp("CREATEDAT"),
                rs.getTimestamp("COMPLETEDAT")
        );
    }
}
