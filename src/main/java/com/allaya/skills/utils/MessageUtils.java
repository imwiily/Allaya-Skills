package com.allaya.skills.utils;

import com.allaya.skills.AllayaSkills;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageUtils {

    private static FileConfiguration messages;

    public static void loadMessages() {
        File file = new File(AllayaSkills.getInstance().getDataFolder(), "messages.yml");
        messages = YamlConfiguration.loadConfiguration(file);
    }

    public static String getMessage(String key) {
        if (!messages.contains(key)) return ChatColor.RED + "Mensagem não encontrada: " + key;
        String msg = messages.getString(key);
        String prefix = messages.getString("prefix", "");
        return ChatColor.translateAlternateColorCodes('&', msg.replace("%prefix%", prefix));
    }

    public static void send(CommandSender sender, String key) {
        sender.sendMessage(getMessage(key));
    }
    public static String format(String key, String... replacements) {
        String msg = getMessage(key);
        for (int i = 0; i < replacements.length - 1; i += 2) {
            msg = msg.replace(replacements[i], replacements[i + 1]);
        }
        return msg;
    }

}
