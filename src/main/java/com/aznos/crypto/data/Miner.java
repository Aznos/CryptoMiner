package com.aznos.crypto.data;

import com.aznos.crypto.Crypto;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
        return cost * 0.6;
    }

    public Material getMaterial() {
        return Material.matchMaterial(material);
    }

    public ItemStack getItemStack() {
        double revenue = Crypto.calculateRevenue(hashRate, powerConsumption);

        ItemStack stack = new ItemStack(getMaterial());
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(List.of(
                "Manufacturer: " + manufacturer,
                "Revenue/D: " + revenue,
                "Hash Rate: " + hashRate + "H/s",
                "Power Consumption: " + powerConsumption + "W",
                "Sell Value: $" + getSellValue()
        ));

        return stack;
    }
}
