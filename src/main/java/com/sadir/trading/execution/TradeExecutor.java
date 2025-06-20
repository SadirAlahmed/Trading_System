package com.sadir.trading.execution;

import com.sadir.trading.model.StockData;



public class TradeExecutor {

    public void buy(StockData data){
        System.out.println("[BUY]" + data.getDate() + " at $" + String.format("%.2f",data.getClose()));
    }
    public void sell (StockData data) {
        System.out.println("[SELL]" + data.getDate() + " at $" +  String.format("%.2f",data.getClose()));
    }
}
