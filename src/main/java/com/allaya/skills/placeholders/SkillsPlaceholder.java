package com.allaya.skills.placeholders;

import com.allaya.skills.paths.PathData;
import com.allaya.skills.paths.PathLoader;
import com.allaya.skills.paths.PathManager;
import com.allaya.skills.paths.PlayerPathProgress;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


public class SkillsPlaceholder extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "skills";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Allaya Studios";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return "";

        UUID uuid = player.getUniqueId();

        if (identifier.startsWith("xp_")) {
            String pathId = identifier.substring("xp_".length()).toLowerCase();
            PlayerPathProgress progress = PathManager.getProgress(uuid, pathId);
            return progress != null ? String.valueOf(progress.currentXp) : "0";
        }

        if (identifier.startsWith("nextxp_")) {
            String pathId = identifier.substring("nextxp_".length()).toLowerCase();
            PlayerPathProgress progress = PathManager.getProgress(uuid, pathId);
            return progress != null ? String.valueOf(progress.xpNeeded) : "0";
        }

        if (identifier.startsWith("level_")) {
            String pathId = identifier.substring("level_".length()).toLowerCase();
            PlayerPathProgress progress = PathManager.getProgress(uuid, pathId);
            return progress != null ? String.valueOf(progress.level) : "1";
        }

        if (identifier.startsWith("points_")) {
            String pathId = identifier.substring("points_".length()).toLowerCase();
            PlayerPathProgress progress = PathManager.getProgress(uuid, pathId);
            return progress != null ? String.valueOf(progress.getAvailablePoints()) : "0";
        }

        if (identifier.startsWith("path_display_")) {
            String pathId = identifier.substring("path_display_".length()).toLowerCase();
            PathData pathData = PathLoader.getPath(pathId);
            return pathData != null ? pathData.displayName : pathId;
        }

        return null;
    }
}
