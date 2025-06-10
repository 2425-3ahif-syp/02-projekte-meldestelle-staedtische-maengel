package com.syp.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {

    public static void sendEmail(String recipient, String subject, String content) {
        final String fromEmail = "deine.email@gmail.com";
        final String password = "deinAppPasswort"; // App-spezifisches Passwort zuerst muss registierung funktionieren

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("E-Mail erfolgreich gesendet an " + recipient);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("E-Mail konnte nicht gesendet werden.");
        }
    }
}
