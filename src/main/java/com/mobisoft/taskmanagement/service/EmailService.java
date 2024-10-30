package com.mobisoft.taskmanagement.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    // void sendEmail(String email, String otp);
    void sendEmail(String email, String otp) throws MessagingException; // Déclarez l'exception ici


}
