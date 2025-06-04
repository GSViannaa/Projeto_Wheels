package com.ProjetoWheels.service;

import com.lowagie.text.Document;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class EmailService
{
    private final String user = "biketronn3000@gmail.com";
    private final String password = "chvppvlylwfinkxh";

    public void enviarEmail(String para, String assunto, String caminhoDoArquivoPdf)
    {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(user, password);
            }
        });

        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(para)
            );
            message.setSubject(assunto);

            MimeBodyPart textoPart = new MimeBodyPart();
            textoPart.setText("Segue em anexo o recibo do seu aluguel.");

            MimeBodyPart anexoPart = new MimeBodyPart();
            anexoPart.attachFile(new File(caminhoDoArquivoPdf));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textoPart);
            multipart.addBodyPart(anexoPart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("E-mail enviado com sucesso!");

        }
        catch (MessagingException | IOException e)
        {
            e.printStackTrace();
        }
    }

}
