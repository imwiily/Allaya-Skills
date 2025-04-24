package com.allaya.skills.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarManager {

    private static final Map<String, BossBar> bars = new HashMap<>();
    private static final Map<String, Integer> timers = new HashMap<>();

    public static void updateBar(Player player, String pathId, int currentXp, int neededXp) {
        String key = player.getUniqueId() + ":" + pathId;

        double progress = Math.min(1.0, (double) currentXp / neededXp);
        String title = "§bXP: §e" + currentXp + "§7/§a" + neededXp;

        BossBar bar = bars.get(key);
        if (bar == null) {
            bar = Bukkit.createBossBar(title, BarColor.GREEN, BarStyle.SEGMENTED_10);
            bar.addPlayer(player);
            bars.put(key, bar);
        }

        bar.setTitle(title);
        bar.setProgress(progress);
        bar.setVisible(true);

        if (timers.containsKey(key)) {
            Bukkit.getScheduler().cancelTask(timers.get(key));
        }

        String finalKey = key;
        BossBar finalBar = bar;
        int taskId = Bukkit.getScheduler().runTaskLater(
                Bukkit.getPluginManager().getPlugin("Allaya-Skills"),
                () -> {
                    finalBar.setVisible(false);
                    bars.remove(finalKey);
                    timers.remove(finalKey);
                }, 60L // 3 segundos
        ).getTaskId();


        timers.put(key, taskId);
    }
}
