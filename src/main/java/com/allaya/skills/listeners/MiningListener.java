package com.allaya.skills.listeners;

import com.allaya.skills.managers.SkillManager;
import com.allaya.skills.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.EnumSet;
import java.util.Set;

public class MiningListener implements Listener {

    private static final Set<Material> MINABLE_BLOCKS = EnumSet.of(
            Material.STONE, Material.COAL_ORE, Material.IRON_ORE,
            Material.GOLD_ORE, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE,
            Material.NETHER_QUARTZ_ORE, Material.COPPER_ORE, Material.REDSTONE_ORE
    );

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material block = event.getBlock().getType();

        if (MINABLE_BLOCKS.contains(block)) {
            int currentXP = SkillManager.getXP(player.getUniqueId(), "mining");
            SkillManager.addXP(player.getUniqueId(), "mining", 10);
            player.sendMessage(MessageUtils.format("xp-gain",
                    "%skill%", "Mining",
                    "%xp%", String.valueOf(10)
            ));

        }
    }
}
