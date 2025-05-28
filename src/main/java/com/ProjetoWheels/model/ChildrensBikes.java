package com.ProjetoWheels.model;

import com.ProjetoWheels.enums.bikes.StatusBikes;
import com.ProjetoWheels.enums.bikes.TemRodinhas;
import lombok.*;

@Getter
@Setter

public class ChildrensBikes extends Bikes
{
    private TemRodinhas bikeTemRodinhas;
    private double preco;


    public ChildrensBikes(String modelo, String cor, StatusBikes statusDisponibilidade, TemRodinhas temRodinhas)
    {
        super(modelo, cor);
        this.bikeTemRodinhas = temRodinhas;
        preco = bikeTemRodinhas.getPreco();
    }

    public ChildrensBikes(int id, String modelo, String cor, TemRodinhas temRodinhas)
    {
        super(id, modelo, cor);
        this.bikeTemRodinhas = temRodinhas;
    }


    @Override
    public double calcularPreço()
    {
        return preco;
    }

    @Override
    public boolean verificarStatusDisponibilidade()
    {
        return false;
    }
}
