package com.aznos.crypto.data.miners;

import com.aznos.crypto.data.Miner;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GT1030 extends Miner {
    public GT1030() {
        super("GT 1030", 6.8, 30, 0);
    }

    public static ItemStack getItemStack() {
        ItemStack stack = new ItemStack(Material.DIRT);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "GT 1030");

        List<String> lore = List.of(
                ChatColor.GRAY + "Manufacturer: " + ChatColor.GREEN + "Nvidia",
                ChatColor.GRAY + "Revenue/D: " + ChatColor.GREEN + getRevenue(6.8, 30) + "₿",
                ChatColor.GRAY + "Hash Rate: " + ChatColor.GOLD + "6.8H/s",
                ChatColor.GRAY + "Power Consumption: " + ChatColor.GOLD + "30W",
                ChatColor.GRAY + "Sell Price: " + ChatColor.GOLD + getSellPrice(0)
        );

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }

    public static double getRevenue(double hashRate, double powerConsumption) {
        double networkHashRate = 300_000_000;
        double blockReward = 6.25;
        int blocksPerDay = 144;
        double electricityRate = 0.0001;
        double bitcoinPrice = 92_000;

        double grossRevenue = (hashRate / networkHashRate) * blockReward * blocksPerDay;
        double electricityCost = (powerConsumption * electricityRate) / bitcoinPrice;
        double netRevenue = grossRevenue - electricityCost;

        return Math.max(netRevenue, 0.00000001);
    }

    private static double getSellPrice(int buyPrice) {
        return buyPrice * 0.6;
    }
}
