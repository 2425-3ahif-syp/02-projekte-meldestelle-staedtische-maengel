package com.syp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Database instance;
    
    private final static String URL = "jdbc:h2:tcp://localhost:9092/./cityIssuesDb";
    private final static String USERNAME = "sa";
    private final static String PASSWORD = "";

    private static Connection connection;

    private Database() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
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
        return connection;
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
