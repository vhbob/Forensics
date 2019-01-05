package com.vhbob.forensics.commands;

import com.vhbob.forensics.Main;
import com.vhbob.forensics.items.Items;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopRecorder implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("stoprecorder")) {
            if (commandSender instanceof Player) {
                Player p = (Player) commandSender;
                if (Main.PLUGIN.playerToRecorder.containsKey(p.getUniqueId())) {
                    p.getInventory().addItem(Items.getRecord(Main.PLUGIN.playerToRecorder.get(p.getUniqueId())));
                    p.sendMessage(ChatColor.GREEN + "Your recording finished!");
                    Main.PLUGIN.playerToRecorder.remove(p.getUniqueId());
                } else {
                    p.sendMessage(ChatColor.RED + "You are not recording!");
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "You are not a player!");
            }
        }
        return false;
    }
}
