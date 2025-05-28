package com.ProjetoWheels.model;

import com.ProjetoWheels.enums.bikes.StatusBikes;
import com.ProjetoWheels.enums.bikes.TipoPneu;
import lombok.*;

@Getter
@Setter

public class MountainBike extends  Bikes {

    private TipoPneu bikeTipoPneu;
    private double preco;


    public MountainBike(String modelo, String cor, StatusBikes statusDisponibilidade, TipoPneu tipoPneu)
    {
        super(modelo, cor);
        this.bikeTipoPneu = tipoPneu;
        preco = bikeTipoPneu.getPreco();
    }

    public MountainBike(int id, String modelo, String cor,  TipoPneu tipoPneu)
    {
        super(id, modelo, cor);
        this.bikeTipoPneu = tipoPneu;
    }

    @Override
    public double calcularPreço() {
        return preco;
    }

    @Override
    public boolean verificarStatusDisponibilidade() {
        return false;
    }

    @Override
    public String toString() {
        return "MountainBike{" + "id=" + id + ", cor=" + cor + ", modelo='" + modelo + '\'' + ", tipoPneu=" + bikeTipoPneu + ", status=" + statusDisponibilidade + '}';
    }
}
