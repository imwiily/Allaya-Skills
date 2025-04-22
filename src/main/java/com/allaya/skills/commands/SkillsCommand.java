package com.allaya.skills.commands;

import com.allaya.skills.AllayaSkills;
import com.allaya.skills.gui.MenuGUI;
import com.allaya.skills.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkillsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("allayaskills.admin")) {
                MessageUtils.send(sender, "no-permission");
                return true;
            }

            AllayaSkills.getInstance().reloadConfig();
            MessageUtils.loadMessages();
            MessageUtils.send(sender, "reload-success");
            return true;
        }

        if (sender instanceof Player player) {
            MenuGUI.open(player, "skills");
        } else {
            sender.sendMessage("Este comando só pode ser usado por jogadores.");
        }

        return true;
    }
}
