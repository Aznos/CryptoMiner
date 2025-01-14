package com.aznos.crypto.data.miners;

import com.aznos.crypto.data.Miner;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GT1030 extends Miner {
    public GT1030() {
        super("GT 1030", 0.5, 30, 0);
    }

    public static ItemStack getItemStack() {
        ItemStack stack = new ItemStack(Material.DIRT);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "GT 1030");

        List<String> lore = List.of(
                ChatColor.GRAY + "Manufacturer: " + ChatColor.GREEN + "Nvidia",
                ChatColor.GRAY + "Hash Rate: " + ChatColor.GOLD + "0.5",
                ChatColor.GRAY + "Power Consumption: " + ChatColor.GOLD + "30",
                ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "0"
        );

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }
}
