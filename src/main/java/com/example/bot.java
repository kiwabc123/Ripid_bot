package com.example;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.ReturnOverMaxDrawdownCriterion;
import org.ta4j.core.analysis.criteria.VersusBuyAndHoldCriterion;
import org.ta4j.core.analysis.criteria.WinningPositionsRatioCriterion;
import org.ta4j.core.analysis.criteria.pnl.AverageProfitCriterion;
import org.ta4j.core.analysis.criteria.pnl.GrossProfitCriterion;
import org.ta4j.core.analysis.criteria.pnl.GrossReturnCriterion;
import org.ta4j.core.analysis.criteria.pnl.NetProfitCriterion;
import org.ta4j.core.analysis.criteria.pnl.ProfitLossCriterion;
import org.ta4j.core.Trade;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage.SendMessageBuilder;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.RestApi;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.vdurmont.emoji.EmojiParser;

public class bot extends TelegramLongPollingBot {
    private String money_emoji = EmojiParser.parseToUnicode("💰");
    private boolean EntryOrExit;

    @Override
    public void onUpdateReceived(Update update) {
        // TODO Auto-generated method stub
        if (update.hasMessage()) {

            if (update.getMessage().getText().equals("/start")) {

                String chatId = update.getMessage().getChatId().toString();
                String text = update.getMessage().getFrom().getUserName();
                SendMessage message = new SendMessage(chatId,
                        "Hi " + text + "\n" + "what cryptocurrency that you wanna trade");

                InputFile file = new InputFile("https://t.me/AnimatedStickers/462");
                SendSticker sticker = new SendSticker(chatId, file);

                // inline
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                List<InlineKeyboardButton> inlineKeyboardButtonlist = new ArrayList<>();
                InlineKeyboardButton Button1 = new InlineKeyboardButton();
                InlineKeyboardButton Button2 = new InlineKeyboardButton();
                Button1.setText("BTC");
                Button2.setText("ETH");
                Button1.setCallbackData("But1");
                Button2.setCallbackData("But2");
                inlineKeyboardButtonlist.add(Button1);
                inlineKeyboardButtonlist.add(Button2);

                inlineButtons.add(inlineKeyboardButtonlist);
                inlineKeyboardMarkup.setKeyboard(inlineButtons);
                message.setReplyMarkup(inlineKeyboardMarkup);

                try {
                    execute(message);
                    execute(sticker);
                } catch (TelegramApiException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            } else if (update.getMessage().getText().equals("/stop")) {

              
                EntryOrExit = false;
                System.out.println("stopppppp");
            }

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callback = update.getCallbackQuery();
            String data = callback.getData();
            Message chatId = update.getCallbackQuery().getMessage();
            SendMessage info = new SendMessage();
            SendMessage info2 = new SendMessage();
            info.setChatId(chatId.getChatId().toString());
            info2.setChatId(chatId.getChatId().toString());

            InputFile file = new InputFile("https://t.me/AnimatedStickers/325");
            SendSticker sticker2 = new SendSticker(chatId.getChatId().toString(), file);

            // BTC button
            if (data.equals("But1")) {
                System.out.println("btc");
                System.out.println("user " + update.getCallbackQuery().getFrom().getUserName());
                try {

                    REST_methods rest = new REST_methods("B29D27FF-44C9-4765-AA16-9245A79AAD07");
                    String text2 = null;

                    info.setText("*BTC  /  USD*");
                    info.enableMarkdown(true);

                    // Trade[] t = rest.trades_get_latest_data("BINANCE_SPOT_USDC_BTC");
                    // int tc = t.length;
                    // System.out.println(tc);

                    Exchange_rate exchange = rest.get_exchange_rate("BTC", "USD");
                    System.out.println(exchange.get_rate());
                    text2 = "rate : " + exchange.get_rate();

                    info2.setText(text2);

                    // inline
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonlist = new ArrayList<>();
                    InlineKeyboardButton Button1 = new InlineKeyboardButton();
                    InlineKeyboardButton Button2 = new InlineKeyboardButton();
                    Button1.setText("Analysis");
                    Button2.setText("Back");
                    Button1.setCallbackData("analysis_btc");
                    Button2.setCallbackData("back");
                    inlineKeyboardButtonlist.add(Button1);
                    inlineKeyboardButtonlist.add(Button2);

                    inlineButtons.add(inlineKeyboardButtonlist);
                    inlineKeyboardMarkup.setKeyboard(inlineButtons);
                    info.setReplyMarkup(inlineKeyboardMarkup);
                    execute(info);
                    execute(info2);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (TelegramApiException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // ETH button
            } else if (data.equals("But2")) {
                try {

                    REST_methods rest = new REST_methods("B29D27FF-44C9-4765-AA16-9245A79AAD07");
                    String text2 = null;

                    info.setText("*ETH  /  USD*");
                    info.enableMarkdown(true);

                    // Trade[] t = rest.trades_get_latest_data("BINANCE_SPOT_USDC_BTC");
                    // int tc = t.length;
                    // System.out.println(tc);

                    Exchange_rate exchange = rest.get_exchange_rate("ETH", "USD");
                    System.out.println(exchange.get_rate());
                    text2 = "rate : " + exchange.get_rate();

                    info2.setText(text2);

                    // inline
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonlist = new ArrayList<>();
                    InlineKeyboardButton Button1 = new InlineKeyboardButton();
                    InlineKeyboardButton Button2 = new InlineKeyboardButton();
                    Button1.setText("Analysis");
                    Button2.setText("Back");
                    Button1.setCallbackData("analysis_eth");
                    Button2.setCallbackData("back");
                    inlineKeyboardButtonlist.add(Button1);
                    inlineKeyboardButtonlist.add(Button2);

                    inlineButtons.add(inlineKeyboardButtonlist);
                    inlineKeyboardMarkup.setKeyboard(inlineButtons);
                    info.setReplyMarkup(inlineKeyboardMarkup);
                    execute(info);
                    execute(info2);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (TelegramApiException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (data.equals("back")) {

            } else if (data.equals("analysis_btc")) {

                BarSeries series = new BuildBarSeries().buildAndAddData();
                Strategy strategy = analysis.createtrategy(series);
                BarSeriesManager seriesManager = new BarSeriesManager(series);
                TradingRecord tradingRecord = seriesManager.run(strategy, Trade.TradeType.BUY,
                        DecimalNum.valueOf(50));
                System.out.println(tradingRecord);

                // analysis
                AnalysisCriterion criterion = new GrossReturnCriterion();
                Num calculate3DaySma = criterion.calculate(series, tradingRecord);

                String text1, text2, text3, text4 = null;

                text1 = "Number of positions for our strategy: " + tradingRecord.getPositionCount();

                // Analysis

                // Getting the winning positions ratio
                AnalysisCriterion winningPositionsRatio = new WinningPositionsRatioCriterion();

                text2 = "Winning positions ratio :" + winningPositionsRatio.calculate(series, tradingRecord);
                // Getting a risk-reward ratio
                AnalysisCriterion romad = new ReturnOverMaxDrawdownCriterion();
                text3 = "Return over Max Drawdown:  " + romad.calculate(series, tradingRecord);

                // Total return of our strategy vs total return of a buy-and-hold strategy
                AnalysisCriterion vsBuyAndHold = new VersusBuyAndHoldCriterion(new GrossReturnCriterion());
                text4 = ("Our return vs buy-and-hold return: \n" + vsBuyAndHold.calculate(series, tradingRecord));

                System.out.println(text1 + "\n" + text2 + "\n \n" + text3 + "\n \n" + text4);
                SendMessage ananlize = new SendMessage();
                SendMessage header = new SendMessage();
                header.setChatId(chatId.getChatId().toString());
                header.setText("*Analiysis for BTC *");

                ananlize.setChatId(chatId.getChatId().toString());
                ananlize.setText(text1 + "\n" + text2 + "\n" + text3 + "\n" + text4);
                header.enableMarkdown(true);

                // inline
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                List<InlineKeyboardButton> inlineKeyboardButtonlist = new ArrayList<>();
                InlineKeyboardButton Button1 = new InlineKeyboardButton();
                InlineKeyboardButton Button2 = new InlineKeyboardButton();
                Button1.setText("Trade");
                Button2.setText("Live");
                Button1.setCallbackData("trade");
                Button2.setCallbackData("live");
                inlineKeyboardButtonlist.add(Button1);
                inlineKeyboardButtonlist.add(Button2);

                inlineButtons.add(inlineKeyboardButtonlist);
                inlineKeyboardMarkup.setKeyboard(inlineButtons);
                ananlize.setReplyMarkup(inlineKeyboardMarkup);

                try {
                    execute(sticker2);
                    execute(header);
                    execute(ananlize);
                } catch (TelegramApiException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (data.equals("trade")) {

                SendMessage Message = new SendMessage();
                Message.setChatId(chatId.getChatId().toString());

                BarSeries series = new BuildBarSeries().buildAndAddData();
                Strategy strategy = analysis.createtrategy(series);
                TradingRecord tradingRecord = new BaseTradingRecord();

                for (int i = 0; i < series.getBarCount(); i++) {

                    Bar newBar = series.getBar(i);

                    // try {
                    // TimeUnit.SECONDS.sleep(1);
                    // } catch (InterruptedException e1) {
                    // // TODO Auto-generated catch block
                    // e1.printStackTrace();
                    // }

                    int endIndex = i;

                    System.out.println(endIndex);
                    if (strategy.shouldEnter(endIndex)) {
                        // Our strategy should enter
                        System.out.println("Strategy should ENTER on " + endIndex);
                        boolean entered = tradingRecord.enter(endIndex, newBar.getClosePrice(),
                                DecimalNum.valueOf(100));
                        if (entered) {
                            Trade entry = tradingRecord.getLastEntry();
                            System.out.println(
                                    "Entered on " + entry.getIndex() + " (price=" + entry.getNetPrice().doubleValue()
                                            + ", amount=" + entry.getAmount().doubleValue() + ")");
                            String Entry = "Entered on " + entry.getIndex() + " (price="
                                    + entry.getNetPrice().doubleValue()
                                    + ", amount=" + entry.getAmount().doubleValue() + ")";

                            Message.setText(Entry);

                            try {
                                execute(Message);
                            } catch (TelegramApiException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    } else if (strategy.shouldExit(endIndex)) {
                        // Our strategy should exit
                        System.out.println("Strategy should EXIT on " + endIndex);
                        boolean exited = tradingRecord.exit(endIndex, newBar.getClosePrice(), DecimalNum.valueOf(100));
                        if (exited) {
                            Trade exit = tradingRecord.getLastExit();
                            System.out.println(
                                    "Exited on " + exit.getIndex() + " (price=" + exit.getNetPrice().doubleValue()
                                            + ", amount=" + exit.getAmount().doubleValue() + ")");

                            String Exit = "Exited on " + exit.getIndex() + " (price=" + exit.getNetPrice().doubleValue()
                                    + ", amount=" + exit.getAmount().doubleValue() + ")";

                            Message.setText(Exit);

                            try {
                                execute(Message);
                            } catch (TelegramApiException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    }

                }

                Num Network = new GrossProfitCriterion().calculate(series, tradingRecord);
                Message.setText("*GrossProfitCriterion :*" + Network.toString() + money_emoji);
                Message.enableMarkdown(true);
                try {
                    execute(Message);
                } catch (TelegramApiException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (data.equals("live")) {
                SendMessage Message = new SendMessage();
                Message.setChatId(chatId.getChatId().toString());

                BarSeries series = new BuildBarSeries().buildAndAddData();
                Strategy strategy = analysis.createtrategy(series);
                TradingRecord tradingRecord = new BaseTradingRecord();
                Bar newBar = series.getLastBar();
                int endIndex = series.getEndIndex();
                Timer t = new Timer();
                EntryOrExit=true;
                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        if(EntryOrExit) {
                        Message.setText("****Waiting for the next hour****");
                        System.out.println(endIndex);
                        if (strategy.shouldEnter(endIndex)) {
                            // Our strategy should enter
                            System.out.println("Strategy should ENTER on " + endIndex);
                            boolean entered = tradingRecord.enter(endIndex, newBar.getClosePrice(),
                                    DecimalNum.valueOf(100));
                            if (entered) {
                                Trade entry = tradingRecord.getLastEntry();
                                System.out.println(
                                        "Entered on " + entry.getIndex() + " (price="
                                                + entry.getNetPrice().doubleValue()
                                                + ", amount=" + entry.getAmount().doubleValue() + ")");
                                String Entry = "Entered on " + entry.getIndex() + " (price="
                                        + entry.getNetPrice().doubleValue()
                                        + ", amount=" + entry.getAmount().doubleValue() + ")";

                                Message.setText(Entry);

                            }
                        } else if (strategy.shouldExit(endIndex)) {
                            // Our strategy should exit
                            System.out.println("Strategy should EXIT on " + endIndex);
                            boolean exited = tradingRecord.exit(endIndex, newBar.getClosePrice(),
                                    DecimalNum.valueOf(100));
                            if (exited) {
                                Trade exit = tradingRecord.getLastExit();
                                System.out.println(
                                        "Exited on " + exit.getIndex() + " (price=" + exit.getNetPrice().doubleValue()
                                                + ", amount=" + exit.getAmount().doubleValue() + ")");

                                String Exit = "Exited on " + exit.getIndex() + " (price="
                                        + exit.getNetPrice().doubleValue()
                                        + ", amount=" + exit.getAmount().doubleValue() + ")";

                                Message.setText(Exit);

                            }
                        }
                        try {
                            execute(Message);
                        } catch (TelegramApiException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        t.cancel();
                        t.purge();
                     }
                    };
                };
                t.schedule(tt, 1000, 3600000);

            }

        }
    }

    @Override
    public String getBotUsername() {
        // TODO Auto-generated method stub
        return "Ripid_Trading_Bot.";
    }

    @Override
    public String getBotToken() {
        // TODO Auto-generated method stub
        return "2109646286:AAGZZbV9kH0hd4W5rfoZ7Pjbm54nebLtB6g";
    }

}
