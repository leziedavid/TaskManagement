package com.mobisoft.taskmanagement.service;

import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Service  // Ajoutez cette annotation
public class SendEmailServices {

    public void sendEmailViaApi(String senderId, Long userId, String subject, String receiver, String message) {
        try {
            // Création de l'URI
            URI uri = new URI("http://84.247.170.134:7051/api/v1/broker/publish/email");
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // Configuration de la connexion
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            // Corps de la requête
            String jsonInputString = String.format(
                "{\"senderId\": \"%s\", \"userId\": %d, \"resourceIds\": \"1,1,4\", \"subject\": \"%s\", \"receivers\": [\"%s\"], \"message\": \"%s\"}",
                senderId, userId, subject, receiver, message
            );
            
            // Envoi de la requête
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            // Vérification de la réponse
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Erreur lors de l'envoi de l'email : " + responseCode);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
