package com.ProjetoWheels.model;

import lombok.*;

@Getter
@Setter

public class ChildrensBikes  extends Bikes
{
    private boolean temRodinhas;


    public ChildrensBikes(String modelo, String cor, boolean statusDisponibilidade)
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
