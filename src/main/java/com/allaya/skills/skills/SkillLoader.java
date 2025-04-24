package com.allaya.skills.skills;

import com.allaya.skills.AllayaSkills;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class SkillLoader {

    private static final Map<String, SkillData> loadedSkills = new HashMap<>();

    public static void loadSkills() {
        File folder = new File(AllayaSkills.getInstance().getDataFolder(), "skills");
        if (!folder.exists()) folder.mkdirs();

        loadedSkills.clear();

        for (File file : folder.listFiles((dir, name) -> name.endsWith(".yml"))) {
            String id = file.getName().replace(".yml", "").toLowerCase();
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            SkillData skill = new SkillData();
            skill.id = id;
            skill.name = config.getString("name", id);
            skill.description = config.getString("description", "");
            skill.path = config.getString("path", "default");
            skill.unlockLevel = config.getInt("unlock_level", 0);
            skill.skillPointsRequired = config.getInt("skill-points-required", 1);
            skill.requires = config.getStringList("requires");
            skill.regionAllowed = config.getStringList("region-allowed");
            skill.cooldown = config.getInt("cooldown", 0);

            List<SkillEffect> effects = new ArrayList<>();
            for (Map<?, ?> map : config.getMapList("effects")) {
                SkillEffect effect = new SkillEffect();
                effect.trigger = String.valueOf(map.get("trigger"));
                effect.effect = String.valueOf(map.get("effect"));
                effect.type = String.valueOf(map.get("type"));
                effect.amplifier = (int) map.get("amplifier");
                effect.duration = (int) map.get("duration");
                effects.add(effect);
            }
            skill.effects = effects;

            loadedSkills.put(id, skill);
        }
    }

    public static Collection<SkillData> getAllSkills() {
        return loadedSkills.values();
    }

    public static SkillData getById(String id) {
        return loadedSkills.get(id.toLowerCase());
    }

    public static List<SkillData> getByPath(String path) {
        List<SkillData> result = new ArrayList<>();
        for (SkillData data : loadedSkills.values()) {
            if (data.path.equalsIgnoreCase(path)) {
                result.add(data);
            }
        }
        return result;
    }
}
