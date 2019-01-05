package com.vhbob.forensics.commands;

import com.vhbob.forensics.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class Bleed implements CommandExecutor, Listener {

    Main plugin = Main.PLUGIN;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("Bleed")) {
            if (args.length == 0) {
                if (commandSender instanceof Player) {
                    if (!plugin.getBleeding().contains((Player) commandSender)) {
                        ArrayList<Player> bleeding = plugin.getBleeding();
                        bleeding.add((Player) commandSender);
                        commandSender.sendMessage(ChatColor.GREEN + "You are now bleeding");
                    } else {
                        if (Main.PLUGIN.forced.contains(((Player) commandSender).getUniqueId())) {
                            commandSender.sendMessage(ChatColor.RED + "You cannot stop the bleeding!");
                            return false;
                        }
                        plugin.getBleeding().remove((Player) commandSender);
                        commandSender.sendMessage(ChatColor.RED + "You are no longer bleeding");
                    }
                } else {
                    commandSender.sendMessage(ChatColor.RED + "You are not a player!");
                }
            } else {
                if (commandSender.hasPermission("forensics.bleedothers")) {
                    if (args.length == 1) {
                        if (Bukkit.getPlayer(args[0]) == null) {
                            commandSender.sendMessage(ChatColor.RED + "Player not found");
                            return false;
                        }
                        if (plugin.getBleeding().contains(Bukkit.getPlayer(args[0]))) {
                            plugin.getBleeding().remove(Bukkit.getPlayer(args[0]));
                            Main.PLUGIN.forced.remove( Bukkit.getPlayer(args[0]).getUniqueId());
                            commandSender.sendMessage(ChatColor.RED + Bukkit.getPlayer(args[0]).getName() + " is no longer bleeding");
                            Bukkit.getPlayer(args[0]).sendMessage(ChatColor.RED + "You are no longer bleeding!");
                        } else {
                            plugin.getBleeding().add(Bukkit.getPlayer(args[0]));
                            Main.PLUGIN.forced.add( Bukkit.getPlayer(args[0]).getUniqueId());
                            commandSender.sendMessage(ChatColor.GREEN + Bukkit.getPlayer(args[0]).getName()  + " is now bleeding");
                            Bukkit.getPlayer(args[0]).sendMessage(ChatColor.RED + "You are now bleeding!");
                        }
                    } else if (args.length >= 2) {
                        if (Bukkit.getPlayer(args[0]) == null) {
                            commandSender.sendMessage(ChatColor.RED + "Player not found");
                            return false;
                        }
                        if (!isNumeric(args[1])) {
                            commandSender.sendMessage(ChatColor.RED + "Invalid time!");
                            return false;
                        }
                        if (Main.PLUGIN.getBleeding().contains(Bukkit.getPlayer(args[0]))) {
                            commandSender.sendMessage(ChatColor.RED + Bukkit.getPlayer(args[0]).getName()  + " is already bleeding!");
                            return false;
                        }
                        plugin.getBleeding().add(Bukkit.getPlayer(args[0]));
                        Main.PLUGIN.forced.add( Bukkit.getPlayer(args[0]).getUniqueId());
                        commandSender.sendMessage(ChatColor.GREEN + Bukkit.getPlayer(args[0]).getName()  + " is now bleeding for " + args[1] + " seconds");
                        Bukkit.getPlayer(args[0]).sendMessage(ChatColor.RED + "You are now bleeding!");
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.PLUGIN, new Runnable() {
                            @Override
                            public void run() {
                                if (plugin.getBleeding().contains(Bukkit.getPlayer(args[0]))) {
                                    plugin.getBleeding().remove(Bukkit.getPlayer(args[0]));
                                    Main.PLUGIN.forced.remove(Bukkit.getPlayer(args[0]).getUniqueId());
                                    Bukkit.getPlayer(args[0]).sendMessage(ChatColor.RED + "You are no longer bleeding!");
                                }
                            }
                        }, Integer.valueOf(args[1]) * 20);
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "Usage: /bleed (player) (seconds)");
                        commandSender.sendMessage(ChatColor.RED + "Player and seconds are optional");
                    }
                } else
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission!");
            }
        }
        return false;
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}
