package com.vhbob.forensics;

import com.vhbob.forensics.commands.Bleed;
import com.vhbob.forensics.commands.Forensics;
import com.vhbob.forensics.commands.StopRecorder;
import com.vhbob.forensics.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {

    public ArrayList<Player> bleeding;
    public static Main PLUGIN;
    public HashMap<Location, ArrayList<String>> blood;
    public HashMap<UUID, ArrayList<String>> aliases;
    public ArrayList<String> hidden;
    public HashMap<UUID, ArrayList<String>> recorded;
    public HashMap<UUID, UUID> playerToRecorder;
    public HashMap<Location, ArrayList<String>> interactable;
    public ArrayList<UUID> forced;

    @Override
    public void onEnable() {
        blood = new HashMap<>();
        PLUGIN = this;
        bleeding = new ArrayList<>();
        aliases = new HashMap<>();
        hidden = new ArrayList<>();
        recorded = new HashMap<>();
        playerToRecorder = new HashMap<>();
        interactable = new HashMap<>();
        forced = new ArrayList<>();
        startBleeding();
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new NickChange(), this);
        Bukkit.getPluginManager().registerEvents(new Scanner(), this);
        Bukkit.getPluginManager().registerEvents(new Recorder(), this);
        Bukkit.getPluginManager().registerEvents(new Scrambler(), this);
        Bukkit.getPluginManager().registerEvents(new Interact(), this);
        getCommand("StopRecorder").setExecutor(new StopRecorder());
        getCommand("forensics").setExecutor(new Forensics());
        getCommand("bleed").setExecutor(new Bleed());
        Bukkit.getConsoleSender().sendMessage("Forensics has been " + ChatColor.GREEN + " enabled!" );
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Forensics has been " + ChatColor.RED + " disabled!" );
    }

    public ArrayList<Player> getBleeding() {
        return this.bleeding;
    }

    public void startBleeding() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PLUGIN, new Runnable() {
            @Override
            public void run() {
                for (Player p : bleeding) {
                    if (p != null && p.getLocation().getBlock().getRelative(0,-1,0).getType() != null &&
                            !p.getLocation().getBlock().getRelative(0, -1, 0).getType().toString().toUpperCase().contains("FENCE") &&
                            p.getLocation().getBlock().getRelative(0, -1, 0).getType().isSolid()
                            && p.getLocation().getBlock().getType() == Material.AIR) {
                        p.getLocation().getBlock().setType(Material.REDSTONE_WIRE);
                        ArrayList<String> players;
                        if (blood.get(p.getLocation().getBlock().getLocation()) == null) {
                            players = new ArrayList<>();
                        } else {
                            players = blood.get(p.getLocation().getBlock().getLocation());
                        }
                        boolean duplicate = false;
                        if (aliases.containsKey(p.getUniqueId())) {
                            for (String alias : aliases.get(p.getUniqueId())) {
                                for (String name : players) {
                                    if (ChatColor.stripColor(name).equalsIgnoreCase(alias)) {
                                        duplicate = true;
                                    }
                                }
                            }
                        }
                        if (!duplicate && !players.contains(p.getDisplayName()) && !p.hasPermission("forensics.exempt")) {
                            if ( Main.PLUGIN.aliases.get(p.getUniqueId()) == null || Main.PLUGIN.aliases.get(p.getUniqueId()).isEmpty()
                                    || !Main.PLUGIN.aliases.get(p.getUniqueId()).contains("UNKNOWN")) {
                                players.add(p.getDisplayName());
                            } else {
                                players.add("UNKNOWN");
                            }
                        }
                        blood.put(p.getLocation().getBlock().getLocation(), players);
                        if (getConfig().getInt("blood-decay") > 0) {
                            Block placed = p.getLocation().getBlock();
                            Bukkit.getScheduler().scheduleSyncDelayedTask(PLUGIN, new Runnable() {
                                @Override
                                public void run() {
                                    placed.setType(Material.AIR);
                                    ArrayList<String> players = blood.get(p.getLocation().getBlock().getLocation());
                                    if (players != null && players.contains(p.getDisplayName())) {
                                        players.remove(p.getDisplayName());
                                        blood.put(p.getLocation().getBlock().getLocation(), players);
                                    }
                                }
                            }, getConfig().getInt("blood-decay"));
                        }
                    }
                }
            }
        }, 10, 20);
    }

}
