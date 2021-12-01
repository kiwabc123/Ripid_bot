package com.example;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BacktestExecutor;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.ReturnOverMaxDrawdownCriterion;
import org.ta4j.core.analysis.criteria.VersusBuyAndHoldCriterion;
import org.ta4j.core.analysis.criteria.WinningPositionsRatioCriterion;
import org.ta4j.core.analysis.criteria.pnl.GrossReturnCriterion;
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
 * Quickstart for ta4j.
 *
 * Global example.
 */
public class botอะไรซักอย่าง {

    public static void main(String[] args) {

        // Getting a bar series (from any provider: CSV, web service, etc.)
        // BarSeries series = CsvTradesLoader.loadBitstampSeries();
        BarSeries series = new BuildBarSeries().buildAndAddData();
        // for (int i = 0; i < series.getBarCount(); i++) {
        //     System.out.println(series.getBar(i));
        // }

        // Getting the close price of the bars
        Num firstClosePrice = series.getBar(0).getClosePrice();
        System.out.println("First close price: " + firstClosePrice.doubleValue());
        // Or within an indicator:
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        // Here is the same close price:
        System.out.println(firstClosePrice.isEqual(closePrice.getValue(0))); // equal to firstClosePrice

        // Getting the simple moving average (SMA) of the close price over the last 5
        // bars
        SMAIndicator shortSma = new SMAIndicator(closePrice, 5);
        // Here is the 5-bars-SMA value at the 42nd index
        // System.out.println("5-bars-SMA value at the 42nd index: " + shortSma.getValue(42).doubleValue());

        // Getting a longer SMA (e.g. over the 30 last bars)
        SMAIndicator longSma = new SMAIndicator(closePrice, 12);
        EMAIndicator longEma = new EMAIndicator(closePrice, 26);

        MACDIndicator MACD = new MACDIndicator(closePrice, 12, 26);
        EMAIndicator signal = new EMAIndicator(MACD, 9);
        RSIIndicator RSI = new RSIIndicator(closePrice, 14);

        System.out.println(
                "signal" + signal.getValue(1));
        System.out.println(
                MACD.getShortTermEma().getValue(1).doubleValue() - MACD.getLongTermEma().getValue(1).doubleValue());
        System.out.println(RSI);
        // Ok, now let's building our trading rules!

        // Buying rules
        // We want to buy:
        // - if the 5-bars SMA crosses over 30-bars SMA
        // - or if the price goes below a defined price (e.g $800.00)
        Rule buyingRule = new CrossedUpIndicatorRule(MACD, signal)
                .and(new CrossedUpIndicatorRule(RSI, 55));

        // Selling rules
        // We want to sell:
        // - if the 5-bars SMA crosses under 30-bars SMA
        // - or if the price loses more than 3%
        // - or if the price earns more than 2F%
        Rule sellingRule = new CrossedDownIndicatorRule(MACD, signal)
                .and((new CrossedUpIndicatorRule(RSI, 90)).or(new CrossedDownIndicatorRule(RSI, 50)))
                .or(new StopLossRule(closePrice, series.numOf(2))
                        .or(new StopGainRule(closePrice, series.numOf(3))));

        // Running our juicy trading strategy...
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(new BaseStrategy(buyingRule, sellingRule));
        System.out.println("Number of positions for our strategy: " + tradingRecord.getPositionCount());

        // Analysis

        // Getting the winning positions ratio
        AnalysisCriterion winningPositionsRatio = new WinningPositionsRatioCriterion();

        System.out.println("Winning positions ratio: " + winningPositionsRatio.calculate(series, tradingRecord));
        // Getting a risk-reward ratio
        AnalysisCriterion romad = new ReturnOverMaxDrawdownCriterion();
        System.out.println("Return over Max Drawdown: " + romad.calculate(series, tradingRecord));

        // Total return of our strategy vs total return of a buy-and-hold strategy
        AnalysisCriterion vsBuyAndHold = new VersusBuyAndHoldCriterion(new GrossReturnCriterion());
        System.out.println("Our return vs buy-and-hold return: " + vsBuyAndHold.calculate(series, tradingRecord));

        // Your turn!
    }
}