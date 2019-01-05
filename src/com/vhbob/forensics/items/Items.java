package com.vhbob.forensics.items;

import com.vhbob.forensics.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

public class Items {

    public static ItemStack getScanner() {
        ItemStack scanner;
        String material = Main.PLUGIN.getConfig().getString("scanner-material");
        if (Material.getMaterial(material) != null) {
            scanner = new ItemStack(Material.getMaterial(material));
            ItemMeta sm = scanner.getItemMeta();
            sm.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.PLUGIN.getConfig().getString("scanner-name")));
            scanner.setItemMeta(sm);
        } else {
            scanner = new ItemStack(Material.STICK);
            ItemMeta sm = scanner.getItemMeta();
            sm.setDisplayName("Item's material is not correct!");
            scanner.setItemMeta(sm);
        }
        return scanner;
    }

    public static ItemStack getScrambler() {
        ItemStack scanner;
        String material = Main.PLUGIN.getConfig().getString("scrambler-material");
        if (Material.getMaterial(material) != null) {
            scanner = new ItemStack(Material.getMaterial(material));
            ItemMeta sm = scanner.getItemMeta();
            sm.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.PLUGIN.getConfig().getString("scrambler-name")));
            scanner.setItemMeta(sm);
        } else {
            scanner = new ItemStack(Material.STICK);
            ItemMeta sm = scanner.getItemMeta();
            sm.setDisplayName("Item's material is not correct!");
            scanner.setItemMeta(sm);
        }
        return scanner;
    }

    public static ItemStack getRecorder() {
        ItemStack scanner;
        String material = Main.PLUGIN.getConfig().getString("recorder-material");
        if (Material.getMaterial(material) != null) {
            scanner = new ItemStack(Material.getMaterial(material));
            ItemMeta sm = scanner.getItemMeta();
            sm.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.PLUGIN.getConfig().getString("recorder-name")));
            scanner.setItemMeta(sm);
        } else {
            scanner = new ItemStack(Material.STICK);
            ItemMeta sm = scanner.getItemMeta();
            sm.setDisplayName("Item's material is not correct!");
            scanner.setItemMeta(sm);
        }
        return scanner;
    }

    public static ItemStack getRecord(UUID identifier) {
        ItemStack scanner;
        String material = Main.PLUGIN.getConfig().getString("recorded-material");
        if (Material.getMaterial(material) != null) {
            scanner = new ItemStack(Material.getMaterial(material));
            ItemMeta sm = scanner.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            lore.add(identifier.toString());
            sm.setLore(lore);
            sm.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.PLUGIN.getConfig().getString("recorded-name")));
            scanner.setItemMeta(sm);
        } else {
            scanner = new ItemStack(Material.STICK);
            ItemMeta sm = scanner.getItemMeta();
            sm.setDisplayName("Item's material is not correct!");
            scanner.setItemMeta(sm);
        }
        return scanner;
    }

}
