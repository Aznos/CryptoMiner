package com.aznos.crypto.ui;

import com.aznos.crypto.Crypto;
import com.aznos.crypto.data.PlayerData;
import com.aznos.crypto.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MinerUI {
    public MinerUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Crypto Miners");

        PlayerData data = Database.fetchPlayerData(player.getUniqueId());
        String inventory = data.inventory();

        for(String miner : inventory.trim().split(",")) {
            if(miner.equalsIgnoreCase("GT-1030")) {
                inv.addItem(Crypto.MINERS.get("GT-1030").getItemStack());
            }
        }

        player.openInventory(inv);
    }
}
