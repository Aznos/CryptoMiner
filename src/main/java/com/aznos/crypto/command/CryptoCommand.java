package com.aznos.crypto.command;

import com.aznos.crypto.Crypto;
import com.aznos.crypto.data.PlayerData;
import com.aznos.crypto.data.miners.GT1030;
import com.aznos.crypto.db.Database;
import com.aznos.crypto.ui.MinerUI;
import com.aznos.crypto.util.Conversions;
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
                            double balance = Crypto.economy.getBalance(player);

                            if(balance >= miner.getCost()) {
                                data = new PlayerData(data.inventory() + "GT-1030,", data.crypto());
                                Database.savePlayerData(player.getUniqueId(), data.inventory(), data.crypto());
                                Crypto.economy.withdrawPlayer(player, miner.getCost());
                                player.sendMessage(ChatColor.GREEN + "You have purchased a GT-1030 miner for " + ChatColor.GOLD + ChatColor.BOLD + miner.getCost() + ChatColor.RESET + ChatColor.GREEN + "$");
                            } else {
                                player.sendMessage(ChatColor.RED + "You do not have enough money to purchase a GT-1030 miner");
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
        } else if(args[0].equalsIgnoreCase("sell")) {
            if(sender instanceof Player player) {
                PlayerData data = Database.fetchPlayerData(player.getUniqueId());
                double balance = Crypto.economy.getBalance(player);
                double crypto = data.crypto();

                if(args.length == 2) {
                    if(args[1].equalsIgnoreCase("all")) {
                        if(crypto > 0) {
                            double amount = Conversions.btcToUSD(crypto);
                            Crypto.economy.depositPlayer(player, amount);
                            Database.savePlayerData(player.getUniqueId(), data.inventory(), 0);
                            player.sendMessage(ChatColor.GREEN + "You have sold all your crypto for " + ChatColor.GOLD + ChatColor.BOLD + String.format("%.2f", amount) + ChatColor.RESET + ChatColor.GOLD + "$");
                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have any crypto to sell");
                        }
                    } else if(args[1].equalsIgnoreCase("half")) {
                        if(crypto > 0) {
                            double amount = Conversions.btcToUSD(crypto / 2);
                            Crypto.economy.depositPlayer(player, amount);
                            Database.savePlayerData(player.getUniqueId(), data.inventory(), crypto - (crypto / 2));
                            player.sendMessage(ChatColor.GREEN + "You have sold half of your crypto for " + ChatColor.GOLD + ChatColor.BOLD + String.format("%.2f", amount) + ChatColor.RESET + ChatColor.GOLD + "$");
                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have any crypto to sell");
                        }
                    } else {
                        double amount = Conversions.usdToBTC(Double.parseDouble(args[1]));

                        if(amount <= crypto) {
                            Crypto.economy.depositPlayer(player, Double.parseDouble(args[1]));
                            Database.savePlayerData(player.getUniqueId(), data.inventory(), crypto - amount);
                            player.sendMessage(ChatColor.GREEN + "You have sold " + ChatColor.GOLD + ChatColor.BOLD + String.format("%.2f", Double.parseDouble(args[1])) + ChatColor.RESET + ChatColor.GOLD + "$ worth of crypto");
                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have enough crypto to sell that amount");
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Invalid subcommand");
                }
            } else {
                sender.sendMessage("You must be a player to use this command");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid subcommand");
        }

        return false;
    }
}
