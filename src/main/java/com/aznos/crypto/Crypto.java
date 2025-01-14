package com.aznos.crypto;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.UUID;

public final class Crypto extends JavaPlugin {
    private Connection connection;

    @Override
    public void onEnable() {
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

    private void initDB() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlit:" + getDataFolder() + "/data.db");

            String createTableQuery = """
                    CREATE TABLE IF NOT EXISTS player_data (
                        id TEXT PRIMARY KEY,
                        inventory TEXT,
                        crypto REAL
                    );
            """;

            try(PreparedStatement stmt = connection.prepareStatement(createTableQuery)) {
                stmt.executeUpdate();
            }

            getLogger().info("Database connection established");
        } catch(SQLException e) {
            getLogger().warning("Failed to establish database connection");
        }
    }

    public void savePlayerData(UUID uuid, String inventory, double crypto) {
        String id = uuid.toString();
        String insertQuery = """
            INSERT OR REPLACE INTO player_data (id, inventory, crypto)
            VALUES (?, ?, ?);
        """;

        try(PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setString(1, id);
            stmt.setString(2, inventory);
            stmt.setDouble(3, crypto);
        } catch(SQLException e) {
            getLogger().severe("Failed to save player data for " + Bukkit.getPlayer(uuid));
        }
    }

    public void fetchPlayerData(UUID uuid) {
        String id = uuid.toString();
        String selectQuery = "SELECT inventory, crypto FROM player_data WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                String inventory = rs.getString("inventory");
                double crypto = rs.getDouble("crypto");
            } else {
                getLogger().warning("No data found for player " + Bukkit.getPlayer(uuid) + ", yet it was requested");
            }
        } catch(SQLException e) {
            getLogger().severe("Failed to fetch player data for " + Bukkit.getPlayer(uuid));
        }
    }
}
