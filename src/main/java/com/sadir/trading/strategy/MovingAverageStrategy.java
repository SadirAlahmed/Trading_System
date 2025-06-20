package com.sadir.trading.strategy;

import com.sadir.trading.model.StockData;
import com.sadir.trading.execution.TradeExecutor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MovingAverageStrategy implements TradingStrategy {

    private final int period = 5;
    private final Queue<Double> window = new LinkedList<>();
    private double sum = 0;

    private final TradeExecutor executor;

    private final List<Double> movingAverages = new ArrayList<>();
    private final List<String> signals = new ArrayList<>();
    //قائمة لحفظ أسعار الإغلاق الأخيرة لحساب RSI
    private final List<Double> closingPrices = new ArrayList<>();

    public MovingAverageStrategy(TradeExecutor executor) {
        this.executor = executor;
    }
    @Override
    public void evaluate(StockData data) {
        double price = data.getClose();

        window.add(price);
        sum += price;

        if (window.size() > period){
            sum -= window.remove();
        }
        if(window.size() == period) {
            double average = sum / period;
            movingAverages.add(average);
            if (price > average) {
                signals.add("BUY");
                executor.buy(data);
            } else if (price < average) {
                signals.add("SELL");
                executor.sell(data);
            } else {
                signals.add(null); // لا يوجد تقاطع واضح
            }
        } else {
            movingAverages.add(null); // لم نصل بعد للحجم المطلوب
            signals.add(null);
        }
    }

    // Getter لتوفير قائمة المتوسطات المتحركة
    public List<Double> getMovingAverages() {
        return movingAverages;
    }

    // Getter لتوفير قائمة إشارات البيع والشراء
    public List<String> getSignals() {
        return signals;
    }
}
