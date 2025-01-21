package com.aznos.crypto.listener;

import com.aznos.crypto.db.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Database.initPlayerData(e.getPlayer().getUniqueId());
    }
}
