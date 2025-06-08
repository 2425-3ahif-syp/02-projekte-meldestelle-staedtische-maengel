package com.syp;

import com.syp.model.Complaint;
import com.syp.repository.ComplaintRepository;
import com.syp.util.Database;

import java.sql.*;
import java.util.List;

public class DbTest {
    public static void main(String[] args) {
        System.out.println("Starte Datenbank-Test...");

        try (Connection conn = Database.getConnection()) {
            System.out.println("Datenbank-Verbindung erfolgreich!");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        ComplaintRepository complaintRepository = new ComplaintRepository();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM APP_USER");
             ResultSet rs = ps.executeQuery()) {

            System.out.println("--- Alle Benutzer in APP_USER: ---");
            while (rs.next()) {
                System.out.println(
                        "ID=" + rs.getInt("ID") +
                                ", USERNAME=" + rs.getString("USERNAME") +
                                ", ROLE=" + rs.getString("ROLE")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Datenbank-Test abgeschlossen.");
    }
}
