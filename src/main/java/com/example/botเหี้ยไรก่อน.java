package com.example;

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan, 2017-2021 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import java.time.Duration;
import java.time.ZonedDateTime;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Strategy;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import org.ta4j.core.rules.OverIndicatorRule;
import org.ta4j.core.rules.StopGainRule;
import org.ta4j.core.rules.StopLossRule;
import org.ta4j.core.rules.UnderIndicatorRule;

import ta4jexamples.loaders.CsvTradesLoader;

/**
 * This class is an example of a dummy trading bot using ta4j.
 * <p/>
 */
public class botเหี้ยไรก่อน {

    /**
     * Close price of the last bar
     */
    private static Num LAST_BAR_CLOSE_PRICE;

    /**
     * Builds a moving bar series (i.e. keeping only the maxBarCount last bars)
     *
     * @param maxBarCount the number of bars to keep in the bar series (at maximum)
     * @return a moving bar series
     */
    private static BarSeries initMovingBarSeries(int maxBarCount) {
        BarSeries series = CsvTradesLoader.loadBitstampSeries();
        System.out.print("Initial bar count: " + series.getBarCount());
        // Limitating the number of bars to maxBarCount
        series.setMaximumBarCount(maxBarCount);
        LAST_BAR_CLOSE_PRICE = series.getBar(series.getEndIndex()).getClosePrice();
        System.out.println(" (limited to " + maxBarCount + "), close price = " + LAST_BAR_CLOSE_PRICE);
        return series;
    }

    /**
     * @param series a bar series
     * @return a dummy strategy
     */
    private static Strategy buildStrategy(BarSeries series) {
        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        SMAIndicator shortSma = new SMAIndicator(closePrice, 5);
        SMAIndicator longSma = new SMAIndicator(closePrice, 12);

        MACDIndicator MACD = new MACDIndicator(closePrice);
        RSIIndicator RSI = new RSIIndicator(closePrice, 14);
        EMAIndicator signal = new EMAIndicator(MACD, 9);
        // Signals
        // Buy when SMA goes over close price
        // Sell when close price goes over SMA
        Strategy buySellSignals = new BaseStrategy(new CrossedUpIndicatorRule(MACD, signal),
                new CrossedDownIndicatorRule(MACD, signal)
                        .and((new CrossedUpIndicatorRule(RSI, 90)).or(new CrossedDownIndicatorRule(RSI, 50)))
                        .or(new StopLossRule(closePrice, series.numOf(2))
                                .or(new StopGainRule(closePrice, series.numOf(3)))));

        return buySellSignals;

    }

    interface Backtest {
        static String Backtest1(BarSeries series) throws InterruptedException {
            Strategy strategy = buildStrategy(series);
            TradingRecord tradingRecord = new BaseTradingRecord();
            String Bar = null;
            for (int i = 0; i < series.getBarCount() - 1; i++) {
                Thread.sleep(30);
                Bar newBar = series.getBar(i);

                Bar = Bar + newBar.getClosePrice().toString();
                int endIndex = i;
                System.out.println(endIndex);
                if (strategy.shouldEnter(endIndex)) {
                    // Our strategy should enter
                    System.out.println("Strategy should ENTER on " + endIndex);
                    boolean entered = tradingRecord.enter(endIndex, newBar.getClosePrice(), DecimalNum.valueOf(10));
                    if (entered) {
                        Trade entry = tradingRecord.getLastEntry();
                        System.out.println("Entered on " + entry.getIndex() + " (price=" + entry.getNetPrice().doubleValue()
                                + ", amount=" + entry.getAmount().doubleValue() + ")");
                    }
                } else if (strategy.shouldExit(endIndex)) {
                    // Our strategy should exit
                    System.out.println("Strategy should EXIT on " + endIndex);
                    boolean exited = tradingRecord.exit(endIndex, newBar.getClosePrice(), DecimalNum.valueOf(10));
                    if (exited) {
                        Trade exit = tradingRecord.getLastExit();
                        System.out.println("Exited on " + exit.getIndex() + " (price=" + exit.getNetPrice().doubleValue()
                                + ", amount=" + exit.getAmount().doubleValue() + ")");
                    }
                }
            }
            return Bar;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("********************** Initialization **********************");
        // Getting the bar series
        // BarSeries series = initMovingBarSeries(20);
        BarSeries series = new BuildBarSeries().buildAndAddData();
        // Building the trading strategy

        Strategy strategy = buildStrategy(series);

        // Initializing the trading history
        TradingRecord tradingRecord = new BaseTradingRecord();
        System.out.println("************************************************************");

        /*
         * We run the strategy for the 50 next bars.
         */
        for (int i = 0; i < series.getBarCount() - 1; i++) {

            // New bar
            Thread.sleep(30); // I know...
            Bar newBar = series.getBar(i);
            // Bar newBar = generateRandomBar();
            System.out.println("------------------------------------------------------\n"
                    + "Bar " + i
                    + " added, close price = " + newBar.getClosePrice().doubleValue());
            // series.addBar(newBar);

            int endIndex = i;
            System.out.println(endIndex);
            if (strategy.shouldEnter(endIndex)) {
                // Our strategy should enter
                System.out.println("Strategy should ENTER on " + endIndex);
                boolean entered = tradingRecord.enter(endIndex, newBar.getClosePrice(), DecimalNum.valueOf(10));
                if (entered) {
                    Trade entry = tradingRecord.getLastEntry();
                    System.out.println("Entered on " + entry.getIndex() + " (price=" + entry.getNetPrice().doubleValue()
                            + ", amount=" + entry.getAmount().doubleValue() + ")");
                }
            } else if (strategy.shouldExit(endIndex)) {
                // Our strategy should exit
                System.out.println("Strategy should EXIT on " + endIndex);
                boolean exited = tradingRecord.exit(endIndex, newBar.getClosePrice(), DecimalNum.valueOf(10));
                if (exited) {
                    Trade exit = tradingRecord.getLastExit();
                    System.out.println("Exited on " + exit.getIndex() + " (price=" + exit.getNetPrice().doubleValue()
                            + ", amount=" + exit.getAmount().doubleValue() + ")");
                }
            }
        }
    }

}