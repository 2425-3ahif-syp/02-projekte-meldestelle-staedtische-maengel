package com.syp.util;

import org.h2.tools.Server;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;


public class Database {

    static {
        try {

            Server.createTcpServer(
                    "-tcp",
                    "-tcpAllowOthers",
                    "-tcpPort", "9092",
                    "-ifNotExists"
            ).start();
        } catch (SQLException e) {
            if (e.getErrorCode() == 90061) {
                System.out.println("Port 9092 ist bereits belegt – verwende bestehenden H2-Server.");
            } else {
                e.printStackTrace();
                throw new RuntimeException("Fehler beim Starten des H2-TCP-Servers", e);
            }
        }

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("H2-Treiber nicht gefunden", e);
        }

        initializeDatabase();
    }

    private static final String JDBC_URL =
            "jdbc:h2:tcp://localhost:9092/./backend/db/cityIssuesDb;AUTO_SERVER=TRUE";

    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASS);
    }

    private static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String sqlComplaint =
                    "CREATE TABLE IF NOT EXISTS COMPLAINT ("
                            + "  ID INT AUTO_INCREMENT PRIMARY KEY,"
                            + "  SUBJECT VARCHAR(255) NOT NULL,"
                            + "  CATEGORY VARCHAR(255),"
                            + "  ADDRESS VARCHAR(255),"
                            + "  DESCRIPTION VARCHAR(255),"
                            + "  IMAGEPATH VARCHAR(255),"
                            + "  STATUS VARCHAR(255),"
                            + "  CREATEDAT TIMESTAMP,"
                            + "  COMPLETEDAT TIMESTAMP"
                            + ");";
            stmt.execute(sqlComplaint);

            String sqlUser =
                    "CREATE TABLE IF NOT EXISTS APP_USER ("
                            + "  ID INT AUTO_INCREMENT PRIMARY KEY,"
                            + "  USERNAME VARCHAR(100) NOT NULL UNIQUE,"
                            + "  PASSWORD_HASH VARCHAR(255) NOT NULL,"
                            + "  ROLE VARCHAR(50)"
                            + ");";
            stmt.execute(sqlUser);

            String checkAdmin = "SELECT COUNT(*) FROM APP_USER";
            try (ResultSet rs = stmt.executeQuery(checkAdmin)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String defaultUsername = "admin";
                    String defaultPassword = "admin";
                    String hashed = BCrypt.hashpw(defaultPassword, BCrypt.gensalt());
                    String insertAdmin =
                            "INSERT INTO APP_USER (USERNAME, PASSWORD_HASH, ROLE) VALUES (?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertAdmin)) {
                        ps.setString(1, defaultUsername);
                        ps.setString(2, hashed);
                        ps.setString(3, "ADMIN");
                        ps.executeUpdate();
                        System.out.println("Default-Admin angelegt: admin / admin");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fehler beim Initialisieren der Tabellen in der DB", e);
        }
    }
}
