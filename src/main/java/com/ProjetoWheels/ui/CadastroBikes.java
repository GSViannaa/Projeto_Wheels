package com.ProjetoWheels.ui;

import com.ProjetoWheels.DAO.BikesDAO;
import com.ProjetoWheels.enums.bikes.StatusBikes;
import com.ProjetoWheels.enums.bikes.TamanhoQuadro;
import com.ProjetoWheels.enums.bikes.TemRodinhas;
import com.ProjetoWheels.enums.bikes.TipoPneu;
import com.ProjetoWheels.model.*;

import javax.swing.*;
import java.awt.*;

public class CadastroBikes extends JDialog
{
    private JTextField campoModelo, campoCor;
    private JRadioButton radioMountain, radioSpeed, radioDefault, radioChildrens;
    private JRadioButton radioComRodinhas, radioSemRodinhas;
    private final ButtonGroup grupoRodinhas = new ButtonGroup();
    private JComboBox<TipoPneu> comboTipoPneu;
    private JComboBox<TamanhoQuadro> comboTamanhoQuadro;
    private JPanel painelExtra;

    public CadastroBikes(JFrame parent)
    {
        super(parent, "Nova Bicicleta", true);

        setSize(500, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initComponentes();
    }
    private void initComponentes()
    {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JPanel modeloPanel = new JPanel();
        modeloPanel.setLayout(new BoxLayout(modeloPanel, BoxLayout.Y_AXIS));
        modeloPanel.add(new JLabel("Modelo:"));

        campoModelo = new JTextField(20);
        campoModelo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        modeloPanel.add(campoModelo);
        form.add(modeloPanel);
        form.add(Box.createVerticalStrut(15));

        JPanel corPanel = new JPanel();
        corPanel.setLayout(new BoxLayout(corPanel, BoxLayout.Y_AXIS));
        corPanel.add(new JLabel("Cor:"));

        campoCor = new JTextField(20);
        campoCor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        corPanel.add(campoCor);
        form.add(corPanel);
        form.add(Box.createVerticalStrut(15));

        JPanel tipoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tipoPanel.add(new JLabel("Tipo:"));
        radioMountain = new JRadioButton("Mountain Bike");
        radioSpeed = new JRadioButton("Speed Bike");
        radioDefault = new JRadioButton("Urbana");
        radioChildrens = new JRadioButton("Infantil");

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(radioMountain);
        grupo.add(radioSpeed);
        grupo.add(radioDefault);
        grupo.add(radioChildrens);

        tipoPanel.add(radioMountain);
        tipoPanel.add(radioSpeed);
        tipoPanel.add(radioDefault);
        tipoPanel.add(radioChildrens);
        form.add(tipoPanel);

        painelExtra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboTipoPneu = new JComboBox<>(TipoPneu.values());
        comboTamanhoQuadro = new JComboBox<>(TamanhoQuadro.values());
        radioComRodinhas = new JRadioButton("Com Rodinhas");
        radioSemRodinhas = new JRadioButton("Sem Rodinhas");
        grupoRodinhas.add(radioComRodinhas);
        grupoRodinhas.add(radioSemRodinhas);

        form.add(painelExtra);
        atualizarExtras();

        radioMountain.addActionListener(e -> atualizarExtras());
        radioSpeed.addActionListener(e -> atualizarExtras());
        radioChildrens.addActionListener(e -> atualizarExtras());
        radioDefault.addActionListener(e -> atualizarExtras());

        JButton salvar = new JButton("Salvar");
        salvar.addActionListener(e -> {
            salvarBicicleta();
            TelaAdministrador.atualizarTabela();
        });

        JPanel botaoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoPanel.add(salvar);

        add(form, BorderLayout.CENTER);
        add(botaoPanel, BorderLayout.SOUTH);
    }
    private void atualizarExtras() {
        painelExtra.removeAll();

        if (radioMountain.isSelected()) {
            painelExtra.add(new JLabel("Tipo de Pneu:"));
            painelExtra.add(comboTipoPneu);
        }
        else if (radioSpeed.isSelected())
        {
            painelExtra.add(new JLabel("Tamanho do Quadro:"));
            painelExtra.add(comboTamanhoQuadro);
        }
        else if (radioChildrens.isSelected())
        {
            painelExtra.add(radioComRodinhas);
            painelExtra.add(radioSemRodinhas);
        }
        else if (radioDefault.isSelected())
        {
            painelExtra.removeAll();

        }
        painelExtra.revalidate();
        painelExtra.repaint();
    }

    private  void salvarBicicleta()
    {
        String cor = campoCor.getText();
        String modelo = campoModelo.getText();

        if (modelo.isBlank() || cor.isBlank())
        {
            JOptionPane.showMessageDialog(this, "Campo obrigatórios");
            return;
        }

        Bikes novaBike;

        if (radioMountain.isSelected())
        {
            TipoPneu tipoPneu = (TipoPneu) comboTipoPneu.getSelectedItem();
            novaBike = new MountainBikes(modelo, cor, StatusBikes.DISPONIVEL,tipoPneu);
        }
        else if (radioSpeed.isSelected())
        {
            TamanhoQuadro tamanhoQuadro = (TamanhoQuadro) comboTamanhoQuadro.getSelectedItem();
            novaBike = new SpeedBikes(modelo, cor, StatusBikes.DISPONIVEL,tamanhoQuadro);
        }
        else if (radioChildrens.isSelected())
        {
            TemRodinhas rodinhas = radioComRodinhas.isSelected() ? TemRodinhas.SIM : TemRodinhas.NAO;
            novaBike = new ChildrensBikes(modelo, cor, StatusBikes.DISPONIVEL, rodinhas);
        }
        else
        {
            novaBike = new DefaultBikes(modelo, cor, StatusBikes.DISPONIVEL);
        }
        novaBike.setStatusDisponibilidade(StatusBikes.DISPONIVEL);

        try
        {
            BikesDAO.salvarNoBancoDeDados(novaBike);
            JOptionPane.showMessageDialog(this, "Bicicleta salva com sucesso!");
            dispose();
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, "Erro ao salvar a bicicleta: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
