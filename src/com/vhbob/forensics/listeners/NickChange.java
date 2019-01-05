package com.vhbob.forensics.listeners;

import com.vhbob.forensics.Main;
import net.ess3.api.events.NickChangeEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class NickChange implements Listener {

    @EventHandler
    public void nickNameChange(NickChangeEvent e) {
        ArrayList<String> aliases = new ArrayList<>();
        if (Main.PLUGIN.aliases.get(e.getAffected().getBase().getUniqueId()) != null) {
            aliases = Main.PLUGIN.aliases.get(e.getAffected().getBase().getUniqueId());
            if (!aliases.contains(ChatColor.stripColor(e.getAffected().getBase().getDisplayName())))
                aliases.add(ChatColor.stripColor(e.getAffected().getBase().getDisplayName()));
            Main.PLUGIN.aliases.put(e.getAffected().getBase().getUniqueId(), aliases);
        } else {
            aliases.add(ChatColor.stripColor(e.getAffected().getBase().getDisplayName()));
            Main.PLUGIN.aliases.put(e.getAffected().getBase().getUniqueId(), aliases);
        }
    }

}
