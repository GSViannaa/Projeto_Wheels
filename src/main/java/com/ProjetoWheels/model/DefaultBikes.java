package com.ProjetoWheels.model;

import lombok.*;

@Getter
@Setter
public class DefaultBikes  extends Bikes{

    public DefaultBikes(String modelo, String cor, boolean statusDisponibilidade)
    {
        super(modelo, cor, statusDisponibilidade);
    }

    @Override
    public double calcularPreço()
    {
        return 0;
    }

    @Override
    public boolean verificarStatusDisponibilidade()
    {
        return false;
    }
}
