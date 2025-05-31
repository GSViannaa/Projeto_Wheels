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
            processarMensagemDeTexto(update.getMessage());
        }

        if (update.hasCallbackQuery())
        {
            processarCallback(update.getCallbackQuery());
        }
    }
      // =============================
     //  Processamento  mensagem
    // =============================

    private void processarMensagemDeTexto(Message msg)
    {
        Long chatId = msg.getChatId();
        SendMessage mensagem = criarMensagemInicial(chatId);
        enviarMensagem(mensagem);
    }

     // =============================
    //  Processamento  callback
   // =============================

    private void processarCallback(CallbackQuery callbackQuery)
    {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();


         if (data.equals("ESCOLHER_TIPO"))
        {
            enviarMenuTiposDeBike(chatId);
        }
        else if (data.startsWith("ESCOLHER_MODELO_"))
        {
            processarEscolhaDeModelo(chatId, data);
        }
        else if (isTipoDeBike(data))
        {
            enviarMenuModelosDeBike(chatId, data);
        }
        else if (data.equals("AJUDA")) {
            String respostaAjuda = gerarMensagemAjuda();
            enviarMensagemSimples(chatId, respostaAjuda);
        }
        else {
            enviarMensagemSimples(chatId, "Opção desconhecida.");
        }

    }

     // =============================
    //  Lógica tipos de bikes
   // =============================
    private boolean isTipoDeBike(String data)
    {
        return data.contains("MountainBikes") || data.equals("SpeedBikes") || data.equals("DefaultBikes") || data.equals("ChildrensBikes");
    }

    // =============================
   //  Processar escolha de modelos
  // =============================
    private void processarEscolhaDeModelo(Long chatId, String data)
    {
        String modelo = data.replace("ESCOLHER_MODELO_", "");

        List<Bikes> bikes = retornarDetalhesDoModelo(modelo);

        String textoMensagem = mensagemDetalhesModeloEscolhido(bikes);
        String tipoDaBike = bikes.isEmpty() ? "" : bikes.get(0).getClass().getSimpleName();

        SendMessage mensagem = new SendMessage();
        mensagem.setChatId(chatId.toString());
        mensagem.setText(textoMensagem);
        mensagem.enableMarkdown(true);

        InlineKeyboardMarkup teclado = menuReumoDaBike(modelo, tipoDaBike);
        mensagem.setReplyMarkup(teclado);

        enviarMensagem(mensagem);

    }
    // =============================
   //  Menus e mensagens
  // =============================

    private  InlineKeyboardMarkup menuReumoDaBike(String modelo, String tipo)
    {
        InlineKeyboardButton botaoConfirmar = new InlineKeyboardButton();
        botaoConfirmar.setText("✅ Confirmar");
        botaoConfirmar.setCallbackData("CONFIRMAR_MODELO" + modelo);

        InlineKeyboardButton botaoVoltar = new InlineKeyboardButton();
        botaoVoltar.setText("🔙 Voltar");
        botaoVoltar.setCallbackData("ESCOLHER_TIPO" + tipo);

        List<List<InlineKeyboardButton>> linhas = List.of(List.of(botaoConfirmar), List.of(botaoVoltar));
        InlineKeyboardMarkup teclado = new InlineKeyboardMarkup();
        teclado.setKeyboard(linhas);

        return teclado;
    }

    private String mensagemDetalhesModeloEscolhido(List<Bikes> listaBikes)
    {
        if (listaBikes.isEmpty()) return "Nenhuma bicicleta desse modelo disponível no momento";

        Bikes primeiraBike = listaBikes.get(0);
        String modelo = primeiraBike.getModelo();
        String cor = primeiraBike.getCor();
        double preco = primeiraBike.calcularPreco();

        int quantidadeDisponivel = listaBikes.size();

        return String.format("""
            *Informações do Modelo:*
            • Modelo: %s
            • Cor: %s
            • Preço por dia: R$ %.2f
            • Quantidade disponível: %d
            """,
                modelo, cor, preco, quantidadeDisponivel
        );
    }


    private void enviarMenuTiposDeBike(Long chatId)
    {
        SendMessage mensagem = new SendMessage();
        mensagem.setChatId(chatId.toString());
        mensagem.setText("Tipo de bicicleta:");
        mensagem.setReplyMarkup(criarTecladoTiposDeBike());
        enviarMensagem(mensagem);
    }

    private void enviarMenuModelosDeBike(Long chatId, String tipo)
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
    // =============================
   //  Mensagem inicial
  // =============================

    private static SendMessage criarMensagemInicial(Long chatId)
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
    // =============================
   //  Teclado tipos de bike
  // =============================

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

    // =============================
   //  Mensagem de ajuda
  // =============================

    public String gerarMensagemAjuda()
    {
        return  """
                1️⃣ Clique em 'Escolher tipos'
                2️⃣ Selecione o tipo de bicicleta
                3️⃣ Escolha a bicicleta
                4️⃣ Estabeleça o período do aluguel
                5️⃣ Defina o método de pagamento
                     """;
    }
    // =============================
   //  Acesso ao SQL
  // =============================

    public List<String> retornarBikesPortipo(String data)
    {
        List<Bikes> bikesFiltradas = BikesDAO.ListarBikesPorTipo(data);
        List<String> modelo = new ArrayList();

        for (Bikes bike: bikesFiltradas)
        {
           modelo.add(bike.getModelo());
        }
        return modelo;
    }

    public List<Bikes> retornarDetalhesDoModelo(String data)
    {
        List<Bikes> bikesFiltradas = BikesDAO.buscarBikesPorModelo(data);

        return  bikesFiltradas;
    }
}
