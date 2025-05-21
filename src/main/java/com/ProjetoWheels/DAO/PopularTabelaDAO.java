package com.ProjetoWheels.DAO;

import com.ProjetoWheels.enums.bikes.StatusBikes;
import com.ProjetoWheels.enums.bikes.TamanhoQuadro;
import com.ProjetoWheels.enums.bikes.TipoPneu;
import com.ProjetoWheels.model.MountainBike;
import com.ProjetoWheels.model.SpeedBikes;

public class PopularTabelaDAO
{
    public static void popularTabela()
    {
        MountainBike mtb = new MountainBike( "Caloi Explorer", "Preta", StatusBikes.DISPONIVEL, TipoPneu.CRAVADO);
        BikesDAO.salvarNoBancoDeDados(mtb);
        System.out.println("MountainBike salva com sucesso!");

        SpeedBikes speed = new SpeedBikes( "Specialized Tarmac", "Azul",StatusBikes.DISPONIVEL, TamanhoQuadro.M);
        BikesDAO.salvarNoBancoDeDados(speed);
        System.out.println("SpeedBike salva com sucesso!");
    }
}
