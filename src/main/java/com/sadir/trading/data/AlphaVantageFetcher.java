package com.sadir.trading.data;

import com.sadir.trading.model.StockData;
import java.time.LocalDate;

import java.io.BufferedReader;  // لقراءة النصوص من الانترنت
import java.io.InputStreamReader;   // لقراءة النصوص من الانترنت
import java.net.HttpURLConnection;  // لانشاء اتصال بالانترنت مع موقع خارجي
import java.net.URL;                // لانشاء اتصال بالانترنت مع موقع خارجي
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject; // لتحليل البيانات بصيغة json

public class AlphaVantageFetcher {

    private static final String API_KEY = "OCDKYSMJOCKWX9FC"; // هذا المفتاح تجلبه من الموقع اثناء التسجيل Alpha Vantage

    public static List<StockData> fetchHistoricalPrices(String symbol) {
        List<StockData> dataList = new ArrayList<>();
        try {
            String urlString = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + symbol + "&apikey=" + API_KEY;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();

            JSONObject obj = new JSONObject(json.toString());
            if (!obj.has("Time Series (Daily)")) {
                System.err.println("Error: 'Time Series (Daily)' not found.");
                return dataList;
            }

            JSONObject timeSeries = obj.getJSONObject("Time Series (Daily)");

            for (String dateStr : timeSeries.keySet()) {
                JSONObject dailyData = timeSeries.getJSONObject(dateStr);
                StockData stock = new StockData();

                stock.setDate(LocalDate.parse(dateStr));
                stock.setOpen(Double.parseDouble(dailyData.getString("1. open")));
                stock.setHigh(Double.parseDouble(dailyData.getString("2. high")));
                stock.setLow(Double.parseDouble(dailyData.getString("3. low")));
                stock.setClose(Double.parseDouble(dailyData.getString("4. close")));
                stock.setVolume(Long.parseLong(dailyData.getString("5. volume")));

                dataList.add(stock);
            }

            // لترتيب البيانات من الأقدم إلى الأحدث
            dataList.sort(Comparator.comparing(StockData::getDate));

        } catch (Exception e) {
            System.err.println("Error fetching historical data: " + e.getMessage());
        }

        return dataList;
    }

}
