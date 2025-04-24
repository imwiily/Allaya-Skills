package com.allaya.skills.effects;

import com.allaya.skills.skills.SkillEffect;
import org.bukkit.entity.Player;

public interface EffectExecutor {
    void apply(Player player, SkillEffect effect);
}
