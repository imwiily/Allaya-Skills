package com.allaya.skills.skills;

import com.allaya.skills.AllayaSkills;
import com.allaya.skills.paths.PlayerPathProgress;
import com.allaya.skills.paths.PathManager;
import com.allaya.skills.effects.PassiveEffectTask;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkillManager {

    private static final Map<UUID, PlayerSkillData> playerSkills = new HashMap<>();

    public static PlayerSkillData get(UUID uuid) {
        return playerSkills.computeIfAbsent(uuid, u -> new PlayerSkillData());
    }

    public static boolean tryUnlock(UUID uuid, SkillData skill, int playerLevel, int availablePoints) {
        PlayerSkillData data = get(uuid);

        if (data.isUnlocked(skill.id)) return false;
        if (playerLevel < skill.unlockLevel) return false;
        if (availablePoints < skill.skillPointsRequired) return false;

        for (String required : skill.requires) {
            if (!data.isUnlocked(required)) return false;
        }

        data.unlock(skill.id);
        return true;
    }

    public static void attemptUnlock(Player player, String skillId) {
        SkillData skill = SkillLoader.getById(skillId);
        if (skill == null) {
            player.sendMessage("§cSkill não encontrada.");
            return;
        }

        PlayerPathProgress progress = PathManager.getOrCreateProgress(player.getUniqueId(), skill.path);
        if (progress == null) {
            player.sendMessage("§cCaminho associado à skill não encontrado.");
            return;
        }

        boolean success = tryUnlock(
                player.getUniqueId(),
                skill,
                progress.level,
                progress.pointsEarned - progress.pointsUsed
        );

        if (success) {
            PathManager.usePoint(player.getUniqueId(), skill.path);
            player.sendMessage("§aSkill §b" + skill.name + " §adesbloqueada com sucesso!");

            // ✅ Inicia task reativa apenas se for PASSIVE_TICK
            for (SkillEffect effect : skill.effects) {
                if ("PASSIVE_TICK".equalsIgnoreCase(effect.trigger)) {
                    new PassiveEffectTask(player, skill.id, effect)
                            .runTaskTimer(AllayaSkills.getInstance(), 0L, 20L); // 1 segundo
                }
            }
        } else {
            player.sendMessage("§cVocê não atende aos requisitos para desbloquear essa skill.");
        }
    }
    public static void set(UUID uuid, PlayerSkillData data) {
        playerSkills.put(uuid, data);
    }

}
