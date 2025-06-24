###### **3.1 Gesamtarchitektur**

- **Main**: Einstiegspunkt, erzeugt und Executor, ruft Fetcher auf.

- **AlphaVantageFetcher**: Kapselt HTTP-Logik zum Abruf und Parsen der Json-Antwort.

- StockData: Datenmodell für Kursdaten

- **MovingAverageStrategy**: Berechnung der gleitenden Durchschnitte, Generierung von Signalen und Delegation an TradeExecutor

- TradingStrategy(Interface): Definiert den Vertrag für Handelsstrategien mit Methoden 

- **TradeExecutor**: Schnittstelle für Kauf-/Verkaufsausführung (aktuellkonsolenausgabe)

- **ChartPlotter**: Verwendung von JFreeChart zur Visualisierung.

###### **3.2 Code-Auszüge**

- **fetchHistoricalPrices()**: HTTP GET, JSON-Parsen, Sortierung nach Datum

- **evaluate()**: Warteschlangenlogik für Gleitender Durchschnitt (Periode 5), Signalerzeugung.

- **plotCandlestickWithMAAndSignals()**: Aufbau des OHLCDatasets, Zeichnen von Candlesticks, MA-Linie, Annotationen für BUY/SELL.

