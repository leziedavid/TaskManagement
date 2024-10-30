package com.mobisoft.taskmanagement.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage; // Assurez-vous d'importer java.io.File

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender; // Injection correcte du JavaMailSender

    @Override
    public void sendEmail(String email, String otp) throws MessagingException {
        // Log pour débugger
        System.out.println("Envoi d'un email à : " + email);
        
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            mimeMessage.setFrom(new InternetAddress("contact@tarafe.com"));
            mimeMessage.setSubject("Code OTP pour réinitialisation de mot de passe");
            mimeMessage.setText("Votre code OTP est : " + otp + ". Ce code expirera dans 3 minutes.");
    
            // Gestion des pièces jointes
            String attachmentPath = ""; // chemin de la pièce jointe
            if (!attachmentPath.isEmpty()) {
                File file = new File(attachmentPath);
                FileSystemResource fileResource = new FileSystemResource(file);
    
                if (fileResource.exists() && fileResource.isFile() && file.length() > 0) {
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                    helper.addAttachment(fileResource.getFilename(), fileResource);
                    helper.setText("Votre code OTP est : " + otp + ". Ce code expirera dans 3 minutes.", true);
                } else {
                    System.err.println("Le fichier est vide ou introuvable : " + attachmentPath);
                }
            }
        };
    
        try {
            javaMailSender.send(preparator);
        } catch (Exception e) {
            throw new MessagingException("Erreur lors de l'envoi de l'email.", e);
        }
    }

}
