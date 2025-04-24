package com.allaya.skills.database;

import com.allaya.skills.AllayaSkills;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static HikariDataSource dataSource;

    public static void setupDatabase() {
        File dbFile = new File(AllayaSkills.getInstance().getDataFolder(), "skills.db");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
        config.setMaximumPoolSize(10);
        config.setPoolName("AllayaSkillsPool");

        dataSource = new HikariDataSource(config);

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS player_skills (" +
                            "uuid TEXT NOT NULL, " +
                            "skill TEXT NOT NULL, " +
                            "xp INTEGER NOT NULL DEFAULT 0, " +
                            "level INTEGER NOT NULL DEFAULT 1, " +
                            "PRIMARY KEY (uuid, skill))"
            );
        } catch (SQLException e) {
            AllayaSkills.getInstance().getLogger().severe("Erro ao criar a tabela do SQLite: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
