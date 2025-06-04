package com.ProjetoWheels.bot;

import com.ProjetoWheels.DAO.BikesDAO;
import com.ProjetoWheels.enums.usuarios.EstadoUsuario;
import com.ProjetoWheels.model.Bikes;
import com.ProjetoWheels.service.EmailService;
import com.ProjetoWheels.service.ReciboService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CallbackHandler
{
    private final MessageHandler messageHandler;

    public CallbackHandler(MessageHandler sharedHandler) {this.messageHandler = sharedHandler;}

    public void handleCallback(CallbackQuery callbackQuery, AbsSender bot)
    {
        Long chatId = callbackQuery.getMessage().getChatId();
        String data = callbackQuery.getData();

        EstadoUsuario estadoAtual = messageHandler.obterEstado(chatId);

        switch (data) {
            case "ESCOLHER_TIPO", "ESCOLHER_MAIS_UMA" -> {

                SendMessage menuTipos = MessageBuilder.criarMenuTiposDeBike(chatId);
                enviar(bot, menuTipos);

                messageHandler.definirEstado(chatId, EstadoUsuario.ESCOLHENDO_TIPO);
            }

            case "AJUDA" -> {
                SendMessage ajuda = MessageBuilder.gerarMensagemAjuda(chatId);
                enviar(bot, ajuda);
            }
            case "DefaultBikes", "MountainBikes", "SpeedBikes", "ChildrensBikes" -> {
                List<String> listaBruta = BikesDAO.listarBikesPorTipo(data);

                Set<String> modelosUnicos = new LinkedHashSet<>(listaBruta);

                if (modelosUnicos.isEmpty())
                {

                    SendMessage msg = new SendMessage(chatId.toString(), "Nenhuma bicicleta desse tipo disponível no momento.");
                    enviar(bot, msg);
                    return;
                }

                List<List<InlineKeyboardButton>> linhas = new ArrayList<>();

                for (String modelo : modelosUnicos)
                {
                    var botaoModelo = new InlineKeyboardButton(modelo);
                    botaoModelo.setCallbackData("ESCOLHER_MODELO_" + modelo);
                    linhas.add(List.of(botaoModelo));
                }

                var tecladoModelos = new InlineKeyboardMarkup(linhas);

                SendMessage msgModelos = new SendMessage(chatId.toString(), "Escolha uma bicicleta:");
                msgModelos.setReplyMarkup(tecladoModelos);
                enviar(bot, msgModelos);

                messageHandler.definirEstado(chatId, EstadoUsuario.ESCOLHENDO_MODELO);
            }

            default -> {
                if (data.startsWith("ESCOLHER_MODELO_"))
                {
                    processarEscolhaDeModelo(chatId, data, bot);
                }
                else if (data.startsWith("CONFIRMAR_"))
                {
                    processarConfirmacaoDeModelo(chatId, data, bot);
                }
                else if (data.startsWith("ESCOLHER_DURACAO_"))
                {
                    List<String> listaModelos = messageHandler.obterModelosEscolhidos(chatId);
                    List<Bikes> listaBikes = new ArrayList<>();

                    if (listaModelos != null)
                    {
                        for (String entry : listaModelos)
                        {
                            String[] parts = entry.split("##");
                            String modelo = parts[0];

                            List<Bikes> busca = BikesDAO.buscarBikesPorModelo(modelo);
                            if (!busca.isEmpty()) {
                                listaBikes.add(busca.get(0));
                            }
                        }
                    }


                    SendMessage msgDias = MessageBuilder.criarMensagemEscolhaDeDias(chatId, listaBikes);
                    enviar(bot, msgDias);
                    messageHandler.definirEstado(chatId, EstadoUsuario.ESCOLHANDO_DIAS);
                }
                else if (data.startsWith("ESCOLHER_DIAS_"))
                {
                    processarEscolherDias(chatId, data, bot);
                }
                else if (data.equals("FINALIZAR_"))
                {
                    SendMessage perguntaEmail = MessageBuilder.criarPerguntaEmail(chatId);
                    enviar(bot, perguntaEmail);
                    messageHandler.definirEstado(chatId, EstadoUsuario.AGUARDANDO_EMAIL);
                }
                else if (data.startsWith("PAGO_"))
                {
                    processarPagamento(chatId, data, bot);
                }
                else
                {
                    SendMessage desconhecido = new SendMessage(chatId.toString(), "Opção desconhecida.");
                    enviar(bot, desconhecido);
                }
            }
        }
    }

    private void processarEscolhaDeModelo(Long chatId, String data, AbsSender bot
    ) {
        String modelo = data.replace("ESCOLHER_MODELO_", "");
        List<Bikes> bikes = BikesDAO.buscarBikesPorModelo(modelo);

        if (bikes.isEmpty())
        {
            SendMessage msg = new SendMessage(chatId.toString(), "Nenhuma bicicleta desse modelo disponível no momento");
            enviar(bot, msg);
            return;
        }

        Bikes primeira = bikes.get(0);
        String texto = MessageBuilder.montarDetalhesModelo(primeira, bikes.size());
        SendMessage msgDetalhes = new SendMessage(chatId.toString(), texto);
        msgDetalhes.enableMarkdown(true);
        msgDetalhes.setReplyMarkup(KeyboardBuilder.criarTecladoConfirmacaoModelo(primeira.getModelo(), primeira.getCor()));
        enviar(bot, msgDetalhes);

        messageHandler.definirEstado(chatId, EstadoUsuario.ESCOLHENDO_MODELO);
    }

    private void processarConfirmacaoDeModelo(Long chatId, String data, AbsSender bot)
    {
        String resto = data.replace("CONFIRMAR_", "");
        String[] partes = resto.split("_", 2);
        String modeloConfirmado = partes[0];
        String corConfirmada = partes.length > 1 ? partes[1] : "";


        List<String> listaModelos = messageHandler.obterModelosEscolhidos(chatId);
        if (listaModelos == null) {
            listaModelos = new ArrayList<>();
        }
        listaModelos.add(modeloConfirmado + "##" + corConfirmada);
        messageHandler.adicionarModeloEscolhido(chatId, listaModelos);

        SendMessage opcoes = MessageBuilder.montarMensagemEscolherDuracaoOuMais(chatId, modeloConfirmado, corConfirmada);
        enviar(bot, opcoes);
        messageHandler.definirEstado(chatId, EstadoUsuario.AGUARDANDO_ESCOLHA_DURACAO_OU_MAIS);
    }

    private void processarEscolherDias(Long chatId, String data, AbsSender bot)
    {
        String dias = data.replace("ESCOLHER_DIAS_", "");
        messageHandler.armazenarDias(chatId, dias);

        List<String> modelos = messageHandler.obterModelosEscolhidos(chatId);
        List<Bikes> listaBikes = new ArrayList<>();

        if (modelos != null)
        {
            for (String entry : modelos)
            {
                String[] parts = entry.split("##");
                String modelo = parts[0];

                List<Bikes> busca = BikesDAO.buscarBikesPorModelo(modelo);

                if (!busca.isEmpty())
                {
                    listaBikes.add(busca.get(0));
                }
            }
        }

        String resumo = MessageBuilder.montarResumoAluguel(chatId, dias, listaBikes);
        SendMessage msgResumo = new SendMessage(chatId.toString(), resumo);
        msgResumo.setReplyMarkup(KeyboardBuilder.criarTecladoFinalizarSelecao());
        enviar(bot, msgResumo);

        messageHandler.definirEstado(chatId, EstadoUsuario.AGUARDANDO_EMAIL);
    }

    private void processarPagamento(Long chatId, String data, AbsSender bot)
    {
        String email = messageHandler.obterEmail(chatId);
        String dias = messageHandler.obterDias(chatId);


        List<String> modelos = messageHandler.obterModelosEscolhidos(chatId);
        List<Bikes> listaBikes = new ArrayList<>();
        if (modelos != null)
        {
            for (String entry : modelos)
            {
                String[] parts = entry.split("##");
                String modelo = parts[0];
                List<Bikes> busca = BikesDAO.buscarBikesPorModelo(modelo);

                if (!busca.isEmpty())
                {
                    listaBikes.add(busca.get(0));
                }
            }
        }

        String resumoParaPdf = MessageBuilder.montarResumoAluguel(chatId, dias, listaBikes);
        String caminhoPdf = ReciboService.gerarPDF(resumoParaPdf);
        new EmailService().enviarEmail(email, "Seu recibo de aluguel", caminhoPdf);

        SendMessage confirmacao = new SendMessage(chatId.toString(),
                "✅ Pagamento confirmado! Seu recibo foi enviado para: " + email + ".  \n" +
                        "Obrigado por alugar com a Biketron 3000! \n" +
                        " Agora, entre em contato com nossa loja física para agendar a retirada da sua bike. Estamos te esperando!");
        enviar(bot, confirmacao);

        messageHandler.limparDadosUsuario(chatId);
    }

    public static SendMessage criarMenuTiposDeBike(Long chatId)
    {
        SendMessage msg = new SendMessage(chatId.toString(), "Tipo de bicicleta:");
        InlineKeyboardMarkup teclado = KeyboardBuilder.criarTecladoTiposDeBike(chatId);
        msg.setReplyMarkup(teclado);
        return msg;
    }

    private void enviar(AbsSender bot, SendMessage mensagem)
    {
        try
        {
            bot.execute(mensagem);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
