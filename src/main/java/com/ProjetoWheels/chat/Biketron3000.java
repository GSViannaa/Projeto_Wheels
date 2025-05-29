package com.ProjetoWheels.chat;
import com.ProjetoWheels.model.Bikes;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.ProjetoWheels.DAO.BikesDAO.ListarBikesPorTipo;

public class Biketron3000 extends TelegramLongPollingBot
{

    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage())
        {
            processarMensagem(update.getMessage());
        }

        if (update.hasCallbackQuery())
        {
            processarCallback(update.getCallbackQuery());
        }
    }

    private void processarMensagem(Message msg)
    {
        Long chatId = msg.getChatId();
        SendMessage mensagem = mensagemInicial(chatId);
        enviarMensagem(mensagem);
    }

    private void processarCallback(CallbackQuery callbackQuery)
    {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();

        switch (data) {
            case "ESCOLHER_TIPO":
                enviarMensagemDeEscolhaTipo(chatId);
                enviarBikesPorTipo(chatId, data);
                break;

            case "AJUDA":
                String respostaAjuda = caseBotaoAjudaFoiClicado();
                enviarMensagemSimples(chatId, respostaAjuda);
                break;

            default:
                enviarMensagemSimples(chatId, "Opção desconhecida.");
        }
    }

    private void enviarMensagemDeEscolhaTipo(Long chatId)
    {
        SendMessage mensagem = new SendMessage();
        mensagem.setChatId(chatId.toString());
        mensagem.setText("Tipo de bicicleta:");
        mensagem.setReplyMarkup(criarTecladoTiposDeBike());
        enviarMensagem(mensagem);
    }

    private void enviarBikesPorTipo(Long chatId, String tipo)
    {
        List<String> bikes = retornarBikesPortipo(tipo);
        StringBuilder sb = new StringBuilder("Bicicletas disponíveis:\n");
        for (String bike : bikes) {
            sb.append("- ").append(bike).append("\n");
        }
        enviarMensagemSimples(chatId, sb.toString());
    }

    private void enviarMensagemSimples(Long chatId, String texto)
    {
        SendMessage mensagem = new SendMessage();
        mensagem.setChatId(chatId.toString());
        mensagem.setText(texto);
        enviarMensagem(mensagem);
    }

    private void enviarMensagem(SendMessage mensagem)
    {
        try {
            execute(mensagem);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    private static SendMessage mensagemInicial(Long chatId)
    {
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
        return mensagem;
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
        defaultBikeBotao.setCallbackData("DefaultBikes");

        InlineKeyboardButton mountainBikeBotao = new InlineKeyboardButton();
        mountainBikeBotao.setText("Mountain Bike");
        mountainBikeBotao.setCallbackData("MountainBikes");

        InlineKeyboardButton speedBikeBotao = new InlineKeyboardButton();
        speedBikeBotao.setText("Speed Bike");
        speedBikeBotao.setCallbackData("SpeedBikes");

        InlineKeyboardButton childrensBikeBotao = new InlineKeyboardButton();
        childrensBikeBotao.setText("Bicicleta Infantil");
        childrensBikeBotao.setCallbackData("ChildrensBikes");

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

    public List<String> retornarBikesPortipo(String callback)
    {
        List<Bikes> bikesFiltradas;
        List<String> bikesToSring = new ArrayList();

        bikesFiltradas = ListarBikesPorTipo(callback);

        for (Bikes bike: bikesFiltradas)
        {
           bikesToSring.add(bike.getModelo());
        }
        return bikesToSring;
    }


}
