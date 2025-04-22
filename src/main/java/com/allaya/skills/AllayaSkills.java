package com.allaya.skills;

import com.allaya.skills.commands.SkillsCommand;
import com.allaya.skills.gui.MenuLoader;
import com.allaya.skills.listeners.*;
import com.allaya.skills.managers.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.allaya.skills.utils.MessageUtils;

public class AllayaSkills extends JavaPlugin {

    private static AllayaSkills instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveResource("messages.yml", false);

        //Comandos
        getCommand("skills").setExecutor(new SkillsCommand());

        // Banco de dados SQLite
        DatabaseManager.setupDatabase();

        // Listeners
        getServer().getPluginManager().registerEvents(new MiningListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);

        // Carrega de dados
        MenuLoader.loadMenus();
        MessageUtils.loadMessages();

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
