package com.ProjetoWheels.enums.bikes;

public enum TamanhoQuadro
{

    XS,  // Extra pequeno (geralmente 47-49 cm)
    S,   // Pequeno (50-52 cm)
    M,   // Médio (53-55 cm)
    L,   // Grande (56-58 cm)
    XL,  // Extra grande (59-61 cm)
    XXL; // Muito grande (62 cm+)

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
