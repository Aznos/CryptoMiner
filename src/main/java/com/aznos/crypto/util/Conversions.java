package com.aznos.crypto.util;

import com.aznos.crypto.Crypto;

public class Conversions {
    public static double btcToUSD(double btc) {
        return btc * Crypto.BTC_TO_USD;
    }

    public static double usdToBTC(double usd) {
        return usd / Crypto.BTC_TO_USD;
    }
}
