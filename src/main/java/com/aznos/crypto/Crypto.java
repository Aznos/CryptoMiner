package com.aznos.crypto;

import com.aznos.crypto.command.CryptoCommand;
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
