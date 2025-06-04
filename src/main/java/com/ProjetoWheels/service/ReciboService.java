package com.ProjetoWheels.service;

import com.ProjetoWheels.chat.Biketron3000;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class ReciboService
{

    public static String gerarPDF(String texto)
    {
        String caminho = "Recibo.pdf";

        Document document = new Document();

        try
        {
            PdfWriter.getInstance(document, new FileOutputStream(caminho));
            document.open();
            document.add(new Paragraph(texto));
        }
        catch (DocumentException | IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            document.close();
        }

        return caminho;
    }
}
