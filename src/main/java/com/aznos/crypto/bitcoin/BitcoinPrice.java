package com.aznos.crypto.bitcoin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.Bukkit;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BitcoinPrice {
    private static final String API_URL = "https://api.coincap.io/v2/assets?search=bitcoin&limit=1";

    public static double getBTCPrice() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .build();

            HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
            String respBody = resp.body();

            ObjectMapper om = new ObjectMapper();
            Bitcoin btcData = om.readValue(respBody, Bitcoin.class);
            BitcoinData btc = btcData.getData().getFirst();
            String usdPrice = btc.getPriceUsd();

            return Double.parseDouble(usdPrice);
        } catch(Exception e) {
            Bukkit.getLogger().warning("Failed to get BTC price, defaulting to $92,000");
            return 0;
        }
    }
}
