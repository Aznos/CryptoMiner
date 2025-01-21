package com.aznos.crypto.util;

import com.aznos.crypto.Crypto;

public class Conversions {
    public static double btcToUSD(double btc) {
        return Double.parseDouble(String.format("%.2f", btc * Crypto.BTC_TO_USD));
    }

    public static double usdToBTC(double usd) {
        return usd / Crypto.BTC_TO_USD;
    }

    public static String formatHashRate(double hashRate) {
        if(hashRate >= 1_000_000_000) {
            return String.format("%.2f GH/s", hashRate / 1_000_000_000);
        } else if(hashRate >= 1_000_000) {
            return String.format("%.2f MH/s", hashRate / 1_000_000);
        } else if(hashRate >= 1_000) {
            return String.format("%.2f KH/s", hashRate / 1_000);
        } else {
            return String.format("%.2f H/s", hashRate);
        }
    }
}
