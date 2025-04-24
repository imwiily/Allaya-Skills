package com.allaya.skills.paths;

import com.allaya.skills.AllayaSkills;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PathLoader {

    public static final Map<String, PathData> loadedPaths = new HashMap<>();

    public static void loadPaths() {
        File folder = new File(AllayaSkills.getInstance().getDataFolder(), "paths");
        if (!folder.exists()) folder.mkdirs();

        loadedPaths.clear();

        for (File file : folder.listFiles((dir, name) -> name.endsWith(".yml"))) {
            String id = file.getName().replace(".yml", "").toLowerCase();
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            Map<String, Integer> blockXp = new HashMap<>();
            if (config.contains("block-xp")) {
                for (String block : config.getConfigurationSection("block-xp").getKeys(false)) {
                    blockXp.put(block.toUpperCase(), config.getInt("block-xp." + block));
                }
            }

            PathData path = new PathData(
                    id,
                    config.getString("display-name", id),
                    config.getString("xp-source", "BLOCK_BREAK"),
                    config.getInt("xp-per-event", 10),
                    config.getInt("starting-xp", 100),
                    config.getInt("xp-increase-per-level", 50),
                    config.getInt("points-per-level", 1),
                    blockXp
            );

            loadedPaths.put(id, path);
        }

        AllayaSkills.getInstance().getLogger().info("[AllayaSkills] Caminhos carregados: " + loadedPaths.keySet());
    }

    public static PathData getPath(String id) {
        return loadedPaths.get(id.toLowerCase());
    }

    public static Map<String, PathData> getAllPaths() {
        return loadedPaths;
    }
}
