package com.ProjetoWheels.model;

import com.ProjetoWheels.enums.bikes.StatusBikes;
import lombok.*;

@Getter
@Setter
public class DefaultBikes  extends Bikes{

    private static final double precoFixo = 40.0;

    public DefaultBikes(String modelo, String cor,  StatusBikes statusDisponibilidade)
    {
        super(modelo, cor);
    }

    public DefaultBikes(int id, String modelo, String cor)
    {
        super(id, modelo, cor);
    }

    @Override
    public double calcularPreco()
    {
        return precoFixo;
    }

    @Override
    public boolean verificarStatusDisponibilidade()
    {
        return false;
    }
}
