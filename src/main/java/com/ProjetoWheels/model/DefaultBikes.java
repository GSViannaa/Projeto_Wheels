package com.ProjetoWheels.model;

import com.ProjetoWheels.enums.bikes.StatusBikes;
import lombok.*;

@Getter
@Setter
public class DefaultBikes  extends Bikes{

    public DefaultBikes(String modelo, String cor, StatusBikes statusDisponibilidade)
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
