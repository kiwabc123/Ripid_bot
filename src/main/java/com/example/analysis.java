package com.example;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.LinearTransactionCostCriterion;
import org.ta4j.core.analysis.criteria.ReturnOverMaxDrawdownCriterion;
import org.ta4j.core.analysis.criteria.VersusBuyAndHoldCriterion;
import org.ta4j.core.analysis.criteria.WinningPositionsRatioCriterion;
import org.ta4j.core.analysis.criteria.pnl.GrossReturnCriterion;
import org.ta4j.core.analysis.criteria.pnl.ProfitLossCriterion;
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

public class analysis {

    public static void main(String[] args) throws InterruptedException {
        BarSeries series = new BuildBarSeries().buildAndAddData();
        
        Strategy strategy = createtrategy(series);
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy, Trade.TradeType.BUY,
                DecimalNum.valueOf(100));
        System.out.println(tradingRecord);

        AnalysisCriterion criterion = new GrossReturnCriterion();
        Num calculate3DaySma = criterion.calculate(series, tradingRecord);

        System.out.println("cal :" + calculate3DaySma);

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
     
        
    }

 

    private static ZonedDateTime CreateDay(int day) {
        return ZonedDateTime.of(2018, 01, day, 12, 0, 0, 0, ZoneId.systemDefault());
    }

    public static Strategy createtrategy(BarSeries series) {

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
   

        MACDIndicator macd = new MACDIndicator(closePrice, 12, 26);
        
    

        RSIIndicator RSI = new RSIIndicator(closePrice, 14);
        EMAIndicator signal = new EMAIndicator(macd, 9);

        Rule buyingRule = new CrossedUpIndicatorRule(macd, signal) .and(new CrossedUpIndicatorRule(RSI, 50));

        Rule sellingRule = new CrossedDownIndicatorRule(macd, signal)
                .and((new CrossedUpIndicatorRule(RSI, 90)).or(new CrossedDownIndicatorRule(RSI, 50)))
                .or(new StopLossRule(closePrice, series.numOf(2))
                        .or(new StopGainRule(closePrice, series.numOf(3))));

        return new BaseStrategy(buyingRule, sellingRule);
    }

    private static Strategy create2DaySmaStrategy(BarSeries series) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        SMAIndicator sma = new SMAIndicator(closePrice, 2);
        return new BaseStrategy(new UnderIndicatorRule(sma, closePrice), new OverIndicatorRule(sma, closePrice));
    }
}