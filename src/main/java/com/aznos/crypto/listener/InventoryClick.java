package com.aznos.crypto.listener;

import com.aznos.crypto.Crypto;
import com.aznos.crypto.data.Miner;
import com.aznos.crypto.data.PlayerData;
import com.aznos.crypto.db.Database;
import com.aznos.crypto.ui.MinerUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if(e.getView().getTitle().equalsIgnoreCase("Crypto Miners")) {
            e.setCancelled(true);

            ItemStack clickedItem = e.getCurrentItem();
            if(clickedItem == null || !clickedItem.hasItemMeta()) return;

            ItemMeta meta = clickedItem.getItemMeta();
            String displayName = ChatColor.stripColor(meta.getDisplayName());

            for(Miner miner : Crypto.MINERS.values()) {
                if(miner.getName().equalsIgnoreCase(displayName)) {
                    PlayerData data = Database.fetchPlayerData(player.getUniqueId());
                    String inventory = data.inventory();

                    String[] inventoryItems = inventory.split(",");
                    boolean found = false;

                    StringBuilder updatedInventory = new StringBuilder();
                    for(String item : inventoryItems) {
                        if (!found && item.equalsIgnoreCase(miner.getName())) {
                            found = true;
                        } else {
                            if(!item.isBlank()) {
                                updatedInventory.append(item).append(",");
                            }
                        }
                    }

                    if(!found) {
                        player.sendMessage(ChatColor.RED + "You do not own this miner");
                        return;
                    }

                    data = new PlayerData(updatedInventory.toString(), data.crypto());
                    Database.savePlayerData(player.getUniqueId(), data.inventory(), data.crypto());

                    double sellValue = miner.getSellValue();
                    Crypto.economy.depositPlayer(player, sellValue);

                    player.closeInventory();
                    new MinerUI(player);

                    player.sendMessage(ChatColor.GREEN + "You sold " + miner.getName() + " for $" + sellValue);
                    return;
                }
            }
        }
    }
}
