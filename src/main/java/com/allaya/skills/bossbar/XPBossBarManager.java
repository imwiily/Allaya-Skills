package com.allaya.skills.bossbar;

import com.allaya.skills.AllayaSkills;
import com.allaya.skills.paths.PathData;
import com.allaya.skills.paths.PathLoader;
import com.allaya.skills.paths.PathManager;
import com.allaya.skills.paths.PlayerPathProgress;
import org.bukkit.Bukkit;
import org.bukkit.boss.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class XPBossBarManager {

    private static final Map<String, BossBar> bars = new HashMap<>();
    private static final Map<String, Integer> timers = new HashMap<>();

    public static void update(Player player, String pathId) {
        FileConfiguration config = AllayaSkills.getInstance().getConfig();
        if (!config.getBoolean("bossbar.enabled", true)) return;

        UUID uuid = player.getUniqueId();
        PlayerPathProgress progress = PathManager.getProgress(uuid, pathId);
        PathData path = PathLoader.getPath(pathId);

        if (progress == null || path == null) return;

        String key = uuid.toString() + ":" + pathId;

        // Cria ou atualiza a bossbar
        BossBar bar = bars.get(key);
        if (bar == null) {
            BarColor color = BarColor.valueOf(config.getString("bossbar.color", "GREEN").toUpperCase());
            BarStyle style = BarStyle.valueOf(config.getString("bossbar.style", "SEGMENTED_10").toUpperCase());

            String title = config.getString("bossbar.title", "&aXP: &b%xp%&7/&b%nextxp%");
            title = title.replace("%xp%", String.valueOf(progress.currentXp))
                    .replace("%nextxp%", String.valueOf(progress.xpNeeded))
                    .replace("%path%", path.id)
                    .replace("%path_display%", path.displayName);

            bar = Bukkit.createBossBar(title, color, style);
            bar.setProgress((double) progress.currentXp / progress.xpNeeded);
            bar.addPlayer(player);
            bar.setVisible(true);

            bars.put(key, bar);
        } else {
            bar.setProgress((double) progress.currentXp / progress.xpNeeded);
            String title = config.getString("bossbar.title", "&aXP: &b%xp%&7/&b%nextxp%");
            title = title.replace("%xp%", String.valueOf(progress.currentXp))
                    .replace("%nextxp%", String.valueOf(progress.xpNeeded))
                    .replace("%path%", path.id)
                    .replace("%path_display%", path.displayName);
            bar.setTitle(title);
            bar.setVisible(true);
        }

        // Reinicia o tempo de exibição
        if (timers.containsKey(key)) {
            Bukkit.getScheduler().cancelTask(timers.get(key));
        }

        int duration = config.getInt("bossbar.duration", 60);
        BossBar finalBar = bar;
        int taskId = Bukkit.getScheduler().runTaskLater(AllayaSkills.getInstance(), () -> {
            finalBar.setVisible(false);
            bars.remove(key);
            timers.remove(key);
        }, duration).getTaskId();

        timers.put(key, taskId);
    }
}
