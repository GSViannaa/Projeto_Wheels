package com.ProjetoWheels.chat;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            var msg = update.getMessage();
            var user = msg.getFrom();
            Long chatId = msg.getChatId();

            System.out.println(user.getFirstName() + ": " + msg.getText());

            SendMessage mensagem = new SendMessage();
            mensagem.setChatId(chatId.toString());
            mensagem.setText("Bem-vindo ao Biketron 3000! \nClique abaixo para escolher o tipo de bicicleta:");

            boolean botaoFoiClicado = false;

            InlineKeyboardButton escolherTipos = new InlineKeyboardButton();
            escolherTipos.setText("Escolher tipos");
            escolherTipos.setCallbackData("ESCOLHER_TIPO");

            InlineKeyboardButton ajuda = new InlineKeyboardButton();
            ajuda.setText("Ajuda");
            ajuda.setCallbackData("AJUDA");

            InlineKeyboardMarkup teclado = new InlineKeyboardMarkup();

            if (update.hasCallbackQuery()) botaoFoiClicado = true;
            if(botaoFoiClicado) listarTiposBike();

            teclado.setKeyboard(List.of(List.of(escolherTipos), List.of(ajuda)));

            mensagem.setReplyMarkup(teclado);


            try {
                execute(mensagem);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        else if (update.hasCallbackQuery()) {
            var callbackQuery = update.getCallbackQuery();
            var data = callbackQuery.getData(); // ex: "ESCOLHER_TIPO"
            var chatId = callbackQuery.getMessage().getChatId();

            String resposta;

            switch (data) {
                case "ESCOLHER_TIPO":
                    resposta = "Você escolheu ver os tipos de bicicleta!";
                    break;
                case "AJUDA":
                    resposta = """
                            - Primeiro Passo: Clique em 'Escolher tipos'\n\
                            
                            - Segundo passo: Selecione o tipo de bicicleta\n\
                            
                            - Terceiro passo: Escolha a bicicleta\n\
                            
                            - Quarto passo: Estabeleça o período do aluguel\n\
                            
                            - Quinto passo: Defina o método de pagamento\n\
                            
                            """;
                    break;
                default:
                    resposta = "Opção desconhecida.";
            }

            SendMessage respostaMsg = new SendMessage();
            respostaMsg.setChatId(chatId.toString());
            respostaMsg.setText(resposta);

            try {
                execute(respostaMsg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
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
    public void listarTiposBike(){
        InlineKeyboardButton defaultBike = new InlineKeyboardButton();
        defaultBike.setText("Bicicleta Urbana");
        defaultBike.setCallbackData("DEFAULT_BIKE");

        InlineKeyboardButton mountainBike = new InlineKeyboardButton();
        mountainBike.setText("Mountain Bike");
        mountainBike.setCallbackData("MOUNTAIN_BIKE");

        InlineKeyboardButton speedBike = new InlineKeyboardButton();
        speedBike.setText("Speed Bike");
        speedBike.setCallbackData("SPEED_BIKE");

        InlineKeyboardButton childrensBike = new InlineKeyboardButton();
        childrensBike.setText("Bicicleta Infantil");
        childrensBike.setCallbackData("CHILDRENS_BIKE");
    }
}
