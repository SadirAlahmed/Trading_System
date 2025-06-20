package com.sadir.trading.execution;

import com.sadir.trading.model.StockData;

// اهميته تكمن في كونه حلقة الوصل بين الاستراتيجية التي تحلل وتقرر والاجراء العملي الشراء والبيع
//  هذا الكلاس يتلقى اوامر من الاستراتيجية والتنفيذ هو الان طباعة لمن يمكن تطويره لاحقا بارسال اوامر حقيقية الى منصة تداول او تسجيل الصفقات في قاعدة البيانات او حساب الارباح والخسائر
public class TradeExecutor {

    public void buy(StockData data){
        System.out.println("[BUY]" + data.getDate() + " at $" + String.format("%.2f",data.getClose()));
    }
    public void sell (StockData data) {
        System.out.println("[SELL]" + data.getDate() + " at $" +  String.format("%.2f",data.getClose()));
    }
}
