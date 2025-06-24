###### **3.1 Anforderungen**

- Abruf historischer Kursdaten über Alpha Vantage API  
- Abbildung der Kursdaten in Objekten (StockData)  
- Implementierung einer Moving-Average-Strategie (Periode 5)  
- Ausgabe von "BUY"-und "SELL"-Signalen  
- Visuelle Darstellung als Candlestick-Chart  
- Erweiterbarkeit für weitere Strategien  

###### **3.2 UML-Klassendiagramm**

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


###### **3.3 Activity-Diagram**

flowchart TD
    A[Programm starten] --> B[Historische Daten von AlphaVantage abrufen]
    B --> C{Sind Daten abgerufen?}
    C -- Nein --> Z[Fehler ausgeben und beenden]
    C -- Ja --> D[Schleife durch jeden StockData-Eintrag]
    D --> E["Strategie auswerten (MovingAverageStrategy.evaluate)"]
    E --> F{ist die Window größe >= Period?}
    F -- Nein --> D
    F -- Ja --> G[Ältesten Wert aus Fenster entfernen]
    G --> H[Moving Average berechnen]
    H --> I{Preis > Moving Average?}
    I -- Ja --> J[Kauf-Signal generieren & kauf ausführen]
    I -- Nein --> K{Preis < Moving Average?}
    K -- Ja --> L[Verkauf-Signal generieren & Verkauf ausführen]
    K -- Nein --> M[Kein Signal]
    J --> D
    L --> D
    M --> D
    D --> |Alle Daten verarbeitet| N[Chart mit Kerzen, MA und Signalen plotten]
    N --> O[Chart anzeigen]
    O --> P[Ende]

###### **3.4 Sequence-Diagram**


sequenceDiagram
    participant Main
    participant Fetcher as AlphaVantageFetcher
    participant Strategy as MovingAverageStrategy
    participant Executor as TradeExecutor
    participant Plotter as ChartPlotter

    Main->>Fetcher: fetchHistoricalPrices("WTI")
    Fetcher-->>Main: List<StockData>

    loop for each StockData
        Main->>Strategy: evaluate(data)
        Strategy->>Strategy: Update moving average window
        alt price > MA
            Strategy->>Executor: buy(data)
        else price < MA
            Strategy->>Executor: sell(data)
        else
            Strategy->>Strategy: No action
        end
    end

    Main->>Strategy: getMovingAverages()
    Main->>Strategy: getSignals()

    Main->>Plotter: plotCandlestickWithMAAndSignals(...)
    Plotter-->>Main: Chart Displayed
