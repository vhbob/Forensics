package com.vhbob.forensics.commands;

import com.vhbob.forensics.Main;
import com.vhbob.forensics.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Forensics implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("forensics")) {
            if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
                Player p = Bukkit.getPlayer(args[2]);
                if (p != null) {
                    String itemName = args[1];
                    if (itemName.equalsIgnoreCase("Scanner")) {
                        if (!commandSender.hasPermission("forensics.givescanner"))  {
                            commandSender.sendMessage(ChatColor.RED + "You do not have permission!");
                            return false;
                        }
                        ItemStack scanner = Items.getScanner();
                        p.getInventory().addItem(scanner);
                        commandSender.sendMessage(ChatColor.GREEN + "Scanner given to: " + p.getName());
                    } else if (itemName.equalsIgnoreCase("Scrambler")) {
                        if (!commandSender.hasPermission("forensics.givescrambler"))  {
                            commandSender.sendMessage(ChatColor.RED + "You do not have permission!");
                            return false;
                        }
                        ItemStack scrambler = Items.getScrambler();
                        p.getInventory().addItem(scrambler);
                        commandSender.sendMessage(ChatColor.GREEN + "Scrambler given to: " + p.getName());
                    } else if (itemName.equalsIgnoreCase("Recorder")) {
                        if (!commandSender.hasPermission("forensics.giverecorder"))  {
                            commandSender.sendMessage(ChatColor.RED + "You do not have permission!");
                            return false;
                        }
                        ItemStack recorder = Items.getRecorder();
                        p.getInventory().addItem(recorder);
                        commandSender.sendMessage(ChatColor.GREEN + "Recorder given to: " + p.getName());
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "Invalid Item, Use: Scanner, Scrambler, Recorder");
                    }
                } else {
                    commandSender.sendMessage(ChatColor.RED + "Player " + args[2] + " not found!");
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (commandSender.hasPermission("forensics.reload")) {
                    Main.PLUGIN.reloadConfig();
                    commandSender.sendMessage(ChatColor.GREEN + "Config reloaded!");
                } else {
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission!");
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Usage: /Forensics give (Scanner, Scrambler, Recorder) (Player)");
                if (commandSender.hasPermission("forensics.reload")) {
                    commandSender.sendMessage(ChatColor.RED + "OR /Forensics reload");
                }
            }
        }
        return false;
    }
}
