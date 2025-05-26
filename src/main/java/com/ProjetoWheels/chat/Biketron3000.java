package com.ProjetoWheels.chat;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

public class Biketron3000 extends TelegramLongPollingBot
{

    @Override
    public void onUpdateReceived(Update update)
    {
        var msg = update.getMessage();
        var user = msg.getFrom();
        Long chatId = msg.getChatId();

        System.out.println(user.getFirstName() + ": " + msg.getText());

        SendMessage mensagem = new SendMessage();
        mensagem.setChatId(chatId.toString());
        mensagem.setText("Bem-vindo ao Biketron 3000! Clique abaixo para escolher o tipo de bicicleta:");

        InlineKeyboardButton botao = new InlineKeyboardButton();
        botao.setText("Escolher tipos");
        botao.setCallbackData("ESCOLHER_TIPO");

        InlineKeyboardMarkup teclado = new InlineKeyboardMarkup();

        teclado.setKeyboard(List.of(List.of(botao)));

        mensagem.setReplyMarkup(teclado);

        try
        {
            execute(mensagem);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotToken() {
        return "7880913189:AAHrZgQYS6r_pvh-APAQZXE8V26aEATdLmw";
    }

    @Override
    public String getBotUsername() {
        return "Biketron3000";
    }

    public void sendText(Long who, String what)
    {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what).build();
        try
        {
            execute(sm);
        }
        catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }

}
