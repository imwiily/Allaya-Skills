package com.allaya.skills.listeners;

import com.allaya.skills.gui.MenuGUI;
import com.allaya.skills.gui.MenuLoader;
import com.allaya.skills.skills.SkillManager;
import com.allaya.skills.utils.MenuUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;

        String title = e.getView().getTitle();

        if (!MenuUtils.isOurMenu(player.getUniqueId(), title)) {
            return;
        }

        if (e.getClickedInventory() == null || !e.getClickedInventory().equals(e.getView().getTopInventory())) {
            return;
        }

        e.setCancelled(true);

        if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta()) {
            return;
        }

        String menuId = MenuUtils.getCurrentMenu(player.getUniqueId());

        YamlConfiguration config = MenuLoader.getMenu(menuId);
        if (config == null || !config.contains("items")) {
            return;
        }

        for (String key : config.getConfigurationSection("items").getKeys(false)) {
            int slot = config.getInt("items." + key + ".slot");
            if (e.getSlot() != slot) continue;


            String type = config.getString("items." + key + ".type", "static").toLowerCase();

            switch (type) {
                case "skill" -> {
                    if (config.contains("items." + key + ".locked.unlock-skill")) {
                        String skillId = config.getString("items." + key + ".locked.unlock-skill");
                        Bukkit.getLogger().info("[DEBUG] Tentando desbloquear skill: " + skillId);
                        SkillManager.attemptUnlock(player, skillId);
                        MenuGUI.open(player, menuId);
                    }
                }
                case "static" -> {
                    if (config.contains("items." + key + ".open-menu")) {
                        String target = config.getString("items." + key + ".open-menu");
                        Bukkit.getLogger().info("[DEBUG] Abrindo menu: " + target);
                        MenuGUI.open(player, target);
                    } else if (config.getBoolean("items." + key + ".back", false)) {
                        Bukkit.getLogger().info("[DEBUG] Voltando ao menu principal");
                        MenuGUI.open(player, "main");
                    }
                }
            }
            break;
        }
    }


    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;

        String title = e.getView().getTitle();
        if (MenuUtils.isOurMenu(player.getUniqueId(), title)) {
            e.setCancelled(true); // Prevê movimentação de itens por drag
        }
    }
}
