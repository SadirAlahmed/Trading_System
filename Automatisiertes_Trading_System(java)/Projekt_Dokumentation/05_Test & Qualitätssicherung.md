
**Zur Qualitätssicherung werden folgende manuelle Testfälle durchgeführt:**

- **Datenabruf-Test:**
	- Ziel: Überprüfen, ob historische Daten korrekt geladen werden.
	- Vorgehen: Anwendung mit gültigem Symbol (z. B. "WTI")starten.
	- Erwartung: Liste von StockData-Objekten mit fortlaufenden Datenpunkten (ältester bis jüngster Eintrag)
- **Signalerzeugung-Test:**
	- Ziel: Korrekte BUY/SELL-Signale identifizieren.
	- Vorgehen: Datensatz mit klarem Trend einsetzen (steigende Kurse über 5 Tage, dann fallende)
	- Erwartung: Bei Kurs > MA entsteht "BUY";bei Kurz < MA entsteht "SELL"
- **Konsole-Ausgabe-Test:**
	- Ziel: TradeExecutor-Ausgaben validieren.
	- Vorgehen: Anwendung starten; Konsolenausgabe beobachten.
	- Erwartung: Jede erzeugte Signalzeile im Format [BUY]YYYY-MM-DD at X.XX$ bzw. [SELL]
- **Visualisierungstest:**
	- Ziel: ChartPlotter-Funktionalität prüfen.
	- Vorgehen: Anwendung nach Durchlauf beenden; Chart-Fenster inspizieren
	- Erwartung: Candlestick-Chart zeigt korrekt formatierte Kerzen, MA-Linie und Pfeil-Annotationen bei Signalen.


