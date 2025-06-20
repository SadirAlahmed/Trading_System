package com.sadir.trading.strategy;

import com.sadir.trading.model.StockData;

import java.util.List;

public interface TradingStrategy {
    void evaluate(StockData data);
    List<Double> getMovingAverages();
    List<String> getSignals();
}
/*
هذه الانترفيس هي بمثابة عقد او قانون اي كلاس يلتزم به
 يجب ان يحتوي على وظائف معينة هنا في الكود
 اي كلاس يريد ان يسمى نفسه استراتيجية تداول
  يجب ان ينفذ دالة اسمها evaluate() وتاخذ StockData
 */