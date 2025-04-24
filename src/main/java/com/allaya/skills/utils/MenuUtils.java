package com.allaya.skills.utils;

import com.allaya.skills.gui.MenuGUI;
import com.allaya.skills.gui.MenuLoader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuUtils {

    private static final Map<UUID, String> openMenus = new HashMap<>();
    private static final Map<UUID, String> lastMenu = new HashMap<>();

    public static void setCurrentMenu(UUID uuid, String menuId) {
        openMenus.put(uuid, menuId);
    }

    public static void clearCurrentMenu(UUID uuid) {
        openMenus.remove(uuid);
    }

    public static boolean isOurMenu(UUID uuid, String currentTitle) {
        String expectedId = openMenus.get(uuid);
        if (expectedId == null) return false;

        YamlConfiguration menu = MenuLoader.getMenu(expectedId);
        if (menu == null) return false;

        String expectedTitle = menu.getString("title", expectedId);
        return expectedTitle != null && currentTitle != null && currentTitle.equalsIgnoreCase(expectedTitle);
    }

    public static String getCurrentMenu(UUID uuid) {
        return openMenus.get(uuid);
    }

    public static void execute(Player player, YamlConfiguration config, String key) {
        String path = "items." + key;

        if (config.contains(path + ".run-command")) {
            String cmd = config.getString(path + ".run-command");
            if (cmd != null && !cmd.isEmpty()) {
                player.performCommand(cmd);
            }
        }

        if (config.getBoolean(path + ".close", false)) {
            player.closeInventory();
        }

        if (config.contains(path + ".open-menu")) {
            String target = config.getString(path + ".open-menu");
            if (target != null && !target.isEmpty()) {
                lastMenu.put(player.getUniqueId(), getCurrentMenu(player.getUniqueId()));
                MenuGUI.open(player, target);
            }
        }

        if (config.getBoolean(path + ".back", false)) {
            String previous = lastMenu.get(player.getUniqueId());
            if (previous != null) {
                MenuGUI.open(player, previous);
            } else {
                player.sendMessage("§cNenhum menu anterior encontrado.");
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) {
            clearCurrentMenu(player.getUniqueId());
        }
    }
}
