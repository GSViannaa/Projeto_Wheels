package com.ProjetoWheels.service;

import com.ProjetoWheels.model.Bikes;
import com.ProjetoWheels.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AluguelService
{
    private List<Bikes> listaBikes;
    private Cliente cliente;
    private LocalDate dataInicio;
    private LocalDate dataFim;


    public double calcularTotal()
    {
        long dias = ChronoUnit.DAYS.between(dataInicio, dataFim);

        if (dias <= 0) dias = 1;

        double total = 0;

        for(Bikes b : listaBikes)
        {
            total += b.calcularPreco();
        }

        return total;
    }

}
