package com.ProjetoWheels.ui;

import com.ProjetoWheels.enums.bikes.TamanhoQuadro;
import com.ProjetoWheels.enums.bikes.TipoPneu;

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
        JPanel form = new JPanel(new FlowLayout(FlowLayout.CENTER));

        campoCor = new JTextField(20);
        campoCor.setPreferredSize(new Dimension(100, 50));
        form.add(new JLabel("Cor:"));
        form.add(campoCor);

        campoModelo = new JTextField(20);
        campoModelo.setPreferredSize(new Dimension(100, 50));
        form.add(new JLabel("Modelo:"));
        form.add(campoModelo);


        ButtonGroup grupo = new ButtonGroup();
        radioMountain = new JRadioButton("MountainBike");
        radioSpeed = new JRadioButton("SpeedBike");
        radioChildrens = new JRadioButton("ChildrenBike");
        radioDefault = new JRadioButton("DefaultBike");

        grupo.add(radioMountain);
        grupo.add(radioSpeed);
        grupo.add(radioDefault);
        grupo.add(radioChildrens);

        radioMountain.addActionListener(e -> atualizarExtras());
        radioSpeed.addActionListener(e -> atualizarExtras());

        form.add(new JLabel("Tipo:"));

        JPanel tipoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        tipoPanel.add(radioMountain);
        tipoPanel.add(radioSpeed);
        tipoPanel.add(radioDefault);
        tipoPanel.add(radioChildrens);

        form.add(tipoPanel);




        radioComRodinhas = new JRadioButton("Como Rodinhas");
        radioSemRodinhas = new JRadioButton("Sem Rodinhas");
        grupoRodinhas.add(radioComRodinhas);

        painelExtra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboTipoPneu = new JComboBox<>(TipoPneu.values());
        comboTamanhoQuadro= new JComboBox<>(TamanhoQuadro.values());

        atualizarExtras();

        add(form, BorderLayout.CENTER);
        add(painelExtra, BorderLayout.NORTH);

        JButton salvar = new JButton("Salvar");
        salvar.addActionListener(e -> salvarBicicleta());
        add(salvar, BorderLayout.SOUTH);
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
            painelExtra.add(new JLabel("Com Rodinhas?"));
            painelExtra.add(radioComRodinhas);
            painelExtra.add(new JLabel("Sem Rodinhas?"));
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


    }

}
