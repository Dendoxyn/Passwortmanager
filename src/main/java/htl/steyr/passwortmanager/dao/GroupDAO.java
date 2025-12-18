package htl.steyr.passwortmanager.dao;

import htl.steyr.passwortmanager.utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {

    public int createGroup(String groupName, int creatorId) throws Exception {

        String sql = """
            INSERT INTO `Group` (groupName, creatorId)
            VALUES (?, ?)
        """;

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, groupName);
            stmt.setInt(2, creatorId);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
            throw new Exception("Creating group failed, no ID returned.");
        }
    }

    public boolean groupExists(int groupId) throws Exception {

        String sql = """
            SELECT 1 FROM `Group` WHERE groupId = ?
        """;

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, groupId);
            return stmt.executeQuery().next();
        }
    }

    public List<Integer> getGroupIdsForUser(int userId) throws Exception {

        String sql = """
            SELECT groupId
            FROM UserGroup
            WHERE userId = ?
        """;

        List<Integer> groups = new ArrayList<>();

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                groups.add(rs.getInt("groupId"));
            }
        }
        return groups;
    }

    public void addUserToGroup(int userId, int groupId) throws Exception {

        String sql = """
            INSERT INTO UserGroup (userId, groupId)
            VALUES (?, ?)
        """;

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, groupId);
            stmt.executeUpdate();
        }
    }

    public void removeUserFromGroup(int userId, int groupId) throws Exception {

        String sql = """
            DELETE FROM UserGroup
            WHERE userId = ? AND groupId = ?
        """;

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, groupId);
            stmt.executeUpdate();
        }
    }
}
