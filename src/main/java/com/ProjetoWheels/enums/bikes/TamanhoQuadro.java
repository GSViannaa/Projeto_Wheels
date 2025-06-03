package com.ProjetoWheels.enums.bikes;

public enum TamanhoQuadro
{
    XS(25.0),  // Extra pequeno (geralmente 47-49 cm)
    S(30.0),   // Pequeno (50-52 cm)
    M(35.0),   // Médio (53-55 cm)
    L(40.0),   // Grande (56-58 cm)
    XL(40.0),  // Extra grande (59-61 cm)
    XXL(40.0); // Muito grande (62 cm+)

    private final double preco;
    TamanhoQuadro(double preco) {this.preco = preco;}
    public double getPreco() {return preco;}

    public String getDescricao()
    {
        return switch (this)
        {
            case XS -> "Extra Pequeno (47-49 cm)";
            case S -> "Pequeno (50-52 cm)";
            case M -> "Médio (53-55 cm)";
            case L -> "Grande (56-58 cm)";
            case XL -> "Extra Grande (59-61 cm)";
            case XXL -> "XX Grande (62+ cm)";

        };
    }
}
