package com.ProjetoWheels.bot;

import com.ProjetoWheels.model.Bikes;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class MessageBuilder
{

    public static SendMessage criarMensagemInicial(Long chatId)
    {
        SendMessage msg = new SendMessage(chatId.toString(),"Olá! Você chegou no Biketron 300 — o jeito mais fácil de alugar sua bike pelo Telegram!\n" +
                "Escolha abaixo o tipo de bicicleta e bora pedalar!" +
                "Clique abaixo para escolher o tipo de bicicleta:");

        InlineKeyboardButton escolherTipos = new InlineKeyboardButton("Escolher tipos");
        escolherTipos.setCallbackData("ESCOLHER_TIPO");

        InlineKeyboardButton ajuda = new InlineKeyboardButton("Ajuda");
        ajuda.setCallbackData("AJUDA");

        InlineKeyboardMarkup teclado = new InlineKeyboardMarkup(List.of(List.of(escolherTipos), List.of(ajuda)));
        msg.setReplyMarkup(teclado);

        return msg;
    }

    public static SendMessage criarMensagemRecomecar(Long chatId)
    {
        SendMessage msg = new SendMessage(chatId.toString(), "Opa, parece que algo não saiu como esperado...  \n" +
                "Sem problemas! É só clicar aqui embaixo e tentar novamente.");

        InlineKeyboardButton escolherTipos = new InlineKeyboardButton("Escolher tipos");

        escolherTipos.setCallbackData("ESCOLHER_TIPO");
        InlineKeyboardButton ajuda = new InlineKeyboardButton("Ajuda");

        ajuda.setCallbackData("AJUDA");
        InlineKeyboardMarkup teclado = new InlineKeyboardMarkup(List.of(List.of(escolherTipos), List.of(ajuda)));

        msg.setReplyMarkup(teclado);
        return msg;
    }

    public static SendMessage gerarMensagemAjuda(Long chatId)
    {
        String texto = """
            1 Como alugar sua bike? Super fácil:
             1️⃣ Toque em 'Escolher tipos' \s
             2️⃣ Escolha o tipo de bicicleta que você quer \s
             3️⃣ Selecione sua bike preferida \s
             4️⃣ Defina por quanto tempo vai querer alugar \s
             5️⃣ Escolha como vai pagar... e pronto, só pedalar! 🚴‍♂️🚴‍♀️
            """;
        return new SendMessage(chatId.toString(), texto);
    }

    public static SendMessage montarMensagemLinksPagamento(Long chatId)
    {
        String texto = "✅ Email válido! Agora escolha a forma de pagamento:";
        InlineKeyboardButton mercadoPago = KeyboardBuilder.botaoUrl(
                "💳 Pagar com Mercado Pago",
                "PAGO_",
               "https://www.mercadopago.com/checkout/v1/redirect?pref_id=SUA_PREFERENCIA"
        );
        InlineKeyboardButton picPay = KeyboardBuilder.botaoUrl(
                "💳 Pagar com PicPay",
                "PAGO_",
               "https://picpay.me/seuUsuario/valor"
        );
        InlineKeyboardButton noLocal = new InlineKeyboardButton();
        noLocal.setText("Pagar no Local");
        noLocal.setCallbackData("PAGO_");

        InlineKeyboardMarkup teclado = new InlineKeyboardMarkup(List.of(
                List.of(mercadoPago),
                List.of(picPay),
                List.of(noLocal)
        ));
        SendMessage msg = new SendMessage(chatId.toString(), texto);
        msg.setReplyMarkup(teclado);
        return msg;
    }

    public static SendMessage montarMensagemEscolherDuracaoOuMais(Long chatId, String modelo, String cor)
    {
        String texto = "Você confirmou o modelo " + modelo + " na cor " + cor + ".\n\nO que deseja fazer agora?";
        InlineKeyboardButton botaoDuracao = new InlineKeyboardButton("Escolher Duração do Aluguel");
        botaoDuracao.setCallbackData("ESCOLHER_DURACAO_" + modelo + "_" + cor);

        InlineKeyboardButton botaoMais = new InlineKeyboardButton("Escolher mais uma bike");
        botaoMais.setCallbackData("ESCOLHER_MAIS_UMA");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(List.of(
                List.of(botaoDuracao),
                List.of(botaoMais)
        ));

        SendMessage msg = new SendMessage(chatId.toString(), texto);
        msg.setReplyMarkup(markup);
        return msg;
    }

    public static SendMessage criarMensagemEscolhaDeDias(Long chatId, List<Bikes> listaBikes)
    {
        StringBuilder texto = new StringBuilder();
        texto.append("Você escolheu estas bicicletas:\n\n");

        for (Bikes b : listaBikes)
        {
            texto.append("• ").append(b.getModelo())
                    .append(" na cor ").append(b.getCor())
                    .append("\n");
        }
        texto.append("\nAgora escolha o Tempo de aluguel:");

        SendMessage msg = new SendMessage(chatId.toString(), texto.toString());
        InlineKeyboardMarkup tecladoDias = KeyboardBuilder.criarTecladoEscolhaDeDias();
        msg.setReplyMarkup(tecladoDias);
        return msg;
    }


    public static SendMessage criarPerguntaEmail(Long chatId)
    {
        SendMessage msg = new SendMessage(chatId.toString(), "Agora, envie seu e-mail:");
        ForceReplyKeyboard forceReply = new ForceReplyKeyboard();
        forceReply.setForceReply(true);
        msg.setReplyMarkup(forceReply);
        return msg;
    }

    public static String montarResumoAluguel(Long chatId, String dias, List<Bikes> listaBikes)
    {

        StringBuilder texto = new StringBuilder("Você escolheu estas bicicletas:\n\n");
        double totalGeral = 0.0;

        for (Bikes b : listaBikes)
        {
            texto.append("• ").append(b.getModelo())
                    .append(" na cor ").append(b.getCor()).append("\n");
            texto.append("- Valor do dia: R$").append(String.format("%.2f", b.calcularPreco())).append("\n");
            texto.append("- Quantidade de dias: ").append(dias).append("\n");
            double total = b.calcularPreco() * Integer.parseInt(dias);
            texto.append("- Valor: R$").append(String.format("%.2f", total)).append("\n\n");
            totalGeral += total;
        }

        texto.append("Valor de seguro: R$50\n");
        texto.append("Valor Total: R$").append(String.format("%.2f", totalGeral + 50));

        return texto.toString();
    }

    public static SendMessage criarMenuTiposDeBike(Long chatId)
    {
        SendMessage msg = new SendMessage(chatId.toString(), "Tipo de bicicleta:");
        InlineKeyboardMarkup teclado = KeyboardBuilder.criarTecladoTiposDeBike(chatId);
        msg.setReplyMarkup(teclado);
        return msg;
    }

    public static String montarDetalhesModelo(Bikes bike, int quantidadeDisponivel) {
        String modelo = bike.getModelo();
        String cor = bike.getCor();
        double preco = bike.calcularPreco();
        return String.format("""
            *Informações do Modelo:*
            • Modelo: %s
            • Cor: %s
            • Preço por dia: R$ %.2f
            • Quantidade disponível: %d
            """,
                modelo, cor, preco, quantidadeDisponivel);
    }
}
