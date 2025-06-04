package com.ProjetoWheels;

import com.ProjetoWheels.DAO.BikesDAO;
import com.ProjetoWheels.DAO.PopularTabelaDAO;
import com.ProjetoWheels.chat.Biketron3000;
import com.ProjetoWheels.database.DataBaseSetup;
import com.ProjetoWheels.model.SpeedBikes;
import com.ProjetoWheels.service.EmailService;
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
    }
}