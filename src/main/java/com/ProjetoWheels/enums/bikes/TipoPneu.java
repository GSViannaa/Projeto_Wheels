package com.ProjetoWheels.enums.bikes;

public enum TipoPneu
{
    LISO(40.0),          // uso urbano ou estrada
    MISTO(40.0),         // meio-termo, trilhas leves
    CRAVADO(45.0),       // trilhas pesadas, lama, terra
    FATTIRE(45.0);       // pneus extra largos

    private final double preco;

    TipoPneu(double preco)
    {
        this.preco = preco;
    }
    public double getPreco()
    {
        return preco;
    }
}
