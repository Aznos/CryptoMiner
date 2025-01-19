package com.aznos.crypto.util;

import com.aznos.crypto.Crypto;

public class Revenue {
    public static double calculateRevenue(double hashRate, double powerConsumption) {
        double networkHashRate = 400e18; //400 EH/s
        double blockReward = 6.25;
        int blocksPerDay = 144;
        double electricityRate = 0.0001;

        double grossRevenue = (hashRate / networkHashRate) * blockReward * blocksPerDay;
        double electricityCost = (powerConsumption * electricityRate) / Crypto.BTC_TO_USD;
        double netRevenue = grossRevenue - electricityCost;

        return Math.max(netRevenue, 0.00000001);
    }
}
