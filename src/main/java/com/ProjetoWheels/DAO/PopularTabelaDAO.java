package com.ProjetoWheels.DAO;

import com.ProjetoWheels.enums.bikes.StatusBikes;
import com.ProjetoWheels.enums.bikes.TamanhoQuadro;
import com.ProjetoWheels.enums.bikes.TemRodinhas;
import com.ProjetoWheels.enums.bikes.TipoPneu;
import com.ProjetoWheels.model.ChildrensBikes;
import com.ProjetoWheels.model.DefaultBikes;
import com.ProjetoWheels.model.MountainBikes;
import com.ProjetoWheels.model.SpeedBikes;

public class PopularTabelaDAO
{
    public static void popularTabela()
    {
        MountainBikes mtb0 = new MountainBikes( "Caloi Explorer", "Preta", StatusBikes.DISPONIVEL, TipoPneu.CRAVADO);
        BikesDAO.salvarNoBancoDeDados(mtb0);

        SpeedBikes speed0 = new SpeedBikes( "Specialized Tarmac", "Azul",StatusBikes.DISPONIVEL, TamanhoQuadro.M);
        BikesDAO.salvarNoBancoDeDados(speed0);

        MountainBikes mtb1 = new MountainBikes("Caloi Explorer", "Preta", StatusBikes.DISPONIVEL, TipoPneu.CRAVADO);
        BikesDAO.salvarNoBancoDeDados(mtb1);


        SpeedBikes speed1 = new SpeedBikes("Specialized Tarmac", "Azul", StatusBikes.DISPONIVEL, TamanhoQuadro.M);
        BikesDAO.salvarNoBancoDeDados(speed1);


        DefaultBikes default1 = new DefaultBikes("Monark Classic", "Vermelha", StatusBikes.DISPONIVEL);
        BikesDAO.salvarNoBancoDeDados(default1);


        ChildrensBikes kids1 = new ChildrensBikes("Caloi Kids", "Amarela", StatusBikes.DISPONIVEL, TemRodinhas.SIM);
        BikesDAO.salvarNoBancoDeDados(kids1);


        MountainBikes mtb2 = new MountainBikes("Scott Scale", "Verde", StatusBikes.ALUGADA, TipoPneu.FATTIRE);
        BikesDAO.salvarNoBancoDeDados(mtb2);


        SpeedBikes speed2 = new SpeedBikes("Cannondale Synapse", "Branca", StatusBikes.DISPONIVEL, TamanhoQuadro.L);
        BikesDAO.salvarNoBancoDeDados(speed2);


        DefaultBikes default2 = new DefaultBikes("Houston Urban", "Cinza", StatusBikes.DISPONIVEL);
        BikesDAO.salvarNoBancoDeDados(default2);


        ChildrensBikes kids2 = new ChildrensBikes("BMX Infantil", "Laranja", StatusBikes.DISPONIVEL, TemRodinhas.NAO);
        BikesDAO.salvarNoBancoDeDados(kids2);


        MountainBikes mtb3 = new MountainBikes("Trek Marlin", "Preto e Azul", StatusBikes.COM_DEFEITO, TipoPneu.LISO);
        BikesDAO.salvarNoBancoDeDados(mtb3);


        SpeedBikes speed3 = new SpeedBikes("Giant Propel", "Prata", StatusBikes.DISPONIVEL, TamanhoQuadro.S);
        BikesDAO.salvarNoBancoDeDados(speed3);

    }
}
