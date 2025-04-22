package com.allaya.skills.gui;

import com.allaya.skills.managers.SkillManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MenuGUI {

    public static void open(Player player, String menuId) {
        YamlConfiguration config = MenuLoader.getMenu(menuId);
        if (config == null) {
            player.sendMessage("§cMenu '" + menuId + "' não encontrado.");
            return;
        }

        String title = config.getString("title", menuId);
        int rows = config.getInt("rows", 3);
        Inventory gui = Bukkit.createInventory(null, rows * 9, title);

        UUID uuid = player.getUniqueId();

        if (config.contains("items")) {
            for (String key : config.getConfigurationSection("items").getKeys(false)) {
                String path = "items." + key;
                int slot = config.getInt(path + ".slot");
                Material material = Material.matchMaterial(config.getString(path + ".icon", "STONE"));
                if (material == null) continue;

                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();

                int level = SkillManager.getLevel(uuid, key);
                int xp = SkillManager.getXP(uuid, key);
                int nextxp = level * 100;

                meta.setDisplayName(color(config.getString(path + ".name")
                        .replace("%level%", String.valueOf(level))));

                List<String> lore = config.getStringList(path + ".lore").stream()
                        .map(line -> color(line
                                .replace("%level%", String.valueOf(level))
                                .replace("%xp%", String.valueOf(xp))
                                .replace("%nextxp%", String.valueOf(nextxp))))
                        .collect(Collectors.toList());

                meta.setLore(lore);
                item.setItemMeta(meta);
                gui.setItem(slot, item);
            }
        }

        player.openInventory(gui);
    }

    private static String color(String msg) {
        return msg == null ? "" : msg.replace("&", "§");
    }
}
