package com.aznos.crypto;

import com.aznos.crypto.command.CryptoCommand;
import com.aznos.crypto.data.PlayerData;
import com.aznos.crypto.data.miners.GT1030;
import com.aznos.crypto.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

import static com.aznos.crypto.db.Database.connection;
import static com.aznos.crypto.db.Database.initDB;

public final class Crypto extends JavaPlugin {
    public static Crypto INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        makeDataFolder();
        initDB();

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for(Player player : Bukkit.getOnlinePlayers()) {
                double revenueEarnedFromMiners = 0;

                PlayerData data = Database.fetchPlayerData(player.getUniqueId());
                String inventory = data.inventory();

                for(String miner : inventory.trim().split(",")) {
                    if(miner.equalsIgnoreCase("GT-1030")) {
                        data = new PlayerData(inventory, data.crypto() + GT1030.getRevenue());
                        Database.savePlayerData(player.getUniqueId(), data.inventory(), data.crypto());
                        revenueEarnedFromMiners += GT1030.getRevenue();
                    }
                }

                if(revenueEarnedFromMiners > 0) {
                    player.sendMessage("You have earned " + revenueEarnedFromMiners + "₿ from your miners");
                }
            }
        }, 24000, 24000);

        getCommand("crypto").setExecutor(new CryptoCommand());
    }

    @Override
    public void onDisable() {
        if(connection != null) {
            try {
                connection.close();
            } catch(SQLException e) {
                getLogger().warning("Failed to close database connection");
            }
        }
    }

    private void makeDataFolder() {
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
    }
}
