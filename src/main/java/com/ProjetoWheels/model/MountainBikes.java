package com.ProjetoWheels.model;

import com.ProjetoWheels.enums.bikes.StatusBikes;
import com.ProjetoWheels.enums.bikes.TipoPneu;
import lombok.*;

@Getter
@Setter

public class MountainBikes extends  Bikes {

    private TipoPneu bikeTipoPneu;
    private double preco;

    public MountainBikes(String modelo, String cor, StatusBikes statusDisponibilidade, TipoPneu tipoPneu)
    {
        super(modelo, cor);
        this.bikeTipoPneu = tipoPneu;
        preco = bikeTipoPneu.getPreco();
    }

    public MountainBikes(int id, String modelo, String cor, TipoPneu tipoPneu)
    {
        super(id, modelo, cor);
        this.bikeTipoPneu = tipoPneu;
    }

    @Override
    public double calcularPreco()
    {
        preco = bikeTipoPneu.getPreco();
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
