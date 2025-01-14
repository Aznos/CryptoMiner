package com.aznos.crypto;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

import static com.aznos.crypto.db.Database.connection;
import static com.aznos.crypto.db.Database.initDB;

public final class Crypto extends JavaPlugin {
    public static Crypto INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        initDB();
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
}
