package com.ProjetoWheels.model;


import com.ProjetoWheels.enums.bikes.StatusBikes;
import lombok.*;
import com.ProjetoWheels.enums.bikes.TamanhoQuadro;

@Getter
@Setter

public class SpeedBikes extends Bikes {

    private TamanhoQuadro bikeTamanhoQuadro;
    private double preco = bikeTamanhoQuadro.getPreco();


    public SpeedBikes(String modelo, String cor, StatusBikes statusBikes, TamanhoQuadro tamanhoQuadro)
    {
        super(modelo, cor);
        this.bikeTamanhoQuadro = tamanhoQuadro;
    }

    public SpeedBikes(int id, String modelo, String cor, TamanhoQuadro tamanhoQuadro)
    {
        super(id, modelo, cor);
        this.bikeTamanhoQuadro = tamanhoQuadro;
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
