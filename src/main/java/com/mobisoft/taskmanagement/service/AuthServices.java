package com.mobisoft.taskmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobisoft.taskmanagement.dto.AuthDTO;
import com.mobisoft.taskmanagement.dto.UserDTO;
import com.mobisoft.taskmanagement.entity.User; // Importer l'énumération Role
import com.mobisoft.taskmanagement.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class AuthServices {

    @Autowired
    private UserRepository usersRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // Récupérer l'ID de l'utilisateur à partir du token JWT
    public Long getUserIdFromToken(String token) {
        return usersRepo.findByToken(token).orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé pour le token : " + token)).getUserId();
    }

    // Récupérer toutes les informations de l'utilisateur à partir du token JWT
    public UserDTO getUserInfoFromToken(String token) {
        return usersRepo.findByToken(token).map(this::convertToUserDTO).orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé pour le token : " + token));
    }

    // Méthode utilitaire pour convertir User en UserDTO
    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setLastname(user.getLastname());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setUsername(user.getUsername());
        userDTO.setFonction(user.getFonction());
        userDTO.setGenre(user.getGenre().toString()); // Convertir Enum en String
        userDTO.setRole(user.getRole()); // Ajouter le rôle
        return userDTO;
    }

    public AuthDTO login(AuthDTO loginRequest) {
        
        AuthDTO response = new AuthDTO();
        
        try {
            // Tentative d'authentification
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            
            // Recherche de l'utilisateur par e-mail
            User user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
            
            // Génération du token JWT
            String jwt = jwtUtils.generateToken(user);
            
            // Mettre à jour le token dans l'objet user
            user.setToken(jwt);
            
            // Sauvegarder l'utilisateur mis à jour dans la base de données
            usersRepo.save(user);
            
            // Mettre à jour la réponse avec le token et d'autres informations nécessaires
            response.setToken(jwt);
            response.setFonction(user.getFonction());
            response.setRole(user.getRole().name());
            response.setUserId(user.getUserId());

            // Récupérer le prénom et le nom de famille
            String firstname = user.getFirstname();
            String lastname = user.getLastname();
            // Concaténer les valeurs
            String fullName = firstname + " " + lastname;
            // Transmettre la valeur concaténée à la réponse
            response.setUsername(fullName);
            response.setProfil(user.getProfil());
            
            return response;
            
        } catch (AuthenticationException e) {
            throw new EntityNotFoundException("Authentification échouée");
        } catch (RuntimeException e) {
            throw new EntityNotFoundException("Erreur lors de la tentative d'authentification");
        }
    }
    
    
    public AuthDTO resetPassword(AuthDTO data)  {
        
        Optional<User> optionalUser = usersRepo.findByEmail(data.getEmail());
        User user = optionalUser.orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        if (user.getOtp() == data.getOtp()) {
            user.setPassword(passwordEncoder.encode(data.getPassword()));
            usersRepo.save(user);
    
            AuthDTO response = new AuthDTO();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Votre mot de passe a été changé avec succès.");
            return response;
        } else {
            AuthDTO response = new AuthDTO();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Votre OTP est incorrect.");
            return response;
        }
    }

    // public AuthDTO sendOTPByEmail(AuthDTO data) throws jakarta.mail.MessagingException {
    //     Optional<User> optionalUser = usersRepo.findByEmail(data.getEmail());
    //     System.err.println("Error attraper  : " + optionalUser);

    //     if (optionalUser.isEmpty()) {
    //         AuthDTO response = new AuthDTO();
    //         response.setStatus(HttpStatus.NOT_FOUND.value());
    //         response.setMessage("Cet email n'existe pas. Veuillez fournir un email valide.");
    //         return response;
    //     }



    //     User user = optionalUser.get();

    //     int otp = generateOTP();
    //     String otpString = String.valueOf(otp);

    //     user.setOtp(otp);
    //     usersRepo.save(user);

    //     emailService.sendEmail(data.getEmail(), otpString);

    //     AuthDTO response = new AuthDTO();
    //     response.setOtp(otp);
    //     response.setStatus(200);
    //     response.setMessage("Veuillez consulter votre email, un code de validation vous a été transmis. Ce code expirera dans 3 minutes.");
    //     return response;
    // }


    public AuthDTO sendOTPByEmail(AuthDTO data) throws MessagingException {
        // Recherche de l'utilisateur par email
        Optional<User> optionalUser = usersRepo.findByEmail(data.getEmail());
        
        // Vérification si l'utilisateur existe
        if (optionalUser.isEmpty()) {
            AuthDTO response = new AuthDTO();
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("Cet email n'existe pas. Veuillez fournir un email valide.");
            return response;
        }
    
        // Récupération de l'utilisateur
        User user = optionalUser.get();
        
        // Génération d'un OTP
        int otp = generateOTP();
        user.setOtp(otp);
        usersRepo.save(user); // Enregistrement de l'OTP
    
        // Envoi de l'email
        emailService.sendEmail(data.getEmail(), String.valueOf(otp));
    
        // Préparation de la réponse
        AuthDTO response = new AuthDTO();
        response.setOtp(otp); // Si vous souhaitez retourner l'OTP
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Veuillez consulter votre email, un code de validation vous a été transmis. Ce code expirera dans 3 minutes.");
        
        return response;
    }
    

    // Méthode pour générer un OTP
    private int generateOTP() {
        return (int) ((Math.random() * (9999 - 1000)) + 1000);
    }

    public AuthDTO verifyOTP(AuthDTO data) {

        Optional<User> userOptional = usersRepo.findByEmail(data.getEmail());
        User user = userOptional.orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        if (user.getOtp() == data.getOtp()) {
            AuthDTO response = new AuthDTO();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Votre OTP est valide.");
            return response;
        } else {
            AuthDTO response = new AuthDTO();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Le code OTP saisi est incorrect.");
            return response;
        }
    }

    public AuthDTO refreshToken(AuthDTO refreshTokenRequest) {
        AuthDTO response = new AuthDTO();
        try {
            String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            User user = usersRepo.findByEmail(email).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
                String jwt = jwtUtils.generateToken(user);
                response.setStatus(HttpStatus.OK.value());
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Token rafraîchi avec succès.");
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Token invalide.");
            }
            return response;
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public AuthDTO logout(String token) {
        AuthDTO response = new AuthDTO();
        try {
            Optional<User> userOptional = usersRepo.findByToken(token);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setToken(null);
                usersRepo.save(user);

                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Déconnexion réussie");
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("Utilisateur non trouvé pour le token spécifié");
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Erreur lors de la déconnexion : " + e.getMessage());
        }
        return response;
    }

    public AuthDTO getAllUsers() {
        AuthDTO response = new AuthDTO();
        try {
            List<User> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                // Optionnel : Ajouter des détails supplémentaires dans AuthDTO si nécessaire
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Succès");
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("Aucun utilisateur trouvé");
            }
            return response;
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Erreur survenue : " + e.getMessage());
            return response;
        }
    }

    // Getters et Setters pour les autres dépendances
    public JWTUtils getJwtUtils() {
        return jwtUtils;
    }

    public void setJwtUtils(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
