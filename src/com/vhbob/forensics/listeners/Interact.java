package com.vhbob.forensics.listeners;

import com.vhbob.forensics.Main;
import com.vhbob.forensics.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;

public class Interact implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND)
            return;
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && !e.getPlayer().hasPermission("forensics.exempt") && (
            e.getItem() == null || !e.getItem().hasItemMeta() || !e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Items.getScanner().getItemMeta().getDisplayName()))) {
            Material type = e.getClickedBlock().getType();
            String typeName = type.toString().toUpperCase();
            if (typeName.contains("DOOR") || typeName.contains("LEVER") || typeName.contains("BUTTON") || typeName.contains("CHEST")) {
                ArrayList<String> players = new ArrayList<>();
                if (Main.PLUGIN.interactable.get(e.getClickedBlock().getLocation()) != null) {
                    players = Main.PLUGIN.interactable.get(e.getClickedBlock().getLocation());
                }
                boolean duplicate = false;
                if (Main.PLUGIN.aliases.containsKey(e.getPlayer().getUniqueId())) {
                    for (String alias : (Main.PLUGIN.aliases.get(e.getPlayer().getUniqueId()))) {
                        for (String name : players) {
                            if (ChatColor.stripColor(name).equalsIgnoreCase(alias)) {
                                duplicate = true;
                            }
                        }
                    }
                }
                if (!duplicate && !players.contains(e.getPlayer().getDisplayName()) && !e.getPlayer().hasPermission("forensics.exempt")) {
                    if ( Main.PLUGIN.aliases.get(e.getPlayer().getUniqueId()) == null || Main.PLUGIN.aliases.get(e.getPlayer().getUniqueId()).isEmpty()
                            || !Main.PLUGIN.aliases.get(e.getPlayer().getUniqueId()).contains("UNKNOWN")) {
                        players.add(e.getPlayer().getDisplayName());
                    } else {
                        players.add("UNKNOWN");
                    }
                }
                Main.PLUGIN.interactable.put(e.getClickedBlock().getLocation(), players);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.PLUGIN, new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> players = new ArrayList<>();
                        if (Main.PLUGIN.interactable.get(e.getClickedBlock().getLocation()) != null) {
                            players = Main.PLUGIN.interactable.get(e.getClickedBlock().getLocation());
                        }
                        if (players.contains(e.getPlayer().getDisplayName())) {
                            players.remove(e.getPlayer().getDisplayName());
                        }
                        Main.PLUGIN.interactable.put(e.getClickedBlock().getLocation(), players);
                        if (players.isEmpty()) {
                            Main.PLUGIN.interactable.remove(e.getClickedBlock().getLocation());
                        }
                    }
                }, Main.PLUGIN.getConfig().getInt("dna-decay"));
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (Main.PLUGIN.interactable.keySet().contains(e.getBlock().getLocation())) {
            Main.PLUGIN.interactable.remove(e.getBlock().getLocation());
        }
    }

}
