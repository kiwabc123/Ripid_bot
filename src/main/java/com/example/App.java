package com.example;
import java.io.IOException;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
/**
 * Hello world!
 *
 */
public class App 
{
  
    public static void main( String[] args ) throws IOException
    {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new bot());
            System.out.println("Bot Started Successfully");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
