package com.vhbob.forensics.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if ((command.getName().equalsIgnoreCase("reload"))) {
            if (commandSender.hasPermission(""));
        }
        return false;
    }
}
