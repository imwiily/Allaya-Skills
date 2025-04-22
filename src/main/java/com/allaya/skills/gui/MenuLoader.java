package com.allaya.skills.gui;

import com.allaya.skills.AllayaSkills;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MenuLoader {

    private static final Map<String, YamlConfiguration> loadedMenus = new HashMap<>();

    public static void loadMenus() {
        File folder = new File(AllayaSkills.getInstance().getDataFolder(), "menus");
        if (!folder.exists()) folder.mkdirs();

        for (File file : folder.listFiles((dir, name) -> name.endsWith(".yml"))) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            String name = file.getName().replace(".yml", "").toLowerCase();
            loadedMenus.put(name, config);
        }
    }

    public static YamlConfiguration getMenu(String name) {
        return loadedMenus.get(name.toLowerCase());
    }
}
