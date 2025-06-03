package com.syp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static Database instance;

    private final static String URL = "jdbc:h2:tcp://localhost:9092/./cityIssuesDb";
    private final static String USERNAME = "sa";
    private final static String PASSWORD = "";

    private static Connection connection;

    private Database() {
        try {
            Class.forName("org.h2.Driver");

            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            initialize();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("H2 Driver not found.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Database connection failed.");
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        if (connection == null) {
            System.err.println("Database connection is null!");
        }
        return connection;
    }

    private void initialize() {
        String createComplaintTable = "CREATE TABLE IF NOT EXISTS complaint (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "subject VARCHAR(255) NOT NULL, " +
                "category VARCHAR(255), " +
                "address VARCHAR(255), " +
                "description VARCHAR(255), " +
                "imagePath VARCHAR(255), " +
                "status VARCHAR(50) DEFAULT 'Offen', " +
                "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "completedAt TIMESTAMP)";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createComplaintTable);

            // Beispiel-Daten einfügen (nur für Testzwecke)
            statement.execute("INSERT INTO complaint (subject, category, address, description, status) " +
                    "VALUES ('Schlagloch', 'Straßenschaden', 'Hauptstraße 1', 'Großes Schlagloch vor Nr. 1', 'Offen')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection was closed");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
