package com.ProjetoWheels;

import com.ProjetoWheels.DAO.PopularTabelaDAO;
import com.ProjetoWheels.chat.Biketron3000;
import com.ProjetoWheels.database.DataBaseSetup;
import com.ProjetoWheels.ui.TelaAdministrador;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws TelegramApiException
    {
        DataBaseSetup.criarTabelas();
        //PopularTabelaDAO.popularTabela();
        SwingUtilities.invokeLater(() -> new TelaAdministrador().setVisible(true));

        Biketron3000 bot = new Biketron3000();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Biketron3000());

        bot.sendText(7965199255L, "Olá, sou o Biketron3000 e estou aqui para ajudá-lo! " +
                "\nVocê pode usar os comandos abaixo para ajudar no aluguel da sua bike. \n" +
                "\n/alugar" +
                "\n/lista");


    }
}