package com.aznos.crypto.listener;

import com.aznos.crypto.Crypto;
import com.aznos.crypto.data.Miner;
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
                    player.sendMessage(ChatColor.GREEN + "You clicked on miner: " + miner.getName());
                    return;
                }
            }
        }
    }
}
