package com.ProjetoWheels.service;

import com.lowagie.text.Document;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService
{
    private final String user = "biketronn3000@gmail.com";
    private final String password = "chvppvlylwfinkxh";

    public void enviarEmail(String para, String assunto, Document corpo)
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

            Transport.send(message);

            System.out.println("E-mail enviado com sucesso!");

        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }

}
