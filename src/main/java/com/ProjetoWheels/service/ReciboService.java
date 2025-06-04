package com.ProjetoWheels.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class ReciboService
{
    private Document document;

    public void gerarPDF()
    {
        try
        {
            PdfWriter.getInstance(document, new FileOutputStream("exemplo.pdf"));
            document.open();
            document.add(new Paragraph("Olá, mundo! Este é um PDF gerado com OpenPDF."));
        }
        catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        finally
        {
            document.close();
        }
    }
}
