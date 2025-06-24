 Trading System

Ein automatisiertes Handelssystem basierend auf der Gleitender-Durchschnitt-Strategie.

 Systemkomponenten

- `Main`: Einstiegspunkt des Programms
- `AlphaVantageFetcher`: Holt historische Börsendaten
- `MovingAverageStrategy`: Bewertet Daten mit Hilfe gleitender Durchschnitte
- `TradeExecutor`: Führt Kauf- und Verkaufsentscheidungen aus
- `ChartPlotter`: Visualisiert Kursverläufe mit Diagrammen

 Inhaltsverzeichnis

- [01 - Einleitung](docs/02_Einleitung.md)
- [02 - Analyse & Planung](docs/03_Analyse_&_Planung.md)
- [03 - Realisierung](docs/04_Realisierung.md)
- [04 - Test & Qualitätssicherung](docs/05_Test_&_Qualitätssicherung.md)
- [05 - Fazit & Ausblick](docs/06_Fazit_&_Ausblick.md)
- [06 - Quellen](docs/07_Quellen.md)

 UML-Klassendiagramm

```mermaid
classDiagram
    class Main {
        - symbol: String
    }
    class AlphaVantageFetcher {
        + fetchHistoricalPrices()
    }
    class MovingAverageStrategy {
        - period: int
        - window: Queue<Double>
        - signals: List<String>
    }
    class TradingStrategy {
        <<interface>>
        + evaluate(StockData data)
        + getMovingAverages(): List<Double>
        + getSignals(): List<String>
    }
    class TradeExecutor {
        + buy()
        + sell()
    }
    class StockData {
        - date: LocalDate
        - open: double
        - high: double
        - low: double
        - close: double
        - volume: long
    }
    class ChartPlotter {
        + plotCandlestickWithMA()
    }

    Main --> AlphaVantageFetcher 
    Main --> TradeExecutor
    Main --> TradingStrategy
    Main --> ChartPlotter
    TradingStrategy <|.. MovingAverageStrategy
    MovingAverageStrategy --> TradeExecutor
    TradingStrategy --> StockData
    ChartPlotter --> StockData
