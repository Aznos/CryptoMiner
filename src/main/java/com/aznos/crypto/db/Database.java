package com.aznos.crypto.db;

import com.aznos.crypto.Crypto;
import com.aznos.crypto.data.PlayerData;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;

public class Database {
    public static Connection connection;

    public static void initDB() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + Crypto.INSTANCE.getDataFolder() + "/data.db");

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

            Bukkit.getLogger().info("Database connection established");
        } catch(SQLException e) {
            Bukkit.getLogger().warning("Failed to establish database connection");
        }
    }

    public static void savePlayerData(UUID uuid, String inventory, double crypto) {
        String id = uuid.toString();
        String insertQuery = """
            INSERT OR REPLACE INTO player_data (id, inventory, crypto)
            VALUES (?, ?, ?);
        """;

        try(PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setString(1, id);
            stmt.setString(2, inventory);
            stmt.setDouble(3, crypto);
            stmt.executeUpdate();
        } catch(SQLException e) {
            Bukkit.getLogger().severe("Failed to save player data for " + Bukkit.getPlayer(uuid));
        }
    }

    public static PlayerData fetchPlayerData(UUID uuid) {
        String id = uuid.toString();
        String selectQuery = "SELECT inventory, crypto FROM player_data WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                String inventory = rs.getString("inventory");
                double crypto = rs.getDouble("crypto");

                return new PlayerData(inventory, crypto);
            } else {
                Bukkit.getLogger().warning("No data found for player " + Bukkit.getPlayer(uuid) + ", yet it was requested");
            }
        } catch(SQLException e) {
            Bukkit.getLogger().severe("Failed to fetch player data for " + Bukkit.getPlayer(uuid));
        }

        return new PlayerData("", 0);
    }
}
