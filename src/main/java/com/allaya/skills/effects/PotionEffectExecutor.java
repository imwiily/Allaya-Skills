package com.allaya.skills.effects;

import com.allaya.skills.skills.SkillEffect;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectExecutor implements EffectExecutor {

    @Override
    public void apply(Player player, SkillEffect effect) {
        PotionEffectType type = PotionEffectType.getByName(effect.type.toUpperCase());
        if (type == null) return;

        // Apenas aplica se não tiver ou estiver expirando
        PotionEffect current = player.getPotionEffect(type);
        if (current == null || current.getDuration() < effect.duration / 2) {
            player.addPotionEffect(new PotionEffect(type, effect.duration, effect.amplifier, true, false));
        }
    }
}
