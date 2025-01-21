package com.aznos.crypto.tab;

import com.aznos.crypto.Crypto;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CryptoCommandTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("balance", "miners", "sell", "price"), new ArrayList<>());
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("miners")) {
                return StringUtil.copyPartialMatches(args[1], List.of("purchase", "info"), new ArrayList<>());
            }

            if(args[0].equalsIgnoreCase("sell")) {
                return StringUtil.copyPartialMatches(args[1], Arrays.asList("all", "half"), new ArrayList<>());
            }
        } else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("miners") && args[1].equalsIgnoreCase("purchase") || args[1].equalsIgnoreCase("info")) {
                return StringUtil.copyPartialMatches(args[2], Crypto.MINERS.keySet(), new ArrayList<>());
            }
        }

        return null;
    }
}
