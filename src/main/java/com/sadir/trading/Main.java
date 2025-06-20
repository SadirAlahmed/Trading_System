package com.sadir.trading;

import com.sadir.trading.data.AlphaVantageFetcher;
import com.sadir.trading.execution.TradeExecutor;
import com.sadir.trading.model.StockData;
import com.sadir.trading.strategy.MovingAverageStrategy;
import com.sadir.trading.strategy.TradingStrategy;
import com.sadir.trading.visual.ChartPlotter;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        String symbol = "WTI";
        TradeExecutor executor = new TradeExecutor();
        TradingStrategy strategy = new MovingAverageStrategy(executor);

        // هنا يتم جلب البيانات التاريخية للرمز المحدد من خلال كائن AlphaVantageFetcher
        // البيانات عبارة عن قائمة من كائنات StockData وكل كائن يمثل بيانات يومية مثل السعر الافتتاحي الاغلاق الاعلى الادنى
        List<StockData> historicalData = AlphaVantageFetcher.fetchHistoricalPrices(symbol);

        for (StockData data : historicalData) {
            strategy.evaluate(data);

        }
        List<Double> movingAverages = strategy.getMovingAverages();
        List<String> signals = strategy.getSignals();

        ChartPlotter.plotCandlestickWithMAAndSignals(historicalData,movingAverages,signals,symbol);

    }
}



