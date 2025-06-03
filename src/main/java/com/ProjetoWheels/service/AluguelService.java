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
    private static double valorSeguro = 50;
    private List<Bikes> listaBikes;
    private Cliente cliente;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private double valorTotal;


    public static double calcularTotal(List<Bikes> bikes, String diasString)
    {

        double dias = Integer.parseInt(diasString);

        double total = 0;

        for(Bikes b : bikes)
        {
            total += b.calcularPreco();
        }

        return (total * dias) + valorSeguro;
    }

}
