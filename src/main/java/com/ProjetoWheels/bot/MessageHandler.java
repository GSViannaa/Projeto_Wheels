package com.ProjetoWheels.bot;

import com.ProjetoWheels.bot.MessageBuilder;
import com.ProjetoWheels.enums.usuarios.EstadoUsuario;
import com.ProjetoWheels.service.ValidacaoService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageHandler
{

    private final Map<Long, EstadoUsuario> estadosUsuario = new HashMap<>();
    private final Map<Long, String> emailUsuario = new HashMap<>();
    private final Map<Long, String> diasEscolhidosPorUsuario = new HashMap<>();
    private final Map<Long, List<String>> modelosEscolhidosPorUsuario = new HashMap<>();

    public void handleMessage(Message message, AbsSender bot)
    {
        Long chatId = message.getChatId();
        String texto = message.getText();
        EstadoUsuario estadoAtual = estadosUsuario.getOrDefault(chatId, EstadoUsuario.MENU_PRINCIPAL);

        switch (estadoAtual)
        {
            case MENU_PRINCIPAL -> handleMenuPrincipal(chatId, bot);
            case AGUARDANDO_EMAIL -> handleAguardandoEmail(chatId, texto, bot);
            case ESCOLHENDO_PAGAMENTO -> handleEscolhendoPagamento(chatId, bot);
            default -> handleEstadoDesconhecido(chatId, bot);
        }
    }

    private void handleMenuPrincipal(Long chatId, AbsSender bot)
    {
        SendMessage msg = MessageBuilder.criarMensagemInicial(chatId);
        enviar(bot, msg);
        estadosUsuario.put(chatId, EstadoUsuario.MENU_PRINCIPAL);
    }

    private void handleAguardandoEmail(Long chatId, String texto, AbsSender bot)
    {
        boolean valido = ValidacaoService.isValidEmail(texto);

        if (valido)
        {
            emailUsuario.put(chatId, texto);

            SendMessage msgLinks = MessageBuilder.montarMensagemLinksPagamento(chatId);
            enviar(bot, msgLinks);

            estadosUsuario.put(chatId, EstadoUsuario.ESCOLHENDO_PAGAMENTO);
        }
        else
        {
            SendMessage msgErro = new SendMessage(chatId.toString(), "❌ Email inválido. Por favor, digite um email válido:");
            enviar(bot, msgErro);
        }
    }

    private void handleEscolhendoPagamento(Long chatId, AbsSender bot) {
        SendMessage aviso = new SendMessage(chatId.toString(), "Por favor, escolha a forma de pagamento nos botões abaixo.");
        enviar(bot, aviso);
    }

    private void handleEstadoDesconhecido(Long chatId, AbsSender bot)
    {
        SendMessage msg = MessageBuilder.criarMensagemRecomecar(chatId);
        enviar(bot, msg);
        estadosUsuario.put(chatId, EstadoUsuario.MENU_PRINCIPAL);
    }

    private void enviar(AbsSender bot, SendMessage mensagem)
    {
        try {
            bot.execute(mensagem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void definirEstado(Long chatId, EstadoUsuario estado)
    {
        estadosUsuario.put(chatId, estado);
    }

    public EstadoUsuario obterEstado(Long chatId)
    {
        return estadosUsuario.getOrDefault(chatId, EstadoUsuario.MENU_PRINCIPAL);
    }

    public void armazenarEmail(Long chatId, String email)
    {
        emailUsuario.put(chatId, email);
    }

    public String obterEmail(Long chatId)
    {
        return emailUsuario.get(chatId);
    }

    public void armazenarDias(Long chatId, String dias)
    {
        diasEscolhidosPorUsuario.put(chatId, dias);
    }

    public String obterDias(Long chatId)
    {
        return diasEscolhidosPorUsuario.get(chatId);
    }

    public void adicionarModeloEscolhido(Long chatId, List<String> modelos)
    {
        modelosEscolhidosPorUsuario.put(chatId, modelos);
    }

    public List<String> obterModelosEscolhidos(Long chatId)
    {
        return modelosEscolhidosPorUsuario.get(chatId);
    }

    public void limparDadosUsuario(Long chatId)
    {
        estadosUsuario.remove(chatId);
        emailUsuario.remove(chatId);
        diasEscolhidosPorUsuario.remove(chatId);
        modelosEscolhidosPorUsuario.remove(chatId);
    }
}
