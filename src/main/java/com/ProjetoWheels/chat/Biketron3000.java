package com.ProjetoWheels.chat;
import com.ProjetoWheels.bot.CallbackHandler;
import com.ProjetoWheels.bot.MessageHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


public class Biketron3000 extends TelegramLongPollingBot {

    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;

    public Biketron3000() {
        this.messageHandler = new MessageHandler();
        this.callbackHandler =  new CallbackHandler(messageHandler);;
    }


    @Override
    public String getBotToken() {
        return "7880913189:AAHrZgQYS6r_pvh-APAQZXE8V26aEATdLmw";
    }

    @Override
    public String getBotUsername() {
        return "Biketron3000";
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText()) messageHandler.handleMessage(update.getMessage(), this);

        if (update.hasCallbackQuery())  callbackHandler.handleCallback(update.getCallbackQuery(), this);
    }
}