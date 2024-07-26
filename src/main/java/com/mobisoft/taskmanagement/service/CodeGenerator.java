package com.mobisoft.taskmanagement.service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class CodeGenerator {

    private static final Set<String> generatedCodes = new HashSet<>();

    // Méthode pour générer un code produit unique
    public static String generateUniqueProjectCode() {
        String code;
        do {
            code = generateCode();
        } while (!isUnique(code));

        generatedCodes.add(code);
        return code;
    }

    private static String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Génère 3 lettres aléatoires
        for (int i = 0; i < 3; i++) {
            char randomLetter = (char) (random.nextInt(26) + 'A'); // 'A' = 65, 'Z' = 90
            sb.append(randomLetter);
        }

        // Génère 2 chiffres aléatoires
        for (int i = 0; i < 2; i++) {
            int randomDigit = random.nextInt(10); // 0 à 9
            sb.append(randomDigit);
        }

        return sb.toString();
    }

    private static boolean isUnique(String code) {
        return !generatedCodes.contains(code);
    }
}
