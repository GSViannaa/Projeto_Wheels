package com.ProjetoWheels.model;

import lombok.*;
import com.ProjetoWheels.enums.bikes.StatusBikes;

@Getter
@Setter

public abstract class Bikes
{
    protected int id;
    protected String modelo;
    protected String cor;
    protected StatusBikes statusDisponibilidade = StatusBikes.DISPONIVEL;
    protected double preco;

    public Bikes(String modelo, String cor)
    {
        this.modelo = modelo;
        this.cor = cor;
    }

    public Bikes(int id, String modelo, String cor)
    {
        this.id = id;
        this.modelo = modelo;
        this.cor = cor;

    }
    public abstract double calcularPreco();
    public abstract boolean verificarStatusDisponibilidade();
}
