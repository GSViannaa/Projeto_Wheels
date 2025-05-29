package com.ProjetoWheels.chat;
import com.ProjetoWheels.DAO.BikesDAO;
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


public class Biketron3000 extends TelegramLongPollingBot
{

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

        if (data.startsWith("ESCOLHER_MODELO_"))
        {
            String modeloEscolhido = data.replace("ESCOLHER_MODELO_", "");
            enviarMensagemSimples(chatId, "Você escolheu o modelo: " + modeloEscolhido);

            return;
        }

        switch (data) {
            case "ESCOLHER_TIPO":
                enviarMensagemDeEscolhaTipo(chatId);
                break;


            case "MountainBikes":
            case "SpeedBikes":
            case "DefaultBikes":
            case "ChildrensBikes":
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
        List<String> modelos = retornarBikesPortipo(tipo);

        SendMessage mensagem = new SendMessage();
        mensagem.setChatId(chatId.toString());
        mensagem.setText("Escolha uma bicicleta:");

        List<List<InlineKeyboardButton>> linhas = new ArrayList<>();

        for (String modelo : modelos)
        {
            InlineKeyboardButton botao = new InlineKeyboardButton();
            botao.setText(modelo);
            botao.setCallbackData("ESCOLHER_MODELO_" + modelo);

            linhas.add(List.of(botao));
        }

        InlineKeyboardMarkup teclado = new InlineKeyboardMarkup();
        teclado.setKeyboard(linhas);
        mensagem.setReplyMarkup(teclado);

        enviarMensagem(mensagem);
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
        try
        {
            execute(mensagem);
        }
        catch (TelegramApiException e)
        {
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
        List<Bikes> bikesFiltradas = BikesDAO.ListarBikesPorTipo(callback);
        List<String> modelo = new ArrayList();


        for (Bikes bike: bikesFiltradas)
        {
           modelo.add(bike.getModelo());
        }
        return modelo;
    }


}
