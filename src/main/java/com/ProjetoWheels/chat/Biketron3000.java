package com.ProjetoWheels.chat;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Biketron3000 extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        System.out.println(user.getFirstName() + ": " + msg.getText());;
    }
    @Override
    public String getBotToken() {
        return "7880913189:AAHrZgQYS6r_pvh-APAQZXE8V26aEATdLmw";
    }

    @Override
    public String getBotUsername() {
        return "Biketron3000";
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) // Pra quem a mensagem está sendo mandada
                .text(what).build();    // Conteúdo da mensagem
        try {
            execute(sm);                        // Realmente mandando a mensagem
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws TelegramApiException {
        Biketron3000 bot = new Biketron3000();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Biketron3000());
        bot.sendText(7965199255L, "Olá, sou o Biketron3000 e estou aqui para ajudá-lo! " +
                "\nVocê pode usar os comandos abaixo para ajudar no aluguel da sua bike. \n" +
                "\n/alugar" +
                "\n/lista");
    }
}
