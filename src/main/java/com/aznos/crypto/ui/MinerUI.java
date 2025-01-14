package com.aznos.crypto.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MinerUI {
    public MinerUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Crypto Miners");

        player.openInventory(inv);
    }
}
