package com.ProjetoWheels.ui;

import com.ProjetoWheels.enums.bikes.TamanhoQuadro;
import com.ProjetoWheels.enums.bikes.TipoPneu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AdicionarBikesController  implements Initializable
{
    @FXML private RadioButton radioMountain;
    @FXML private RadioButton radioSpeed;
    @FXML private RadioButton radioDefault;
    @FXML private RadioButton radioChildrens;

    @FXML private TextField campoModelo;
    @FXML private TextField campoCor;

    @FXML private HBox boxTipoPneu;
    @FXML private ComboBox<TipoPneu> cmbTipoPneu;

    @FXML private HBox boxTamanhoQuadro;
    @FXML private ComboBox<TamanhoQuadro> cmbTamanhoQuadro;

    @FXML private HBox boxTemRodinhas;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ToggleGroup tgBikes = new ToggleGroup();

        radioMountain.setToggleGroup(tgBikes);
        radioSpeed.setToggleGroup(tgBikes);
        radioDefault.setToggleGroup(tgBikes);
        radioChildrens.setToggleGroup(tgBikes);

        cmbTipoPneu.getItems().addAll(TipoPneu.values());
        cmbTamanhoQuadro.getItems().addAll(TamanhoQuadro.values());

    }

    @FXML
    private void atualizarCampos()
    {
        boolean ehMountain = radioMountain.isSelected();
        boolean ehSpeed = radioSpeed.isSelected();
        boolean ehDefault = radioDefault.isSelected();
        boolean ehChildrens = radioChildrens.isSelected();

        boxTipoPneu.setVisible(ehMountain);
        boxTipoPneu.setManaged(ehMountain);

        boxTamanhoQuadro.setVisible(ehSpeed);
        boxTamanhoQuadro.setManaged(ehSpeed);


        boxTemRodinhas.setVisible(ehChildrens);
        boxTemRodinhas.setManaged(ehChildrens);


    }

    @FXML
    private void salvarBikes()
    {
        String modelo = campoModelo.getText();
        String cor =  campoCor.getText();

        //TODO: mensagem avisando que os campos são obrigatorios aqui
        if(modelo.isEmpty() || cor.isEmpty())
        {
            return;
        }


        //TODO: Salvar no banco de dados ou em CSV aqui
        if (radioMountain.isSelected())
        {

        }
        else if (radioSpeed.isSelected())
        {

        }
        else if (radioDefault.isSelected())
        {

        }
        else if (radioChildrens.isSelected())
        {

        }
    }
}
