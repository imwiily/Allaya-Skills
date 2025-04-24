package com.allaya.skills.listeners;

import com.allaya.skills.skills.SkillManager;
import com.allaya.skills.skills.SkillStorage;
import com.allaya.skills.skills.PlayerSkillData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class SkillDataListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        PlayerSkillData data = SkillStorage.load(uuid);
        SkillManager.set(uuid, data);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        PlayerSkillData data = SkillManager.get(uuid);
        SkillStorage.save(uuid, data);
    }
}
