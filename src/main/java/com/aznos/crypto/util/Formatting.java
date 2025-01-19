package com.aznos.crypto.util;

import java.text.DecimalFormat;

public class Formatting {
    public static String formatBitcoin(double value) {
        DecimalFormat df = new DecimalFormat("0.00000000");
        return df.format(value);
    }
}
