package com.mobisoft.taskmanagement.service;

import java.io.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String email, String otp) {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            mimeMessage.setFrom(new InternetAddress("mobitask@mobisoft.ci"));
            mimeMessage.setSubject("Code OTP pour réinitialisation de mot de passe");
            mimeMessage.setText("Votre code OTP est : " + otp + ". Ce code expirera dans 3 minutes.");

            // Ajout d'une pièce jointe, si nécessaire
            String attachmentPath = ""; // Remplacer par le chemin du fichier si nécessaire
            if (!attachmentPath.isEmpty()) {
                FileSystemResource file = new FileSystemResource(new File(attachmentPath));
                if (file.exists() && file.getFile().length() > 0) {
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                    helper.addAttachment(file.getFilename(), file);
                }
            }
        };

        // Envoi du message avec gestion des exceptions
        try {
            mailSender.send(preparator);
        } catch (MailException e) {
            // Gérer les exceptions d'envoi d'email
            e.printStackTrace(); // Journaliser l'erreur lors de l'envoi de l'email
        }
    }

    @Override
    public void sendEmail2(String email, String otp) {
        // Create an instance of Java Mail sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();


        mailSender.setHost("mail.mobisoft.ci"); // Replace with your SMTP server host
        mailSender.setPort(465);  // Replace with your SMTP port
        mailSender.setUsername("mobitask@mobisoft.ci"); // Replace with your SMTP username
        mailSender.setPassword("_qpv0M044"); // Replace with your SMTP password


        JavaMailSender javaMailSender = mailSender;
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            mimeMessage.setFrom(new InternetAddress("mobitask@mobisoft.ci"));
            mimeMessage.setSubject("Code OTP pour réinitialisation de mot de passe");
            mimeMessage.setText("Votre code OTP est : " + otp + ". Ce code expirera dans 3 minutes.");

            String attachment = "";
            // String attachment = "uploads/names.csv";

            try {
                FileSystemResource file = new FileSystemResource(new File(attachment));

                if (file.exists() && file.getFile().length() > 0) {
                    // Check if file exists and is not empty
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                    helper.addAttachment(file.getFilename(), file);
                    helper.setText("", true);

                } else {
                    // System.out.println("File is empty or not found: " + attachment);
                }

            } catch (MessagingException ex) {
            }
        };

        javaMailSender.send(preparator);
    }



}
