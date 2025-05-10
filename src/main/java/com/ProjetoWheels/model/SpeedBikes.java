package com.ProjetoWheels.model;


import com.ProjetoWheels.enums.bikes.StatusBikes;
import lombok.*;
import com.ProjetoWheels.enums.bikes.TamanhoQuadro;

@Getter
@Setter

public class SpeedBikes extends Bikes {

    private double peso;
    private TamanhoQuadro tamanhoQuadro;


    public SpeedBikes(String modelo, String cor, StatusBikes statusDisponibilidade)
    {
        super(modelo, cor, statusDisponibilidade);
    }

    @Override
    public double calcularPreço() {
        return 0;
    }

    @Override
    public boolean verificarStatusDisponibilidade() {
        return false;
    }
}
