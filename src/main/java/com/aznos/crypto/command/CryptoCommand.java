package com.aznos.crypto.command;

import com.aznos.crypto.db.Database;
import com.aznos.crypto.db.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CryptoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("bal")) {
            if(args.length == 1) {
                if(sender instanceof Player player) {
                    PlayerData data = Database.fetchPlayerData(player.getUniqueId());
                    player.sendMessage(ChatColor.GREEN + "You have " + ChatColor.GOLD + ChatColor.BOLD + data.crypto() + "₿");
                } else {
                    sender.sendMessage("You must be a player to use this command");
                }
            } else {
                if(Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
                    PlayerData data = Database.fetchPlayerData(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                    sender.sendMessage(ChatColor.GREEN + args[1] + " has " + ChatColor.GOLD + ChatColor.BOLD + data.crypto() + "₿");
                } else {
                    sender.sendMessage(ChatColor.RED + "Player not found");
                }
            }
        }

        return false;
    }
}
