package com.allaya.skills.managers;

import com.allaya.skills.AllayaSkills;
import com.allaya.skills.database.DatabaseManager;
import com.allaya.skills.utils.MessageUtils;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SkillXPManager {

    public static int getXP(UUID uuid, String skill) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT xp FROM player_skills WHERE uuid = ? AND skill = ?")) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, skill.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("xp") : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getLevel(UUID uuid, String skill) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT level FROM player_skills WHERE uuid = ? AND skill = ?")) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, skill.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("level") : 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static void addXP(UUID uuid, String skill, int amount) {
        int currentXP = getXP(uuid, skill);
        int currentLevel = getLevel(uuid, skill);
        int newXP = currentXP + amount;

        int requiredXP = currentLevel * 100;
        int finalLevel = currentLevel;

        while (newXP >= requiredXP) {
            newXP -= requiredXP;
            finalLevel++;
            requiredXP = finalLevel * 100;
        }

        saveSkill(uuid, skill, newXP, finalLevel);

        Player player = AllayaSkills.getInstance().getServer().getPlayer(uuid);
        if (player != null) {
            player.sendMessage(MessageUtils.format("xp-gain",
                    "%skill%", capitalize(skill),
                    "%xp%", String.valueOf(amount)
            ));

            if (finalLevel > currentLevel) {
                player.sendMessage(MessageUtils.format("level-up",
                        "%skill%", capitalize(skill),
                        "%level%", String.valueOf(finalLevel)
                ));
            }
        }
    }

    public static void setLevel(UUID uuid, String skill, int level) {
        saveSkill(uuid, skill, getXP(uuid, skill), level);
    }

    private static void saveSkill(UUID uuid, String skill, int xp, int level) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "REPLACE INTO player_skills (uuid, skill, xp, level) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, skill.toLowerCase());
            stmt.setInt(3, xp);
            stmt.setInt(4, level);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
