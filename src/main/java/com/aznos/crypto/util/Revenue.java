package com.aznos.crypto.util;

import com.aznos.crypto.Crypto;

public class Revenue {
    public static double calculateRevenue(double hashRate, double powerConsumption) {
        double networkHashRate = 900e12; // 900 TH/s
        double blockReward = 6.25;
        int blocksPerDay = 144;
        double electricityRate = 0.1;
        double btcToUsd = Crypto.BTC_TO_USD;

        double grossRevenueBTC = (hashRate / networkHashRate) * blockReward * blocksPerDay;
        double electricityCostBTC = ((powerConsumption * 24) / 1000.0) * electricityRate / btcToUsd;
        double netRevenueBTC = grossRevenueBTC - electricityCostBTC;

        return Math.max(netRevenueBTC, 0.00000001);
    }
}
