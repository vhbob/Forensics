package com.vhbob.forensics.listeners;

import com.dthielke.herochat.ChannelChatEvent;
import com.dthielke.herochat.Chatter;
import com.vhbob.forensics.Main;
import com.vhbob.forensics.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Recorder implements Listener {

    @EventHandler
    public void onListen(PlayerInteractEvent e) {
        if (e.getItem() != null && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getHand() == EquipmentSlot.HAND && e.getItem().hasItemMeta()) {
            if (e.getItem().getItemMeta().getDisplayName().equals(Items.getRecorder().getItemMeta().getDisplayName())) {
                if (Main.PLUGIN.playerToRecorder.containsKey(e.getPlayer().getUniqueId())) {
                    e.getPlayer().sendMessage(ChatColor.RED + "You already have a recorder running!");
                    return;
                }
                if (Main.PLUGIN.getConfig().getInt("recorder-limit") <= 0) {
                    e.getPlayer().sendMessage(ChatColor.RED + "Recorder limit is not setup correctly!");
                    return;
                }
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                UUID recorderID = UUID.randomUUID();
                Main.PLUGIN.playerToRecorder.put(e.getPlayer().getUniqueId(), recorderID);
                e.getPlayer().sendMessage(ChatColor.GREEN + "You started recording");
                ArrayList<String> begin = new ArrayList<>();
                begin.add(ChatColor.GREEN + "-=[Recorded Conversation]=-");
                Main.PLUGIN.recorded.put(recorderID, begin);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.PLUGIN, new Runnable() {
                    @Override
                    public void run() {
                        if (Main.PLUGIN.playerToRecorder.containsKey(e.getPlayer().getUniqueId())) {
                            e.getPlayer().getInventory().addItem(Items.getRecord(recorderID));
                            e.getPlayer().sendMessage(ChatColor.GREEN + "Your recording finished");
                            Main.PLUGIN.playerToRecorder.remove(e.getPlayer().getUniqueId(), recorderID);
                        }
                    }
                }, Main.PLUGIN.getConfig().getInt("recorder-limit"));
            } else {
                if (Main.PLUGIN.recorded.keySet() != null) {
                    for (UUID id : Main.PLUGIN.recorded.keySet()) {
                        if (e.getItem().equals(Items.getRecord(id))) {
                            for (String message : Main.PLUGIN.recorded.get(id)) {
                                e.getPlayer().sendMessage(message);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(ChannelChatEvent e) {
        List<String> blocked = Main.PLUGIN.getConfig().getStringList("recorder-exempt");
        if (!blocked.contains(ChatColor.stripColor(e.getChannel().getNick()))) {
            for (Chatter chatter : e.getChannel().getMembers()) {
                if (Main.PLUGIN.playerToRecorder.containsKey(chatter.getUniqueId())) {
                    UUID recorderID = Main.PLUGIN.playerToRecorder.get(chatter.getUniqueId());
                    ArrayList<String> convo = Main.PLUGIN.recorded.get(recorderID);
                    String messageToAdd = e.getSender().getPlayer().getDisplayName() + ": " + e.getMessage();
                    if (convo != null)
                    convo.add(messageToAdd);
                    Main.PLUGIN.recorded.put(recorderID, convo);
                }
            }
        }
    }

}
