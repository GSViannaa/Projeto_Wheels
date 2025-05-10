package com.ProjetoWheels.ui;

import com.ProjetoWheels.enums.bikes.StatusBikes;
import com.ProjetoWheels.model.Bikes;
import com.ProjetoWheels.model.MountainBike;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaAdministrador extends JFrame
{
    private JTable tabela;
    private DefaultTableModel modelo;
    private List<Bikes> bikes = new ArrayList<>();

    public  TelaAdministrador()
    {
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponentes();
    }

    private  void initComponentes()
    {
        setLayout(new BorderLayout());

        String[] colunas = {"ID", "Modelo", "Tipo", "Status"};
        modelo = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modelo);
        add(new JScrollPane(tabela),BorderLayout.CENTER);

        JButton botaoAdicionar = new JButton("Adicionar bicicleta");

        botaoAdicionar.addActionListener(e ->
        {
            CadastroBikes dialog = new CadastroBikes(this);
            dialog.setVisible(true);
        });


        JPanel painelBotao = new JPanel();
        painelBotao.add(botaoAdicionar);
        add(painelBotao,BorderLayout.SOUTH);
    }

    private void atualizarTabela() {

        modelo.setRowCount(0);

        for (Bikes b : bikes)
        {
            Object[] linha =
            {
              b.getId(),
              b.getModelo(),
              b.getClass().getSimpleName(),
              b.getStatusDisponibilidade()
            };

            modelo.addRow(linha);
        }
    }
    private void adicionarBicicleta() {

        int novoId = bikes.size() + 1;
        MountainBike nova = new MountainBike("p", "Nova MTB " + novoId, StatusBikes.DISPONIVEL , com.ProjetoWheels.enums.bikes.TipoPneu.MISTO);
        bikes.add(nova);
        atualizarTabela();
    }
}
