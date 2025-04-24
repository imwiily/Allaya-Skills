package com.allaya.skills.skills;

import com.allaya.skills.database.DatabaseManager;

import java.sql.*;
import java.util.*;

public class SkillStorage {

    public static void createTable() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS player_skills_unlocked (" +
                    "uuid TEXT NOT NULL, skill TEXT NOT NULL, PRIMARY KEY(uuid, skill))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void save(UUID uuid, PlayerSkillData data) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement delete = conn.prepareStatement("DELETE FROM player_skills_unlocked WHERE uuid = ?");
            delete.setString(1, uuid.toString());
            delete.executeUpdate();

            PreparedStatement insert = conn.prepareStatement("INSERT INTO player_skills_unlocked (uuid, skill) VALUES (?, ?)");
            for (String skill : data.getUnlockedIds()) {
                insert.setString(1, uuid.toString());
                insert.setString(2, skill);
                insert.addBatch();
            }
            insert.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PlayerSkillData load(UUID uuid) {
        PlayerSkillData data = new PlayerSkillData();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT skill FROM player_skills_unlocked WHERE uuid = ?")) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            List<String> skills = new ArrayList<>();
            while (rs.next()) {
                skills.add(rs.getString("skill"));
            }
            data.setUnlocked(skills);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
