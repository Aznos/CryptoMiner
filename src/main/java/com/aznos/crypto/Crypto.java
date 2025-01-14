package com.aznos.crypto;

import com.aznos.crypto.command.CryptoCommand;
import com.aznos.crypto.data.Miner;
import com.aznos.crypto.data.PlayerData;
import com.aznos.crypto.data.miners.GT1030;
import com.aznos.crypto.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.aznos.crypto.db.Database.connection;
import static com.aznos.crypto.db.Database.initDB;

public final class Crypto extends JavaPlugin {
    private static final Map<String, Miner> MINERS = new HashMap<>();
    public static Crypto INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        makeDataFolder();
        initDB();

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            getLogger().info("Giving out crypto mining revenue to players");
            for(Player player : Bukkit.getOnlinePlayers()) {
                double revenueEarnedFromMiners = 0;

                PlayerData data = Database.fetchPlayerData(player.getUniqueId());
                String inventory = data.inventory();

                for(String miner : inventory.trim().split(",")) {
                    if(miner.equalsIgnoreCase("GT-1030")) {
                        data = new PlayerData(inventory, data.crypto() + GT1030.getRevenue(MINERS.get("GT-1030").getHashRate(), MINERS.get("GT-1030").getPowerConsumption()));
                        Database.savePlayerData(player.getUniqueId(), data.inventory(), data.crypto());
                        revenueEarnedFromMiners += GT1030.getRevenue(MINERS.get("GT-1030").getHashRate(), MINERS.get("GT-1030").getPowerConsumption());
                    }
                }

                if(revenueEarnedFromMiners > 0) {
                    player.sendMessage(ChatColor.GREEN + "You have earned " + ChatColor.GOLD + ChatColor.BOLD + revenueEarnedFromMiners + ChatColor.RESET + ChatColor.GOLD + "₿ from your miners");
                }
            }
        }, 100, 100);

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
