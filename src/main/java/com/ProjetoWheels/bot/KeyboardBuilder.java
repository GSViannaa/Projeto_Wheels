package com.ProjetoWheels.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class KeyboardBuilder
{

    public static InlineKeyboardMarkup criarTecladoTiposDeBike(Long chatId)
    {
        InlineKeyboardButton urbana = new InlineKeyboardButton("Bicicleta Urbana");
        urbana.setCallbackData("DefaultBikes");

        InlineKeyboardButton infantil = new InlineKeyboardButton("Bicicleta Infantil");
        infantil.setCallbackData("ChildrensBikes");


        InlineKeyboardButton speed = new InlineKeyboardButton("Speed Bike");
        speed.setCallbackData("SpeedBikes");

        InlineKeyboardButton mountain = new InlineKeyboardButton("Mountain Bike");
        mountain.setCallbackData("MountainBikes");

        return new InlineKeyboardMarkup(List.of(List.of(urbana), List.of(infantil), List.of(speed), List.of(mountain)));
    }

    public static InlineKeyboardMarkup criarTecladoConfirmacaoModelo(String modelo, String cor)
    {
        InlineKeyboardButton confirmar = new InlineKeyboardButton("Confirmar " + modelo + " na cor " + cor);
        confirmar.setCallbackData("CONFIRMAR_" + modelo + "_" + cor);
        InlineKeyboardButton voltar = new InlineKeyboardButton("Voltar");
        voltar.setCallbackData("ESCOLHER_TIPO");

        return new InlineKeyboardMarkup(List.of(List.of(confirmar), List.of(voltar))
        );
    }

    public static InlineKeyboardMarkup criarTecladoEscolhaDeDias()
    {
        InlineKeyboardButton d3 = new InlineKeyboardButton("3 dias");
        d3.setCallbackData("ESCOLHER_DIAS_3");
        InlineKeyboardButton d7 = new InlineKeyboardButton("7 dias");
        d7.setCallbackData("ESCOLHER_DIAS_7");
        InlineKeyboardButton d10 = new InlineKeyboardButton("10 dias");
        d10.setCallbackData("ESCOLHER_DIAS_10");
        InlineKeyboardButton d15 = new InlineKeyboardButton("15 dias");
        d15.setCallbackData("ESCOLHER_DIAS_15");

        return new InlineKeyboardMarkup(List.of(List.of(d3), List.of(d7), List.of(d10), List.of(d15)));
    }

    public static InlineKeyboardMarkup criarTecladoFinalizarSelecao() {

        InlineKeyboardButton confirmar = new InlineKeyboardButton("Confirmar");
        confirmar.setCallbackData("FINALIZAR_");

        InlineKeyboardButton cancelar = new InlineKeyboardButton("Cancelar");
        cancelar.setCallbackData("CANCELAR_");

        return new InlineKeyboardMarkup(List.of(List.of(confirmar), List.of(cancelar)));
    }

    public static InlineKeyboardButton botaoUrl(String texto, String callbackData, String url)
    {
        InlineKeyboardButton botao = new InlineKeyboardButton(texto);
        botao.setUrl(url);
        botao.setCallbackData(callbackData);
        return botao;
    }
}
