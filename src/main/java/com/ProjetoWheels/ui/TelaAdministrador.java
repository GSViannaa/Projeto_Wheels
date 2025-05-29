package com.ProjetoWheels.ui;

import com.ProjetoWheels.DAO.BikesDAO;
import com.ProjetoWheels.model.Bikes;

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
        setSize(600,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponentes();
        atualizarTabela();
    }

    private  void initComponentes()
    {
        setLayout(new BorderLayout());

        String[] colunas = {"ID", "Modelo", "Cor", "Tipo", "Status"};
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
        bikes = BikesDAO.listarBikes();
        modelo.setRowCount(0);

        for (Bikes b : bikes)
        {
            modelo.addRow(new Object[]
            {
             b.getId(),
             b.getModelo(),
             b.getCor(),
             b.getClass().getSimpleName(),
             b.getStatusDisponibilidade()
            });

        }
    }


}
