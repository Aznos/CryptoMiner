package com.aznos.crypto;

import com.aznos.crypto.bitcoin.BitcoinPrice;
import com.aznos.crypto.command.CryptoCommand;
import com.aznos.crypto.data.Miner;
import com.aznos.crypto.data.PlayerData;
import com.aznos.crypto.db.Database;
import com.aznos.crypto.listener.InventoryClick;
import com.aznos.crypto.listener.PlayerJoin;
import com.aznos.crypto.tab.CryptoCommandTab;
import com.aznos.crypto.util.Formatting;
import com.aznos.crypto.util.Metrics;
import com.aznos.crypto.util.Revenue;
import com.google.gson.Gson;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.aznos.crypto.db.Database.connection;
import static com.aznos.crypto.db.Database.initDB;

public final class Crypto extends JavaPlugin {
    public static final Map<String, Miner> MINERS = new HashMap<>();
    public static Crypto INSTANCE;
    public static Economy economy = null;

    public static double BTC_TO_USD = 92_000;

    @Override
    public void onEnable() {
        INSTANCE = this;
        makeDataFolder();
        initDB();

        int pluginId = 24535;
        Metrics metrics = new Metrics(this, pluginId);

        registerMiners();
        if(!setupEconomy()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            getLogger().info("Giving out crypto mining revenue to players");
            for(Player player : Bukkit.getOnlinePlayers()) {
                double revenueEarnedFromMiners = 0;

                PlayerData data = Database.fetchPlayerData(player.getUniqueId());
                String inventory = data.inventory();

                for(String minerName : inventory.trim().split(",")) {
                    Miner miner = MINERS.get(minerName);
                    if(miner != null) {
                        double revenue = Revenue.calculateRevenue(miner.getHashRate(), miner.getPowerConsumption());
                        data = new PlayerData(inventory, data.crypto() + revenue);
                        Database.savePlayerData(player.getUniqueId(), data.inventory(), data.crypto());
                        revenueEarnedFromMiners += revenue;
                    }
                }

                if(revenueEarnedFromMiners > 0) {
                    player.sendMessage(ChatColor.GREEN + "You have earned " + ChatColor.GOLD + ChatColor.BOLD + Formatting.formatBitcoin(revenueEarnedFromMiners) + ChatColor.RESET + ChatColor.GOLD + "₿ from your miners");
                }
            }
        }, 24000, 24000);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            BTC_TO_USD = BitcoinPrice.getBTCPrice();
        }, 20, 20 * 60);

        getCommand("crypto").setExecutor(new CryptoCommand());
        getCommand("crypto").setTabCompleter(new CryptoCommandTab());

        getServer().getPluginManager().registerEvents(new InventoryClick(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
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

    private boolean setupEconomy() {
        if(getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault plugin not found!");
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null) {
            getLogger().severe("Economy provider not found. Please install an economy plugin like EssentialsX");
            return false;
        }

        economy = rsp.getProvider();
        getLogger().info("Economy provider found: " + economy.getName());
        return true;
    }

    private void makeDataFolder() {
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
    }

    private void registerMiners() {
        try {
            File minersFile = new File(getDataFolder(), "miners.json");
            if(!minersFile.exists()) saveResource("miners.json", false);

            Gson gson = new Gson();
            Reader reader = new FileReader(minersFile);
            Miner[] miners = gson.fromJson(reader, Miner[].class);

            for(Miner miner : miners) {
                MINERS.put(miner.getName(), miner);
            }

            getLogger().info("Loaded " + MINERS.size() + " miners from configuration file");
        } catch(Exception e) {
            getLogger().severe("Failed to load miners.json");
        }
    }
}
