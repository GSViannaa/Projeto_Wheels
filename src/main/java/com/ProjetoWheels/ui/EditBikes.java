package com.ProjetoWheels.ui;

import javax.swing.*;
import java.awt.*;

public class EditBikes extends JDialog
{
    public EditBikes(JFrame parent)
    {
        super(parent, "Editar Bicicletas", true);

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

        JButton botaoDeletar = new JButton("Deletar");

        JPanel painelBotao = new JPanel();
        painelBotao.add(botaoDeletar);
        add(painelBotao,BorderLayout.SOUTH);
    }
}
