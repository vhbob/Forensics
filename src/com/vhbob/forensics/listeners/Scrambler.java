package com.vhbob.forensics.listeners;

import com.mysql.fabric.xmlrpc.base.Array;
import com.vhbob.forensics.Main;
import com.vhbob.forensics.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scrambler implements Listener {

    @EventHandler
    public void useScrambler(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.HAND
                && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getItem()!= null && e.getItem().hasItemMeta()) {
            if (e.getItem() != null && e.getItem().hasItemMeta()
                    && e.getItem().getItemMeta().getDisplayName().equals(Items.getScrambler().getItemMeta().getDisplayName())) {
                ArrayList<String> aliases = new ArrayList();
                if (Main.PLUGIN.aliases.get(e.getPlayer().getUniqueId()) != null) {
                    aliases = Main.PLUGIN.aliases.get(e.getPlayer().getUniqueId());
                }
                if (aliases.contains("UNKNOWN")) {
                    e.getPlayer().sendMessage(ChatColor.RED + "Your name is already scrambled!");
                    return;
                }
                aliases.add("UNKNOWN");
                Main.PLUGIN.aliases.put(e.getPlayer().getUniqueId(), aliases);
                //BLOOD
                int power = Main.PLUGIN.getConfig().getInt("scrambler-power");
                int down = Main.PLUGIN.getConfig().getInt("scrambler-time");
                int time = power * down;
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                e.getPlayer().sendMessage(ChatColor.GREEN + "Your name has been scrambled for " + (power * down) / 20 + " seconds!");
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.PLUGIN, new Runnable() {
                    @Override
                    public void run() {
                        Main.PLUGIN.aliases.get(e.getPlayer().getUniqueId()).remove("UNKNOWN");
                        e.getPlayer().sendMessage(ChatColor.RED + "You name is no longer hidden!");
                    }
                }, time);
            }
        }
    }

}
