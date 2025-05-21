package com.ProjetoWheels.model;

import com.ProjetoWheels.enums.bikes.StatusBikes;
import com.ProjetoWheels.enums.bikes.TemRodinhas;
import lombok.*;

@Getter
@Setter

public class ChildrensBikes  extends Bikes
{
    private TemRodinhas temRodinhas;


    public ChildrensBikes(String modelo, String cor,  TemRodinhas temRodinhas)
    {
        super(modelo, cor);
        this.temRodinhas = temRodinhas;
    }

    public ChildrensBikes(int id, String modelo, String cor, TemRodinhas temRodinhas)
    {
        super(id, modelo, cor);
        this.temRodinhas = temRodinhas;
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
