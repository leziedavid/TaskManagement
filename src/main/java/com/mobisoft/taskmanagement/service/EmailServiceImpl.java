package com.mobisoft.taskmanagement.service;

import java.io.File;
import java.util.Properties;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmail(String email, String otp) {
        // Create an instance of Java Mail sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();


        mailSender.setHost("mail.tarafe.com"); // Replace with your SMTP server host
        mailSender.setPort(465);  // Replace with your SMTP port
        mailSender.setUsername("contact@tarafe.com"); // Replace with your SMTP username
        mailSender.setPassword("mp-Tarafe@2024"); // Replace with your SMTP password

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.debug", "true");


        JavaMailSender javaMailSender = mailSender;
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            mimeMessage.setFrom(new InternetAddress("contact@tarafe.com"));
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

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        javaMailSender.send(preparator);
    }

}
