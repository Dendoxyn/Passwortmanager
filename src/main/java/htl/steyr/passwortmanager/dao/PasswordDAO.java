package htl.steyr.passwortmanager.dao;

import htl.steyr.passwortmanager.model.Password;
import htl.steyr.passwortmanager.model.PasswordTag;
import htl.steyr.passwortmanager.model.SecurityLevel;
import htl.steyr.passwortmanager.utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PasswordDAO {

    public void insert(Password password) throws Exception {

        String sql = """
            INSERT INTO Password
            (website_app, loginName, encryptedPwd, note, tag, securityTag, userId, groupId)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, password.getWebsiteApp());
            stmt.setString(2, password.getLoginName());
            stmt.setBytes(3, password.getEncryptedPwd());
            stmt.setString(4, password.getNote());
            stmt.setString(5, password.getTag().name());
            stmt.setString(6, password.getSecurity().name());
            stmt.setInt(7, password.getUserId());

            if (password.getGroupId() == null) {
                stmt.setNull(8, Types.INTEGER);
            } else {
                stmt.setInt(8, password.getGroupId());
            }

            stmt.executeUpdate();
        }
    }

    public List<Password> findPrivatePasswords(int userId) throws Exception {

        String sql = """
            SELECT *
            FROM Password
            WHERE userId = ? AND groupId IS NULL
        """;

        return fetchPasswords(sql, userId);
    }

    public List<Password> findGroupPasswords(int groupId) throws Exception {

        String sql = """
            SELECT *
            FROM Password
            WHERE groupId = ?
        """;

        return fetchPasswords(sql, groupId);
    }

    private List<Password> fetchPasswords(String sql, int id) throws Exception {

        List<Password> result = new ArrayList<>();

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }
        return result;
    }

    public void delete(int passwordId) throws Exception {

        String sql = """
            DELETE FROM Password WHERE passwordId = ?
        """;

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, passwordId);
            stmt.executeUpdate();
        }
    }

    private Password mapRow(ResultSet rs) throws Exception {

        return new Password(
                rs.getInt("passwordId"),
                rs.getString("website_app"),
                rs.getString("loginName"),
                rs.getBytes("encryptedPwd"),
                rs.getString("note"),
                PasswordTag.valueOf(rs.getString("tag")),
                SecurityLevel.valueOf(rs.getString("securityTag")),
                rs.getInt("userId"),
                rs.getObject("groupId", Integer.class)
        );
    }
}
