package com.ProjetoWheels.enums.bikes;

public enum TemRodinhas {
    NAO(30.0),
    SIM(30.0);

    private final double preco;
    TemRodinhas(double preco) {this.preco = preco;}
    public  double getPreco() {return preco;}
}
