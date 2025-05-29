package com.ProjetoWheels.chat;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class Biketron3000 extends TelegramLongPollingBot
{

    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText())
        {
            var msg = update.getMessage();
            var user = msg.getFrom();
            Long chatId = msg.getChatId();

            System.out.println(user.getFirstName() + ": " + msg.getText());

            SendMessage mensagem = new SendMessage();
            mensagem.setChatId(chatId.toString());
            mensagem.setText("Bem-vindo ao Biketron 3000! \nClique abaixo para escolher o tipo de bicicleta:");


            InlineKeyboardButton escolherTipos = new InlineKeyboardButton();
            escolherTipos.setText("Escolher tipos");
            escolherTipos.setCallbackData("ESCOLHER_TIPO");

            InlineKeyboardButton ajuda = new InlineKeyboardButton();
            ajuda.setText("Ajuda");
            ajuda.setCallbackData("AJUDA");

            InlineKeyboardMarkup teclado = new InlineKeyboardMarkup();


            teclado.setKeyboard(List.of(List.of(escolherTipos), List.of(ajuda)));

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
        else if (update.hasCallbackQuery())
        {
            var callbackQuery = update.getCallbackQuery();
            var data = callbackQuery.getData();
            var chatId = callbackQuery.getMessage().getChatId();

            String resposta = "";

            SendMessage respostaMsg = new SendMessage();
            switch (data)
            {
                case "ESCOLHER_TIPO":
                    resposta = "Tipo de bicicleta:";

                    respostaMsg = new SendMessage();
                    respostaMsg.setChatId(chatId.toString());
                    respostaMsg.setText(resposta);

                    respostaMsg.setReplyMarkup(criarTecladoTiposDeBike());


                    break;

                case "AJUDA":
                     resposta = caseBotaoAjudaFoiClicado();

                    respostaMsg = new SendMessage();
                    respostaMsg.setChatId(chatId.toString());
                    respostaMsg.setText(resposta);

                    break;

                default:
                    resposta = "Opção desconhecida.";
            }


            try
            {
                execute(respostaMsg);
            }
            catch (TelegramApiException e)
            {
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
    public InlineKeyboardMarkup criarTecladoTiposDeBike()
    {
        InlineKeyboardMarkup tecladoTipos = new InlineKeyboardMarkup();

        InlineKeyboardButton defaultBikeBotao = new InlineKeyboardButton();
        defaultBikeBotao.setText("Bicicleta Urbana");
        defaultBikeBotao.setCallbackData("DEFAULT_BIKE");

        InlineKeyboardButton mountainBikeBotao = new InlineKeyboardButton();
        mountainBikeBotao.setText("Mountain Bike");
        mountainBikeBotao.setCallbackData("MOUNTAIN_BIKE");

        InlineKeyboardButton speedBikeBotao = new InlineKeyboardButton();
        speedBikeBotao.setText("Speed Bike");
        speedBikeBotao.setCallbackData("SPEED_BIKE");

        InlineKeyboardButton childrensBikeBotao = new InlineKeyboardButton();
        childrensBikeBotao.setText("Bicicleta Infantil");
        childrensBikeBotao.setCallbackData("CHILDRENS_BIKE");

        tecladoTipos.setKeyboard(List.of(List.of(defaultBikeBotao), List.of(childrensBikeBotao), List.of(speedBikeBotao),List.of(mountainBikeBotao)));

        return tecladoTipos;
    }

    public String caseBotaoAjudaFoiClicado()
    {
        return  """
                     - Primeiro Passo: Clique em 'Escolher tipos'\n\
               \s
                     - Segundo passo: Selecione o tipo de bicicleta\n\
               \s
                     - Terceiro passo: Escolha a bicicleta\n\
               \s
                    - Quarto passo: Estabeleça o período do aluguel\n\
               \s
                    - Quinto passo: Defina o método de pagamento\n\
                        \s
               \s""";
    }
}
