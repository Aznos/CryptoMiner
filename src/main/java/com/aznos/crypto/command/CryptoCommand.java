package com.aznos.crypto.command;

import com.aznos.crypto.Crypto;
import com.aznos.crypto.data.PlayerData;
import com.aznos.crypto.data.miners.GT1030;
import com.aznos.crypto.db.Database;
import com.aznos.crypto.ui.MinerUI;
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
                    player.sendMessage(ChatColor.GREEN + "You have " + ChatColor.GOLD + ChatColor.BOLD + Crypto.formatBitcoin(data.crypto()) + ChatColor.RESET + ChatColor.GOLD + "₿");
                } else {
                    sender.sendMessage("You must be a player to use this command");
                }
            } else {
                if(Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
                    PlayerData data = Database.fetchPlayerData(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                    sender.sendMessage(ChatColor.GREEN + args[1] + " has " + ChatColor.GOLD + ChatColor.BOLD + Crypto.formatBitcoin(data.crypto()) + ChatColor.RESET + ChatColor.GOLD + "₿");
                } else {
                    sender.sendMessage(ChatColor.RED + "Player not found");
                }
            }
        } else if(args[0].equalsIgnoreCase("miners") || args[0].equalsIgnoreCase("miner")) {
            if(sender instanceof Player player) {
                if(args.length == 1) {
                    new MinerUI(player);
                } else if(args.length == 3) {
                    if(args[1].equalsIgnoreCase("purchase") || args[1].equalsIgnoreCase("buy")) {
                        if(args[2].equalsIgnoreCase("GT-1030")) {
                            GT1030 miner = new GT1030();
                            PlayerData data = Database.fetchPlayerData(player.getUniqueId());
                            if(data.crypto() >= miner.getCost()) {
                                String inventory = data.inventory();
                                inventory += "GT-1030,";

                                data = new PlayerData(inventory, data.crypto() - miner.getCost());
                                Database.savePlayerData(player.getUniqueId(), data.inventory(), data.crypto());
                                player.sendMessage(ChatColor.GREEN + "You have purchased a GT-1030 miner for " + miner.getCost() + "₿");
                            } else {
                                player.sendMessage(ChatColor.RED + "You do not have enough ₿ to purchase this miner");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Invalid subcommand");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Invalid subcommand");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Invalid subcommand");
                }
            } else {
                sender.sendMessage("You must be a player to use this command");
            }
        } else if(args[0].equalsIgnoreCase("purchase") || args[0].equalsIgnoreCase("buy")) {
            if(sender instanceof Player player) {
                double amount = Double.parseDouble(args[1]);
                PlayerData data = Database.fetchPlayerData(player.getUniqueId());


            } else {
                sender.sendMessage("You must be a player to use this command");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid subcommand");
        }

        return false;
    }
}
