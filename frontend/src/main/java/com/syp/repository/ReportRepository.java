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

    public List<Report> findByComplaintId(int complaintId) {
        String sql = "SELECT * FROM REPORT WHERE COMPLAINT_ID = ? ORDER BY REPORT_TIME DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, complaintId);
            ResultSet rs = ps.executeQuery();

            List<Report> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapResultSetToReport(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteByComplaintId(int complaintId) {
        String sql = "DELETE FROM REPORT WHERE COMPLAINT_ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, complaintId);
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
}
