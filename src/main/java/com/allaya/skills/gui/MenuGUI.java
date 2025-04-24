package com.allaya.skills.gui;

import com.allaya.skills.skills.SkillManager;
import com.allaya.skills.managers.SkillXPManager;
import com.allaya.skills.skills.SkillData;
import com.allaya.skills.skills.SkillLoader;
import com.allaya.skills.utils.Base64HeadUtils;
import com.allaya.skills.utils.MenuUtils;
import com.allaya.skills.paths.PathData;
import com.allaya.skills.paths.PathLoader;
import com.allaya.skills.paths.PathManager;
import com.allaya.skills.paths.PlayerPathProgress;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MenuGUI {

    public static void open(Player player, String menuId) {
        YamlConfiguration config = MenuLoader.getMenu(menuId);
        if (config == null) {
            player.sendMessage("§cMenu '" + menuId + "' não encontrado.");
            return;
        }

        String title = config.getString("title", menuId);
        int rows = config.getInt("rows", 3);
        Inventory gui = Bukkit.createInventory(null, InventoryType.CHEST, title);
        if (gui.getSize() != rows * 9) {
            gui = Bukkit.createInventory(null, rows * 9, title);
        }

        UUID uuid = player.getUniqueId();

        if (config.contains("items")) {
            for (String key : config.getConfigurationSection("items").getKeys(false)) {
                String path = "items." + key;
                int slot = config.getInt(path + ".slot");
                String type = config.getString(path + ".type", "static");

                Material material = Material.STONE;
                String skullMeta = null;
                String name = key;
                List<String> lore = new ArrayList<>();

                if ("skill".equalsIgnoreCase(type)) {
                    boolean unlocked = SkillManager.get(uuid).isUnlocked(config.getString(path + ".locked.unlock-skill"));
                    String section = unlocked ? ".unlocked" : ".locked";

                    material = Material.matchMaterial(config.getString(path + section + ".material", "STONE"));
                    skullMeta = config.getString(path + section + ".skull-meta");
                    name = config.getString(path + section + ".name", key);
                    lore = config.getStringList(path + section + ".lore");

                } else { // static
                    material = Material.matchMaterial(config.getString(path + ".material", "STONE"));
                    name = config.getString(path + ".name", key);
                    lore = config.getStringList(path + ".lore");
                }

                if (material == null) material = Material.STONE;

                ItemStack item = (material == Material.PLAYER_HEAD && skullMeta != null)
                        ? Base64HeadUtils.getHead(skullMeta)
                        : new ItemStack(material);

                ItemMeta meta = item.getItemMeta();

                String parsedName = replacePlaceholders(player, name, key);
                meta.setDisplayName(color(parsedName));

                List<String> parsedLore = new ArrayList<>();
                for (String line : lore) {
                    parsedLore.add(color(replacePlaceholders(player, line, key)));
                }

                meta.setLore(parsedLore);
                item.setItemMeta(meta);
                gui.setItem(slot, item);
            }
        }

        MenuUtils.setCurrentMenu(uuid, menuId);
        player.openInventory(gui);
    }

    private static String color(String msg) {
        return msg == null ? "" : msg.replace("&", "§");
    }

    private static String replacePlaceholders(Player player, String text, String skillOrPathId) {
        UUID uuid = player.getUniqueId();
        int level = SkillXPManager.getLevel(uuid, skillOrPathId);
        int xp = SkillXPManager.getXP(uuid, skillOrPathId);
        int nextxp = level * 100;

        text = text.replace("%level%", String.valueOf(level))
                .replace("%xp%", String.valueOf(xp))
                .replace("%nextxp%", String.valueOf(nextxp));

        for (String path : PathLoader.getAllPaths().keySet()) {
            PlayerPathProgress progress = PathManager.getProgress(uuid, path);
            if (progress != null) {
                text = text.replace("%xp_" + path + "%", String.valueOf(progress.currentXp));
                text = text.replace("%nextxp_" + path + "%", String.valueOf(progress.xpNeeded));
                text = text.replace("%points_" + path + "%", String.valueOf(progress.getAvailablePoints()));
            }
        }

        return text;
    }
}
