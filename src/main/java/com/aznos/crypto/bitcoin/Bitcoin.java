package com.aznos.crypto.bitcoin;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Bitcoin {
    private ArrayList<BitcoinData> data;
    private long timestamp;
}
