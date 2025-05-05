package com.ProjetoWheels.ui;

import com.ProjetoWheels.enums.bikes.StatusBikes;
import com.ProjetoWheels.model.Bikes;

import javafx.fxml.*;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TelaAdminController implements Initializable
{
     @FXML private TableView<Bikes> tabelaBikes;

     @FXML private TableColumn<Bikes, Integer> colunaId;
     @FXML private TableColumn<Bikes, String> colunaCor;
     @FXML private TableColumn<Bikes, String> colunaModelo;
     @FXML private TableColumn<Bikes, String> colunaTipo;
     @FXML private TableColumn<Bikes, StatusBikes> colunaStatus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        colunaId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject()
        );

        //TODO: fazere com as outras colunas. OBS: Resolver problema para pegar o nome da classe não a posição em memoria

    }

    @FXML
    private void handleAdicionar()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ProjetoWheels/ui/fxml/adicionar.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Adicionar Nova Bike");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }
        catch (Exception e)
        {
            //TODO: mensagem de erro aqui!
        }

    }
}
