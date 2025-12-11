package htl.steyr.passwortmanager.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static final String URL = "jdbc:mariadb://localhost:3306/passwordmanager";
    private static final String USER = "user";
    private static final String PASS = "password";

    public static Connection connect() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("✅ DATABASE CONNECTION OK");
            return conn;
        } catch (SQLException e) {
            System.out.println("❌ DATABASE CONNECTION FAILED");
            System.out.println("URL  = " + URL);
            System.out.println("USER = " + USER);
            e.printStackTrace();
            throw e;
        }
    }

}
