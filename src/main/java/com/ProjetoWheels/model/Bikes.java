package com.ProjetoWheels.model;

import lombok.*;
import com.ProjetoWheels.enums.bikes.StatusBikes;

@Getter
@Setter
@AllArgsConstructor

public abstract class Bikes
{
    protected int id;
    protected String modelo;
    protected String cor;
    protected StatusBikes statusDisponibilidade;

    public Bikes(String modelo, String cor, boolean statusDisponibilidade)
    {
        this.modelo = modelo;
        this.cor = cor;
        this.statusDisponibilidade = StatusBikes.DISPONIVEL;
    }

    public abstract double calcularPreço();
    public abstract boolean verificarStatusDisponibilidade();

}
