package com.example;


import java.io.IOException;

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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.ConvertibleBaseBarBuilder;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.DoubleNum;

public class BuildBarSeries {

    /**
     * Calls different functions that shows how a BaseBarSeries could be created and
     * how Bars could be added
     *
     * @param args command line arguments (ignored)
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        BarSeries a = buildAndAddData();
        System.out.println("a: " + a.getBar(0).getClosePrice().getName());
        BaseBarSeriesBuilder.setDefaultFunction(DoubleNum::valueOf);
        a = buildAndAddData();
        System.out.println("a: " + a.getBar(0).getClosePrice().getName());
        System.out.println(a);
        // BarSeries b = buildWithDouble();
        // BarSeries c = buildWithBigDecimal();
        // BarSeries d = buildManually();
        // BarSeries e = buildManuallyDoubleNum();
        // BarSeries f = buildManuallyAndAddBarManually();
        // BarSeries g = buildAndAddBarsFromList();
        for (int i = 0; i < a.getBarCount(); i++) {
                System.out.println("Bar :"+i);
                System.out.println(a.getBar(i));

        }
    }

    public static BarSeries buildAndAddData() {
        BarSeries series = new BaseBarSeriesBuilder().withName("mySeries").build();
        //801A1124-8B21-4690-8C36-A85D3D892325
        //48A0C81A-3E04-4916-A388-ECD3B991C3DC
 REST_methods rest = new REST_methods("48A0C81A-3E04-4916-A388-ECD3B991C3DC");

        // ZonedDateTime endTime = ZonedDateTime.now();
       
       
        
        try {
            Period_identifier period = Period_identifier._1HRS;
      
            Timedata[] dataa = rest.ohlcv_get_latest_timeseries("BINANCE_SPOT_BTC_USDC", period);

            
    
            ZonedDateTime endTime = ZonedDateTime.now();
      
      
            for (int i = dataa.length-1; i >=0; i--) {
                // System.out.println(dataa[i].get_time_period_start());
                // System.out.println(dataa[i].get_time_period_end());
                // System.out.println(dataa[i].get_time_open());
                double price_open=dataa[i].get_price_open();
                double price_high=dataa[i].get_price_high();
                double price_low=dataa[i].get_price_low();
            double price_close=dataa[i].get_price_close();
             double volume_traded=dataa[i].get_volume_traded();

                //  System.out.println(dataa[i].get_price_open());
                // System.out.println(dataa[i].get_price_high());
                // System.out.println(dataa[i].get_price_low());
                // System.out.println(dataa[i].get_price_close());
                // System.out.println(dataa[i].get_volume_traded());
                series.addBar(endTime.plusHours(dataa.length-i), price_open, price_high, price_low, price_close, volume_traded);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // ...
        return series;
    }

        public static BarSeries buildAndAddData2() {
        BarSeries series = new BaseBarSeriesBuilder().withName("mySeries").build();
        //801A1124-8B21-4690-8C36-A85D3D892325
        //48A0C81A-3E04-4916-A388-ECD3B991C3DC
 REST_methods rest = new REST_methods("48A0C81A-3E04-4916-A388-ECD3B991C3DC");

        // ZonedDateTime endTime = ZonedDateTime.now();
       
       
        
        try {
            Period_identifier period = Period_identifier._1HRS;
      
            Timedata[] dataa = rest.ohlcv_get_latest_timeseries("BINANCE_SPOT_ETH_USDC", period);

            
    
            ZonedDateTime endTime = ZonedDateTime.now();
      
      
            for (int i = dataa.length-1; i >=0; i--) {
                // System.out.println(dataa[i].get_time_period_start());
                // System.out.println(dataa[i].get_time_period_end());
                // System.out.println(dataa[i].get_time_open());
                double price_open=dataa[i].get_price_open();
                double price_high=dataa[i].get_price_high();
                double price_low=dataa[i].get_price_low();
            double price_close=dataa[i].get_price_close();
             double volume_traded=dataa[i].get_volume_traded();

                //  System.out.println(dataa[i].get_price_open());
                // System.out.println(dataa[i].get_price_high());
                // System.out.println(dataa[i].get_price_low());
                // System.out.println(dataa[i].get_price_close());
                // System.out.println(dataa[i].get_volume_traded());
                series.addBar(endTime.plusHours(dataa.length-i), price_open, price_high, price_low, price_close, volume_traded);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // ...
        return series;
    }


  

    private static ConvertibleBaseBarBuilder<String> barBuilderFromString() {
        return BaseBar.builder(DoubleNum::valueOf, String.class);
    }
}