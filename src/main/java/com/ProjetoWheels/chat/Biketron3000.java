package com.ProjetoWheels.chat;
import com.ProjetoWheels.DAO.BikesDAO;
import com.ProjetoWheels.enums.usuarios.EstadoUsuario;
import com.ProjetoWheels.model.Bikes;
import com.ProjetoWheels.service.AluguelService;
import com.ProjetoWheels.service.ValidacaoService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;


public class Biketron3000 extends TelegramLongPollingBot
{

    private Map<Long, EstadoUsuario> estadosUsuario = new HashMap<>();
    private Map<Long, List<Bikes>> modelosEscolhidosPorUsuario = new HashMap<>();


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
        if (update.hasMessage() && update.getMessage().hasText())
        {
            processarMensagemDeTexto(update.getMessage());
        }

        if (update.hasCallbackQuery())
        {
            try
            {
                processarCallback(update.getCallbackQuery());
            }
            catch (TelegramApiException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
      // =============================
     //  Processamento mensagem
    // =============================

    private void processarMensagemDeTexto(Message msg)
    {
        Long chatId = msg.getChatId();
        String texto = msg.getText();

        EstadoUsuario estadoAtual = estadosUsuario.getOrDefault(chatId, EstadoUsuario.MENU_PRINCIPAL);

        switch (estadoAtual)
        {
            case MENU_PRINCIPAL ->
            {

                SendMessage mensagem = criarMensagemInicial(chatId);
                enviarMensagem(mensagem);
            }

            case AGUARDANDO_EMAIL ->
            {
                boolean valido = ValidacaoService.isValidEmail(texto);

                if (valido)
                {


                    enviarMensagemSimples(chatId, "✅ Email válido! Agora escolha a forma de pagamento:");

                    String linkMercadoPago = "https://www.mercadopago.com/checkout/v1/redirect?pref_id=SUA_PREFERENCIA";
                    String linkPicPay = "https://picpay.me/seuUsuario/valor";

                    enviarMenuPagamentoComLinks(chatId, linkMercadoPago, linkPicPay);

                    estadosUsuario.put(chatId, EstadoUsuario.ESCOLHENDO_PAGAMENTO);
                }
                else
                {
                    enviarMensagemSimples(chatId, "❌ Email inválido. Por favor, digite um email válido:");

                }
            }
            case ESCOLHENDO_PAGAMENTO ->
            {

                enviarMensagemSimples(chatId, "Por favor, escolha a forma de pagamento nos botões abaixo.");
            }

            default ->
            {

                enviarMensagemSimples(chatId, "Use os botões para navegar pelo menu.");
            }
        }
    }

     // =============================
    //  Processamento  callback
   // ==============================

    private void processarCallback(CallbackQuery callbackQuery) throws TelegramApiException
    {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();

        if (data.equals("ESCOLHER_TIPO"))
        {
            enviarMenuTiposDeBike(chatId);
            estadosUsuario.put(chatId, EstadoUsuario.ESCOLHENDO_TIPO);
        }
        else if (isTipoDeBike(data))
        {
            enviarMenuModelosDeBike(chatId, data);
            estadosUsuario.put(chatId, EstadoUsuario.ESCOLHENDO_MODELO);
        }
        else if (data.startsWith("ESCOLHER_MODELO_"))
        {
            processarEscolhaDeModelo(chatId, data);
            estadosUsuario.put(chatId, EstadoUsuario.ESCOLHENDO_MODELO);
        }
        else if (data.startsWith("CONFIRMAR_"))
        {
            processarConfirmacaoDeModelo(data, chatId);
            estadosUsuario.put(chatId, EstadoUsuario.AGUARDANDO_ESCOLHA_DURACAO_OU_MAIS);
        }
        else if (data.equals("ESCOLHER_MAIS_UMA"))
        {
            enviarMenuTiposDeBike(chatId);
            estadosUsuario.put(chatId, EstadoUsuario.ESCOLHENDO_TIPO);
        }
        else if (data.startsWith("ESCOLHER_DURACAO_"))
        {
            mensagemEscolhaDeDias(chatId);
            estadosUsuario.put(chatId, EstadoUsuario.ESCOLHANDO_DIAS);
        }
        else if (data.startsWith("ESCOLHER_DIAS_"))
        {

            String textoResumo = mensagemResumoAluguel(chatId, data);
            InlineKeyboardMarkup teclado = menuFinalizarSelecao();

            SendMessage mensagem = new SendMessage();
            mensagem.setChatId(chatId.toString());
            mensagem.setReplyMarkup(teclado);
            mensagem.setText(textoResumo);
            enviarMensagem(mensagem);


            estadosUsuario.put(chatId, EstadoUsuario.AGUARDANDO_EMAIL);
        }
        else if (data.equals("FINALIZAR_"))
        {

            mensagemEmailForceReply(chatId);
            estadosUsuario.put(chatId, EstadoUsuario.AGUARDANDO_EMAIL);
        }
        else if (data.equals("AJUDA"))
        {
            String respostaAjuda = gerarMensagemAjuda();
            enviarMensagemSimples(chatId, respostaAjuda);

        }
        else
        {
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
   //  Processos locais
  // =============================
    private void processarEscolhaDeModelo(Long chatId, String data)
    {
        String modelo = data.replace("ESCOLHER_MODELO_", "");

        List<Bikes> bikes = retornarDetalhesDoModelo(modelo);

        String textoMensagem = mensagemDetalhesModeloEscolhido(bikes);
        String tipoDaBike = bikes.isEmpty() ? "" : bikes.getFirst().getClass().getSimpleName();
        String cor = bikes.isEmpty() ? "" : bikes.getFirst().getCor();

        SendMessage mensagem = new SendMessage();
        mensagem.setChatId(chatId.toString());
        mensagem.setText(textoMensagem);
        mensagem.enableMarkdown(true);

        InlineKeyboardMarkup teclado = menuResumoDaBike(modelo, cor, tipoDaBike);
        mensagem.setReplyMarkup(teclado);

        enviarMensagem(mensagem);

    }
    private void processarConfirmacaoDeModelo(String data, Long chatId)
    {
        String resto = data.replace("CONFIRMAR_", "");
        String[] partes = resto.split("_", 2);
        String modeloConfirmado = partes[0];
        String corConfirmada = partes.length > 1 ? partes[1] : "";

        List<Bikes> bikeEscolhida = retornarDetalhesDoModelo(modeloConfirmado);
        Bikes b = bikeEscolhida.get(0);

        modelosEscolhidosPorUsuario.putIfAbsent(chatId, new ArrayList<>());
        modelosEscolhidosPorUsuario.get(chatId).add(b);

        mensagemEscolherDuracaoOuMaisBike(chatId, modeloConfirmado, corConfirmada);
    }
  // =============================
 //  Menus e mensagens
// =============================

    private void enviarMenuPagamentoComLinks(Long chatId, String linkMercadoPago, String linkPicPay)
    {
        SendMessage mensagem = new SendMessage();
        mensagem.setChatId(chatId.toString());
        mensagem.setText("Escolha a plataforma de pagamento:");

        InlineKeyboardButton botaoMercadoPago = new InlineKeyboardButton();
        botaoMercadoPago.setText("💳 Pagar com Mercado Pago");
        botaoMercadoPago.setCallbackData("PAGO_MERCADO");
        botaoMercadoPago.setUrl(linkMercadoPago);

        InlineKeyboardButton botaoPicPay = new InlineKeyboardButton();
        botaoPicPay.setText("💳 Pagar com PicPay");
        botaoPicPay.setCallbackData("PAGO_PICPAY");
        botaoPicPay.setUrl(linkPicPay);

        List<List<InlineKeyboardButton>> linhas = List.of(List.of(botaoMercadoPago), List.of(botaoPicPay));

        InlineKeyboardMarkup teclado = new InlineKeyboardMarkup();
        teclado.setKeyboard(linhas);

        mensagem.setReplyMarkup(teclado);

        enviarMensagem(mensagem);
    }

    private InlineKeyboardMarkup menuFinalizarSelecao()
    {
       InlineKeyboardButton finalizarSelecao = new InlineKeyboardButton();
       finalizarSelecao.setText("Confirmar");
       finalizarSelecao.setCallbackData("FINALIZAR_");

       InlineKeyboardButton voltarSelecao = new InlineKeyboardButton();
       voltarSelecao.setText("Cancelar");
       voltarSelecao.setCallbackData("CANCELAR_");

        List<List<InlineKeyboardButton>> linhas = List.of(List.of(finalizarSelecao), List.of(voltarSelecao));

        InlineKeyboardMarkup teclado = new InlineKeyboardMarkup();
        teclado.setKeyboard(linhas);

        return teclado;
    }
    private String mensagemResumoAluguel(Long chatId, String data)

    {
        StringBuilder texto = builderMensagemResumo(chatId, data);

        return texto.toString();
    }


    private void mensagemEscolherDuracaoOuMaisBike(Long chatId, String modelo, String cor)
     {
      SendMessage mensagem = new SendMessage();
      mensagem.setChatId(chatId.toString());
      mensagem.setText("Você confirmou o modelo " + modelo + " na cor " + cor + ".\n\nO que deseja fazer agora?");

      InlineKeyboardButton botaoEscolherDuracao = new InlineKeyboardButton();
      botaoEscolherDuracao.setText("Escolher Duração do Aluguel");
      botaoEscolherDuracao.setCallbackData("ESCOLHER_DURACAO_" + modelo + "_" + cor);

      InlineKeyboardButton botaoEscolherMaisBike = new InlineKeyboardButton();
      botaoEscolherMaisBike.setText("Escolher mais uma bike");
      botaoEscolherMaisBike.setCallbackData("ESCOLHER_MAIS_UMA");

      List<List<InlineKeyboardButton>> linhas = List.of(List.of(botaoEscolherDuracao), List.of(botaoEscolherMaisBike));

      InlineKeyboardMarkup teclado = new InlineKeyboardMarkup();
      teclado.setKeyboard(linhas);

      mensagem.setReplyMarkup(teclado);

      enviarMensagem(mensagem);
    }

    private void mensagemEscolhaDeDias(Long chatId)
    {
     List<Bikes> escolhas = modelosEscolhidosPorUsuario.getOrDefault(chatId, new ArrayList<>());


     StringBuilder texto = new StringBuilder("Você escolheu estas bicicletas:\n\n");

        for (Bikes escolha : escolhas)
        {
            texto.append("• ").append(escolha.getModelo()).append(" na cor").append(escolha.getCor()).append("\n");
        }

     texto.append("\nAgora escolha o Tempo de aluguel:");
     SendMessage mensagem = new SendMessage();
     mensagem.setChatId(chatId.toString());
     mensagem.setText(texto.toString());

     InlineKeyboardMarkup tecladoDias = menuEscolhaDeDias();
     mensagem.setReplyMarkup(tecladoDias);

     enviarMensagem(mensagem);

    }

    private InlineKeyboardMarkup menuEscolhaDeDias() {
        InlineKeyboardButton dias3 = new InlineKeyboardButton();
        dias3.setText("3 dias");
        dias3.setCallbackData("ESCOLHER_DIAS_3");

        InlineKeyboardButton dias7 = new InlineKeyboardButton();
        dias7.setText("7 dias");
        dias7.setCallbackData("ESCOLHER_DIAS_7");

        InlineKeyboardButton dias10 = new InlineKeyboardButton();
        dias10.setText("10 dias");
        dias10.setCallbackData("ESCOLHER_DIAS_10");

        InlineKeyboardButton dias15 = new InlineKeyboardButton();
        dias15.setText("15 dias");
        dias15.setCallbackData("ESCOLHER_DIAS_15");

        List<List<InlineKeyboardButton>> linhas = List.of(
                List.of(dias3), List.of(dias7), List.of(dias10), List.of(dias15)
        );

        InlineKeyboardMarkup teclado = new InlineKeyboardMarkup();
        teclado.setKeyboard(linhas);

        return teclado;
    }

    private InlineKeyboardMarkup menuResumoDaBike(String modelo, String cor, String tipo)
    {
        InlineKeyboardButton botaoConfirmar = new InlineKeyboardButton();
        botaoConfirmar.setText("Confirmar " + modelo + " na cor " + cor);
        botaoConfirmar.setCallbackData("CONFIRMAR_" + modelo + "_" + cor);

        InlineKeyboardButton botaoVoltar = new InlineKeyboardButton();
        botaoVoltar.setText("Voltar");
        botaoVoltar.setCallbackData("ESCOLHER_TIPO" + tipo);

        List<List<InlineKeyboardButton>> linhas = List.of(List.of(botaoConfirmar), List.of(botaoVoltar));
        InlineKeyboardMarkup teclado = new InlineKeyboardMarkup();
        teclado.setKeyboard(linhas);

        return teclado;
    }
    private String mensagemDetalhesModeloEscolhido(List<Bikes> listaBikes)
    {
        if (listaBikes.isEmpty()) return "Nenhuma bicicleta desse modelo disponível no momento";

        Bikes primeiraBike = listaBikes.getFirst();
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
        List<String> modelos = retornarBikesPorTipo(tipo);

        Set<String> modelosUnicos = new HashSet<>(modelos);

        SendMessage mensagem = new SendMessage();
        mensagem.setChatId(chatId.toString());
        mensagem.setText("Escolha uma bicicleta:");

        List<List<InlineKeyboardButton>> linhas = new ArrayList<>();

        for (String modelo :modelosUnicos)
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
                    \s""";
    }
    // =============================
   //  Acesso ao SQL
  // =============================

    public List<String> retornarBikesPorTipo(String data)
    {
        List<Bikes> bikesFiltradas = BikesDAO.ListarBikesPorTipo(data);
        List<String> modelo = new ArrayList<>();

        for (Bikes bike: bikesFiltradas)
        {
           modelo.add(bike.getModelo());
        }
        return modelo;
    }

    public List<Bikes> retornarDetalhesDoModelo(String data)
    {
        return BikesDAO.buscarBikesPorModelo(data);
    }
    // =============================
   //  Serviços
  // ==============================

    public double servicoCalcularPrecoIndividual(String data,  Bikes listaBikesSelecionadas)
    {
        String diasString = data.replace("ESCOLHER_DIAS_", "");
        return AluguelService.calcularValorIndivudal( listaBikesSelecionadas, diasString);
    }
    public double servicoCalcularPrecoTotal(String data,  List<Bikes> bikesSelecionadas)
    {
        String diasString = data.replace("ESCOLHER_DIAS_", "");
        return AluguelService.calcularTotal( bikesSelecionadas, diasString);
    }

    public void mensagemEmailForceReply(long chatId) throws TelegramApiException
    {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Agora, envie seu e-mail:");
        ForceReplyKeyboard forceReply = new ForceReplyKeyboard();
        forceReply.setForceReply(true);
        message.setReplyMarkup(forceReply);
        execute(message);
    }

    public boolean servicoVerificarEstado()
    {
      return estadosUsuario.containsValue(EstadoUsuario.AGUARDANDO_EMAIL);
    }
    private StringBuilder builderMensagemResumo(Long chatId, String data)
    {

        List<Bikes> bikes = modelosEscolhidosPorUsuario.get(chatId);

        String diasString = data.replace("ESCOLHER_DIAS_", "");

        StringBuilder texto = new StringBuilder("Você escolheu estas bicicletas:\n\n");

        for (Bikes b : bikes)
        {
            texto.append("• ").append(b.getModelo()).append(" na cor ").append(b.getCor()).append("\n");
            texto.append("- " + "Valor do dia: R$").append(b.calcularPreco()).append("\n");
            texto.append("- ").append("Quantidade de dias: ").append(diasString).append("\n");

            double total = servicoCalcularPrecoIndividual(data, b);

            texto.append("- ").append("Valor: R$").append(total).append("\n\n");
        }

        texto.append("Valor de seguro: R$50").append("\n");
        texto.append("Valor Total: R$").append(servicoCalcularPrecoTotal(data,bikes));
        return texto;
    }


}

