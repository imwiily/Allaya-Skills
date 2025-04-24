package com.allaya.skills.listeners;

import com.allaya.skills.paths.PathData;
import com.allaya.skills.paths.PathManager;
import com.allaya.skills.paths.PathLoader;
import com.allaya.skills.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.Map;

public class MiningListener implements Listener {

    // Tabela de XP por bloco (carregada do config.yml)
    private static final Map<Material, Integer> XP_PER_BLOCK = new HashMap<>();

    public static void loadXPMap(ConfigurationSection section) {
        XP_PER_BLOCK.clear();
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            try {
                Material mat = Material.matchMaterial(key.toUpperCase());
                if (mat != null) {
                    XP_PER_BLOCK.put(mat, section.getInt(key));
                } else {
                    Bukkit.getLogger().warning("[AllayaSkills] Bloco inválido no config.yml: " + key);
                }
            } catch (Exception e) {
                Bukkit.getLogger().warning("[AllayaSkills] Erro ao carregar bloco: " + key);
            }
        }
    }

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material block = event.getBlock().getType();

        int xp = XP_PER_BLOCK.getOrDefault(block, 0);
        if (xp > 0) {
            PathManager.addXp(player, "mineracao", xp);
            player.sendMessage(MessageUtils.format("xp-gain",
                    "%skill%", "Mineração",
                    "%xp%", String.valueOf(xp)
            ));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Material block = e.getBlock().getType();
        PathData path = PathLoader.getPath("mineracao");

        if (path == null || !"BLOCK_BREAK".equalsIgnoreCase(path.xpSource)) return;

        int xp = path.getXpForBlock(block.name());
        if (xp > 0) {
            PathManager.addXp(player, "mineracao", xp);
        }
    }

}
