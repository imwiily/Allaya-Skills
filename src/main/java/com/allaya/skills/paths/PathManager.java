package com.allaya.skills.paths;

import com.allaya.skills.bossbar.XPBossBarManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PathManager {

    // In-memory cache por jogador
    private static final Map<UUID, Map<String, PlayerPathProgress>> playerPaths = new HashMap<>();

    public static PlayerPathProgress getProgress(UUID uuid, String pathId) {
        playerPaths.putIfAbsent(uuid, new HashMap<>());
        Map<String, PlayerPathProgress> paths = playerPaths.get(uuid);

        if (!paths.containsKey(pathId)) {
            PathData path = PathLoader.getPath(pathId);
            if (path == null) return null;

            paths.put(pathId, new PlayerPathProgress(1, 0, path.getXpForLevel(1), 0, 0));
        }

        return paths.get(pathId);
    }

    public static void addXp(Player player, String pathId, int amount) {
        PathData path = PathLoader.getPath(pathId);
        if (path == null) return;

        UUID uuid = player.getUniqueId();
        PlayerPathProgress progress = getProgress(uuid, pathId);
        if (progress == null) return;

        progress.currentXp += amount;

        while (progress.currentXp >= progress.xpNeeded) {
            progress.currentXp -= progress.xpNeeded;
            progress.level++;
            progress.xpNeeded = path.getXpForLevel(progress.level);
            progress.addPoint();

            player.sendMessage("§aVocê subiu para o nível §e" + progress.level + " §ano caminho §b" + path.displayName + "§a!");
        }

        // Atualiza ou reinicia a bossbar, sem criar duplicadas
        XPBossBarManager.update(player, pathId);
    }

    public static boolean usePoint(UUID uuid, String pathId) {
        PlayerPathProgress progress = getProgress(uuid, pathId);
        if (progress != null && progress.hasAvailablePoints()) {
            progress.usePoint();
            return true;
        }
        return false;
    }

    public static PlayerPathProgress getOrCreateProgress(UUID uuid, String pathId) {
        playerPaths.putIfAbsent(uuid, new HashMap<>());
        Map<String, PlayerPathProgress> paths = playerPaths.get(uuid);

        PlayerPathProgress progress = paths.get(pathId.toLowerCase());
        if (progress == null) {
            PathData path = PathLoader.getPath(pathId);
            if (path == null) return null;

            progress = new PlayerPathProgress(1, 0, path.getXpForLevel(1), 0, 0);
            paths.put(pathId.toLowerCase(), progress);
        }

        return progress;
    }

    public static Map<String, PlayerPathProgress> getAllProgress(UUID uuid) {
        return playerPaths.getOrDefault(uuid, new HashMap<>());
    }
}
