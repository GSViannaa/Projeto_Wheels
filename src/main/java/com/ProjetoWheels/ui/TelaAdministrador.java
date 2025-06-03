package com.ProjetoWheels.ui;

import com.ProjetoWheels.DAO.BikesDAO;
import com.ProjetoWheels.model.Bikes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TelaAdministrador extends JFrame
{
    private JTable tabela;
    private DefaultTableModel modelo;
    private List<Bikes> bikes = new ArrayList<>();
    private JTextField campoBusca;

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

        JPanel painelBusca = new JPanel(new BorderLayout());
        campoBusca = new JTextField();
        JButton botaoBuscar = new JButton("Buscar");

        botaoBuscar.addActionListener(e -> filtrarTabela());

        painelBusca.add(new JLabel("Buscar: "), BorderLayout.WEST);
        painelBusca.add(campoBusca, BorderLayout.CENTER);
        painelBusca.add(botaoBuscar, BorderLayout.EAST);

        add(painelBusca, BorderLayout.NORTH);

        String[] colunas = {"Modelo", "Cor", "Tipo", "Status"};
        modelo = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modelo);
        add(new JScrollPane(tabela),BorderLayout.CENTER);

        JButton botaoAdicionar = new JButton("Adicionar bicicleta");

        botaoAdicionar.addActionListener(e ->
        {
            CadastroBikes telaCadastro = new CadastroBikes(this);
            telaCadastro.setVisible(true);
        });

        JButton botaoEditar = new JButton("Editar");

        botaoEditar.addActionListener(e ->{
            EditBikes telaEdicao = new EditBikes(this);
            telaEdicao.setVisible(true);
        });

        JButton botaoDeletar = new JButton("Deletar");

        JPanel painelBotoesSouth = new JPanel();
        painelBotoesSouth.add(botaoAdicionar);
        painelBotoesSouth.add(botaoEditar);
        painelBotoesSouth.add(botaoDeletar);
        add(painelBotoesSouth,BorderLayout.SOUTH);

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        modelo.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            // Evita erro se for evento de estrutura ou exclusão de linha
            if (row < 0 || column < 0) return;

            Object novoValor = modelo.getValueAt(row, column);
            int id = (int) modelo.getValueAt(row, 0); // Supondo que a coluna 0 seja o ID

            BikesDAO.atualizarBikeNoBanco(id, column, novoValor);
        });
    }

    private void atualizarTabela()
    {
        bikes = BikesDAO.listarBikes();
        preencherTabela(bikes);
    }

    private void preencherTabela(List<Bikes> lista)
    {
        modelo.setRowCount(0);
        for (Bikes b : lista)
        {
            modelo.addRow(new Object[]{
                    b.getModelo(),
                    b.getCor(),
                    b.getClass().getSimpleName(),
                    b.getStatusDisponibilidade()
            });
        }
    }

    private void filtrarTabela()
    {
        String termo = campoBusca.getText().toLowerCase();

        List<Bikes> filtradas = bikes.stream()
                .filter(b -> b.getModelo().toLowerCase().contains(termo)
                        || b.getCor().toLowerCase().contains(termo)
                        || b.getStatusDisponibilidade().toString().contains(termo))
                .collect(Collectors.toList());

        preencherTabela(filtradas);
    }
}
