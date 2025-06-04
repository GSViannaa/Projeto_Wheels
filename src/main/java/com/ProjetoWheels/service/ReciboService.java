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

    public static Document gerarPDF(String texto)
    {
        Document document = new Document();

        try
        {
            PdfWriter.getInstance(document, new FileOutputStream("exemplo.pdf"));
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
        return document;
    }
}
