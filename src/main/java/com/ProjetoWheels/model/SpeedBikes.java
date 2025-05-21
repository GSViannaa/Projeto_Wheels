package com.ProjetoWheels.model;


import com.ProjetoWheels.enums.bikes.StatusBikes;
import lombok.*;
import com.ProjetoWheels.enums.bikes.TamanhoQuadro;

@Getter
@Setter

public class SpeedBikes extends Bikes {

    private double peso;
    private TamanhoQuadro tamanhoQuadro;


    public SpeedBikes(String modelo, String cor, StatusBikes statusBikes, TamanhoQuadro tamanhoQuadro)
    {
        super(modelo, cor);
        this.tamanhoQuadro = tamanhoQuadro;
    }

    public SpeedBikes(int id, String modelo, String cor, TamanhoQuadro tamanhoQuadro)
    {
        super(id, modelo, cor);
        this.tamanhoQuadro = tamanhoQuadro;
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
