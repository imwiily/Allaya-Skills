package com.allaya.skills;

import com.allaya.skills.commands.SkillsCommand;
import com.allaya.skills.gui.MenuLoader;
import com.allaya.skills.listeners.*;
import com.allaya.skills.database.DatabaseManager;
import com.allaya.skills.paths.PathLoader;
import com.allaya.skills.placeholders.SkillsPlaceholder;
import com.allaya.skills.effects.EffectRegistry;
import com.allaya.skills.effects.PotionEffectExecutor;
import com.allaya.skills.skills.SkillLoader;
import com.allaya.skills.skills.SkillStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.allaya.skills.utils.MessageUtils;

import static com.allaya.skills.paths.PathLoader.loadedPaths;

public class AllayaSkills extends JavaPlugin {

    private static AllayaSkills instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveResource("messages.yml", false);
        saveResource("skills/maos_rapidas.yml", false);
        saveResource("skills/super_toque.yml", false);
        saveResource("skills/picareta_de_ferro.yml", false);
        saveResource("menus/stats.yml", false);
        saveResource("menus/mine-skills.yml", false);
        saveResource("menus/main.yml", false);
        saveResource("paths/mineracao.yml", false);


        //Comandos
        getCommand("skills").setExecutor(new SkillsCommand());

        // Banco de dados SQLite
        DatabaseManager.setupDatabase();

        // Listeners
        getServer().getPluginManager().registerEvents(new MiningListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new SkillDataListener(), this);

        // Carrega de dados
        MenuLoader.loadMenus();
        SkillLoader.loadSkills();
        PathLoader.loadPaths();
        Bukkit.getLogger().info("[AllayaSkills] Caminhos carregados: " + loadedPaths.keySet());

        MessageUtils.loadMessages();

        // Garante que tabela seja criada
        SkillStorage.createTable();

        //Placeholders
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new SkillsPlaceholder().register();
        }



        EffectRegistry.register("POTION_EFFECT", new PotionEffectExecutor());

        getLogger().info("Allaya-Skills habilitado com sucesso.");
    }

    @Override
    public void onDisable() {

        DatabaseManager.shutdown();


        getLogger().info("Allaya-Skills desabilitado.");

    }

    public static AllayaSkills getInstance() {
        return instance;
    }
}
