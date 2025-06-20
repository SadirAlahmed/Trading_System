package com.sadir.trading.strategy;

import com.sadir.trading.model.StockData;

import java.util.List;

public interface TradingStrategy {
    void evaluate(StockData data);
    List<Double> getMovingAverages();
    List<String> getSignals();
}
