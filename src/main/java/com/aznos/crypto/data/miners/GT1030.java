package com.aznos.crypto.data.miners;

import com.aznos.crypto.Crypto;
import com.aznos.crypto.data.Miner;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GT1030 extends Miner {
    public GT1030() {
        super("GT 1030", 6.8, 30, 50);
    }

    public static ItemStack getItemStack() {
        ItemStack stack = new ItemStack(Material.DIRT);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "GT 1030");

        List<String> lore = List.of(
                ChatColor.GRAY + "Manufacturer: " + ChatColor.GREEN + "Nvidia",
                ChatColor.GRAY + "Revenue/D: " + ChatColor.GREEN + Crypto.formatBitcoin(Crypto.calculateRevenue(6.8, 30)) + "₿",
                ChatColor.GRAY + "Hash Rate: " + ChatColor.GOLD + "6.8H/s",
                ChatColor.GRAY + "Power Consumption: " + ChatColor.GOLD + "30W",
                ChatColor.GRAY + "Sell Price: $" + ChatColor.GOLD + getSellPrice(50)
        );

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }

    private static double getSellPrice(int buyPrice) {
        return buyPrice * 0.6;
    }
}
