package com.vhbob.forensics.listeners;

import com.vhbob.forensics.Main;
import com.vhbob.forensics.items.Items;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class Scanner implements Listener {

    @EventHandler
    public void onScan(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null && e.getItem().hasItemMeta()
                && e.getItem().getItemMeta().getDisplayName().equals(Items.getScanner().getItemMeta().getDisplayName())) {
            Material type = e.getClickedBlock().getType();
            if (type == null)
                return;
            String typeName = type.toString().toUpperCase();
            if (type.equals(Material.REDSTONE_WIRE)) {
                if (Main.PLUGIN.blood.get(e.getClickedBlock().getLocation()) != null) {
                    List<String> names = new ArrayList<>();
                    for (String playerName : Main.PLUGIN.blood.get(e.getClickedBlock().getLocation())) {
                        names.add(playerName);
                    }
                    if (!names.isEmpty()) {
                        e.getPlayer().sendMessage(ChatColor.DARK_GREEN + "DNA found!");
                        for (String s : names) {
                            e.getPlayer().sendMessage(ChatColor.GREEN + "DNA of: " + ChatColor.RESET + s);
                        }
                    } else {
                        e.getPlayer().sendMessage(ChatColor.RED + "No DNA found!");
                    }
                } else {
                    e.getPlayer().sendMessage(ChatColor.RED + "No DNA found!");
                }
            } else if (Main.PLUGIN.interactable.keySet().contains(e.getClickedBlock().getLocation())) {
                List<String> names = new ArrayList<>();
                for (String playerName : Main.PLUGIN.interactable.get(e.getClickedBlock().getLocation())) {
                    names.add(playerName);
                }
                if (!names.isEmpty()) {
                    e.getPlayer().sendMessage(ChatColor.DARK_GREEN + "DNA found!");
                    for (String s : names) {
                        e.getPlayer().sendMessage(ChatColor.GREEN + "DNA of: " + ChatColor.RESET + s);
                    }
                } else {
                    e.getPlayer().sendMessage(ChatColor.RED + "No DNA found!");
                }
            } else if (typeName.contains("DOOR") || typeName.contains("LEVER") || typeName.contains("BUTTON") || typeName.contains("CHEST")) {
                e.getPlayer().sendMessage(ChatColor.RED + "No DNA found!");
            }
        }
    }

}
