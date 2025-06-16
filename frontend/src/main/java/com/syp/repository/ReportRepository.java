package com.syp.repository;

import com.syp.model.Report;
import com.syp.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportRepository {

    public void save(Report report) {
        String sql = "INSERT INTO REPORT (COMPLAINT_ID, REASON) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, report.getComplaintId());
            ps.setString(2, report.getReason());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Report mapResultSetToReport(ResultSet rs) throws SQLException {
        return new Report(
                rs.getInt("ID"),
                rs.getInt("COMPLAINT_ID"),
                rs.getString("REASON"),
                rs.getTimestamp("REPORT_TIME").toLocalDateTime()
        );
    }

    public Report findById(int id) {
        String sql = "SELECT * FROM REPORT WHERE ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReport(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // nicht gefunden
    }

    public List<Report> findAll() {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM REPORT ORDER BY REPORT_TIME DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reports.add(mapResultSetToReport(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reports;
    }
}
