package htl.steyr.passwortmanager.dao;

import htl.steyr.passwortmanager.utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void insertUser(String username, String hashedPwd) throws Exception {

        String sql = """
            INSERT INTO User (username, hashedPwd)
            VALUES (?, ?)
        """;

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPwd);

            stmt.executeUpdate();
        }
    }

    public boolean usernameExists(String username) throws Exception {

        String sql = """
            SELECT 1 FROM User WHERE username = ?
        """;

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    public String userLogin(String username, String hashedPassword) throws Exception{

        String sql = """
                
                SELECT 1 FROM User WHERE username = ? 
                
                """;

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
        }


        return "ok";
    }

    public String getPasswordHashByUsername(String username) throws Exception {

        String sql = """
        SELECT hashedPwd
        FROM User
        WHERE username = ?
    """;

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("hashedPwd");
                }
            }
        }

        return null;
    }


}
