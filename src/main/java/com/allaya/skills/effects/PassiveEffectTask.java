package com.allaya.skills.effects;

import com.allaya.skills.skills.SkillEffect;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PassiveEffectTask extends BukkitRunnable {

    private final Player player;
    private final SkillEffect effect;
    private final String skillId;

    public PassiveEffectTask(Player player, String skillId, SkillEffect effect) {
        this.player = player;
        this.skillId = skillId;
        this.effect = effect;
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            cancel();
            return;
        }

        if (CooldownManager.isReady(player.getUniqueId(), skillId)) {
            EffectExecutor executor = EffectRegistry.getExecutor(effect.effect);
            if (executor != null) {
                executor.apply(player, effect);
                CooldownManager.start(player.getUniqueId(), skillId, effect.duration / 20); // converter ticks para segundos
            }
        }
    }
}
