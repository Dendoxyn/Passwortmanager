package htl.steyr.passwortmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static final String URL = "jdbc:mariadb://localhost:3306/passwordmanager";
    private static final String USER = "user";
    private static final String PASS = "password";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
