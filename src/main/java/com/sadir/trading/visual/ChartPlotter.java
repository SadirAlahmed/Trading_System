package com.sadir.trading.visual;
import com.sadir.trading.model.StockData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.chart.ui.TextAnchor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChartPlotter {


    public static void plotCandlestickWithMAAndSignals(
            List<StockData> dataList,
            List<Double> movingAverage,
            List<String> signals,
            String symbol
    ) {
        int n = dataList.size();

        //  تجهيز مصفوفات بيانات الشموع (O,H,L,C,V,Dates)
        Date[] dates   = new Date[n];
        double[] opens  = new double[n];
        double[] highs  = new double[n];
        double[] lows   = new double[n];
        double[] closes = new double[n];
        double[] volts  = new double[n];

        for (int i = 0; i < n; i++) {
            StockData sd = dataList.get(i);
            dates[i]   = java.sql.Date.valueOf(sd.getDate());
            opens[i]   = sd.getOpen();
            highs[i]   = sd.getHigh();
            lows[i]    = sd.getLow();
            closes[i]  = sd.getClose();
            volts[i]   = sd.getVolume();
        }
        OHLCDataset ohlcDataset = new DefaultHighLowDataset(symbol, dates,  highs, lows,opens, closes, volts);

        //  تجهيز بيانات المتوسط المتحرك كـ TimeSeries
        TimeSeries maSeries = new TimeSeries("MA");
        for (int i = 0; i < n; i++) {
            Double ma = movingAverage.get(i);
            if (ma != null) {
                StockData sd = dataList.get(i);
                Day day = new Day(sd.getDate().getDayOfMonth(),
                        sd.getDate().getMonthValue(),
                        sd.getDate().getYear());
                maSeries.add(day, ma);
            }
        }
        TimeSeriesCollection maDataset = new TimeSeriesCollection(maSeries);

        //  إنشاء الشارت
        JFreeChart chart = ChartFactory.createCandlestickChart(
                symbol + " Candlestick + MA",
                "Date",
                "Price",
                ohlcDataset,
                true  /* legend */
        );

        //  تخصيص الـ Plot
        XYPlot plot = chart.getXYPlot();

        //  رسام الشموع
        // أنشئ رندرر مخصص يرسم الإطار بالأسود دائمًا
        CandlestickRenderer candleRenderer = new CandlestickRenderer() {
            @Override
            public Paint getItemOutlinePaint(int row, int column) {
                return Color.BLACK;
            }
        };

        //  اختر إذا كنت تريد إظهار الحجم أم لا
        candleRenderer.setDrawVolume(true);

        //  اختر أن ترسم الجسد ممتلئاً (so up/down paints fill the body)
        candleRenderer.setUseOutlinePaint(true);

        //  ألوان جسد الشمعة
        candleRenderer.setUpPaint(Color.WHITE);   // أيام الصعود
        candleRenderer.setDownPaint(Color.BLACK); // أيام الهبوط

        //  طريقة احتساب العرض
        candleRenderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_AVERAGE);

        //  اربط الرندرر بالطبقة الأولى في الـ plot
        plot.setRenderer(0, candleRenderer);

        // رسام خط المتوسط المتحرك
        XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer(true, false);
        lineRenderer.setSeriesStroke(0, new BasicStroke(2.0f));
        lineRenderer.setSeriesPaint(0, new Color(30, 144, 255));
        plot.setDataset(1, maDataset);
        plot.setRenderer(1, lineRenderer);

        //  تكوين المحاور
        DateAxis domain = (DateAxis) plot.getDomainAxis();
        domain.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
        domain.setLowerMargin(0.01);
        domain.setUpperMargin(0.01);

        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setAutoRangeIncludesZero(false);

        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        //  إضافة إشارات BUY/SELL كتعليقات
        for (int i = 0; i < n; i++) {
            String sig = signals.get(i);
            if (sig == null) continue;

            StockData sd = dataList.get(i);
            Day day = new Day(sd.getDate().getDayOfMonth(),
                    sd.getDate().getMonthValue(),
                    sd.getDate().getYear());
            double t = day.getMiddleMillisecond();
            double y = sd.getClose();

            XYPointerAnnotation ann;
            if ("BUY".equals(sig)) {
                ann = new XYPointerAnnotation("BUY", t, y, Math.PI/4);
                ann.setPaint(new Color(0, 153, 0));
                ann.setTextAnchor(TextAnchor.TOP_LEFT);
            } else {
                ann = new XYPointerAnnotation("SELL", t, y, -Math.PI/4);
                ann.setPaint(Color.RED);
                ann.setTextAnchor(TextAnchor.BOTTOM_RIGHT);
            }
            ann.setArrowPaint(ann.getPaint());
            ann.setArrowStroke(new BasicStroke(1.5f));
            ann.setFont(new Font("SansSerif", Font.BOLD, 12));
            plot.addAnnotation(ann);
        }

        //  دمج التكبير (zoom) والسحب (pan) كما في كودك القديم
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        panel.setMouseZoomable(false);

        final Point[] startPt = new Point[1];
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                startPt[0] = e.getPoint();
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point endPt = e.getPoint();
                double dx = endPt.getX() - startPt[0].getX();
                double dy = endPt.getY() - startPt[0].getY();

                double domainLen = domain.getRange().getLength();
                double rangeLen  = range.getRange().getLength();
                Rectangle2D area = panel.getChartRenderingInfo().getPlotInfo().getDataArea();

                domain.setRange(
                        domain.getLowerBound() - dx * domainLen / area.getWidth(),
                        domain.getUpperBound() - dx * domainLen / area.getWidth()
                );
                range.setRange(
                        range.getLowerBound() + dy * rangeLen / area.getHeight(),
                        range.getUpperBound() + dy * rangeLen / area.getHeight()
                );

                startPt[0] = endPt;
            }
        });
        panel.addMouseWheelListener(e -> {
            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                int rot = e.getWheelRotation();
                double factor = rot > 0 ? 1.1 : 0.9;

                double domCtr = (domain.getLowerBound() + domain.getUpperBound())/2;
                double ranCtr = (range.getLowerBound()  + range.getUpperBound()) /2;

                double domHalf = domain.getRange().getLength() * factor /2;
                double ranHalf = range.getRange().getLength()  * factor /2;

                domain.setRange(domCtr - domHalf, domCtr + domHalf);
                range.setRange(ranCtr - ranHalf, ranCtr + ranHalf);
            }
        });

        //  عرض النافذة
        JFrame frame = new JFrame(symbol + " Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
