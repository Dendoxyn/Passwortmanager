package htl.steyr.passwortmanager.dao;

import htl.steyr.passwortmanager.utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // ================= INSERT =================

    public void insertUser(String username, String hashedPwd, byte[] encryptionSalt) throws Exception {

        String sql = """
            INSERT INTO User (username, hashedPwd, encryptionSalt)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPwd);
            stmt.setBytes(3, encryptionSalt);

            stmt.executeUpdate();
        }
    }

    // ================= CHECK USERNAME =================

    public boolean usernameExists(String username) throws Exception {

        String sql = "SELECT 1 FROM User WHERE username = ?";

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ================= LOAD AUTH DATA =================

    public UserAuthData getAuthDataByUsername(String username) throws Exception {

        String sql = """
            SELECT userId, username, hashedPwd, encryptionSalt
            FROM User
            WHERE username = ?
        """;

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserAuthData(
                            rs.getInt("userId"),
                            rs.getString("username"),
                            rs.getString("hashedPwd"),
                            rs.getBytes("encryptionSalt")
                    );
                }
            }
        }

        return null;
    }

    // ================= RECORD =================

    public record UserAuthData(
            int userId,
            String username,
            String passwordHash,
            byte[] encryptionSalt
    ) {}
}
