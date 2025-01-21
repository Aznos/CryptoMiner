package com.aznos.crypto.data;

import com.aznos.crypto.util.Conversions;
import com.aznos.crypto.util.Revenue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
@AllArgsConstructor
public class Miner {
    private final String name;
    private final String manufacturer;
    private final String material;
    private final double hashRate;
    private final double powerConsumption;
    private final double cost;

    public double getSellValue() {
        return Math.round(cost * 0.6 * 100.0) / 100.0;
    }

    public Material getMaterial() {
        return Material.matchMaterial(material);
    }

    public ItemStack getItemStack() {
        double revenue = Revenue.calculateRevenue(hashRate, powerConsumption);

        String manufacturerColor = switch (manufacturer) {
            case "Nvidia" -> ChatColor.GREEN.toString();
            case "AMD" -> ChatColor.RED.toString();
            default -> null;
        };

        ItemStack stack = new ItemStack(getMaterial());
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(List.of(
                ChatColor.GOLD + "Manufacturer: " + manufacturerColor + manufacturer,
                ChatColor.GOLD + "Revenue/D: " + ChatColor.GREEN + "$" + Conversions.btcToUSD(revenue),
                ChatColor.GOLD + "Hash Rate: " + ChatColor.GREEN + Conversions.formatHashRate(hashRate),
                ChatColor.GOLD + "Power Consumption: " + ChatColor.GREEN + powerConsumption + "W",
                ChatColor.GOLD + "Sell Value: " + ChatColor.GREEN + "$" + getSellValue()
        ));

        stack.setItemMeta(meta);

        return stack;
    }
}
