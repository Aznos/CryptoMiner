package com.aznos.crypto.ui;

import com.aznos.crypto.data.PlayerData;
import com.aznos.crypto.data.miners.GT1030;
import com.aznos.crypto.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MinerUI {
    public MinerUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Crypto Miners");

        PlayerData data = Database.fetchPlayerData(player.getUniqueId());
        String inventory = data.inventory();

        for(String miner : inventory.split(",")) {
            if(miner.equals("GT-1030")) {
                inv.addItem(GT1030.getItemStack());
            }
        }

        player.openInventory(inv);
    }
}
