package com.allaya.skills.listeners;

import com.allaya.skills.gui.MenuGUI;
import com.allaya.skills.gui.MenuLoader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (e.getView().getTitle() == null || !e.getView().getTitle().contains("Skills")) return;

        e.setCancelled(true);
        if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta()) return;

        YamlConfiguration config = MenuLoader.getMenu("skills");
        if (config == null || !config.contains("items")) return;

        for (String key : config.getConfigurationSection("items").getKeys(false)) {
            int slot = config.getInt("items." + key + ".slot");
            if (e.getSlot() == slot && config.contains("items." + key + ".open-menu")) {
                String target = config.getString("items." + key + ".open-menu");
                YamlConfiguration targetConfig = MenuLoader.getMenu(target);
                if (targetConfig != null) {
                    MenuGUI.open(player, target);
                } else {
                    player.sendMessage("§cMenu '" + target + "' não encontrado.");
                }
            }
        }
    }
}
