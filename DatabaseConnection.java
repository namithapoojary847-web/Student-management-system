package com.student.management.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Make sure this method is public and static
    public static Connection getConnection() throws SQLException {
    	String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1"; // your Oracle DB URL
        String username = "ssms_user"; // your DB user name
        String password = "ssms123";   // your DB password

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver"); // load Oracle driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return DriverManager.getConnection(url, username, password);
    }
}