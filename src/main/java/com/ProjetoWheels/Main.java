package com.ProjetoWheels;

import com.ProjetoWheels.DAO.PopularTabelaDAO;
import com.ProjetoWheels.database.DataBaseSetup;
import com.ProjetoWheels.ui.TelaAdministrador;

import javax.swing.*;

public class Main {

    public static void main(String[] args)
    {
        DataBaseSetup.criarTabelas();
        //PopularTabelaDAO.popularTabela();


        SwingUtilities.invokeLater(() -> new TelaAdministrador().setVisible(true));

    }
}