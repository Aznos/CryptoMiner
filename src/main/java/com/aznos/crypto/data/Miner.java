package com.aznos.crypto.data;

import org.bukkit.inventory.ItemStack;

public class Miner {
    private final String name;
    private final double hashRate;
    private final double powerConsumption;
    private final double cost;

    public Miner(String name, double hashRate, double powerConsumption, double cost) {
        this.name = name;
        this.hashRate = hashRate;
        this.powerConsumption = powerConsumption;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getHashRate() {
        return hashRate;
    }

    public double getPowerConsumption() {
        return powerConsumption;
    }

    public double getCost() {
        return cost;
    }

    public double getSellValue() {
        return cost * 0.6;
    }

    public static ItemStack getItemStack() {
        return null;
    }
}
