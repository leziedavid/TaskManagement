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
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.repository.UserRepository;

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



    public AuthDTO login(AuthDTO loginRequest) {
        AuthDTO response = new AuthDTO();
        
        try {
            // Tentative d'authentification
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            // Recherche de l'utilisateur par e-mail
            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
            var jwt = jwtUtils.generateToken(user);
            // var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setToken(jwt);
            response.setFonction(user.getFonction());
            return response;

        } catch (AuthenticationException e) {

            throw new EntityNotFoundException();

        } catch (RuntimeException e) {

            throw new EntityNotFoundException();
        }

        
    }


    public AuthDTO ResetPassword(AuthDTO data)  {

        Optional<User> optionalUser = usersRepo.findByEmail(data.getEmail());
        User user = optionalUser.get();

        if (user.getOtp() == data.getOtp()) {
            
            user.setPassword(passwordEncoder.encode(data.getPassword()));
            System.out.println(passwordEncoder.encode(data.getPassword()));
            usersRepo.save(user);
    
            AuthDTO response = new AuthDTO();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Votre mot de passe a été changé avec succès.");
            return response;

        }else{

            user.setPassword(passwordEncoder.encode(data.getPassword()));
            System.out.println(passwordEncoder.encode(data.getPassword()));
            usersRepo.save(user);
            AuthDTO response = new AuthDTO();
            response.setStatus(HttpStatus.OK.value());
            // response.setMessage("Votre OTP a expiré. Merci d'aller sur la page de réinitialisation pour générer un autre code OTP.");
            return response;
        }


    }

    public AuthDTO sendOTPByEmail(AuthDTO data) throws jakarta.mail.MessagingException {

        Optional<User> optionalUser = usersRepo.findByEmail(data.getEmail());

        // System.out.println(optionalUser);
        
        if (optionalUser.isEmpty()) {
            AuthDTO response = new AuthDTO();
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("Cet email n'existe pas dans la base de données.");
            return response;
        }

        User user = optionalUser.get(); // Obtenez l'utilisateur depuis Optional<User>
        
        int otp = generateOTP();
        String otpString = String.valueOf(otp);

        user.setOtp(otp);
        usersRepo.save(user);

        emailService.sendEmail(data.getEmail(), otpString);

        AuthDTO response = new AuthDTO();
        response.setOtp(otp);
        response.setMessage("Veuillez consulter votre email, un code de validation vous a été transmis. Ce code expirera dans 3 minutes.");
        return response;

    }

    // Méthode pour générer un OTP
    private int generateOTP() {
        return (int) ((Math.random() * (9999 - 1000)) + 1000);
    }

    public AuthDTO verifyOTP(AuthDTO data) {
        // Récupérer l'utilisateur par email
        Optional<User> userOptional = usersRepo.findByEmail(data.getEmail());
        // Obtenir l'utilisateur depuis Optional
        User user = userOptional.get();
        // Vérifier si l'OTP saisi correspond à celui dans la base de données
        if (user.getOtp() == data.getOtp()) {
            // Réinitialisation réussie, retourner un message de succès avec l'OTP
            AuthDTO response = new AuthDTO();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Votre OTP est valide.");
            // response.setOtp(user.getOtp());
            return response;

        } else {

            // OTP incorrect, retourner un message d'erreur
            AuthDTO response = new AuthDTO();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Le code OTP saisi est incorrect.");
            return response;
        }
    }

    public AuthDTO refreshToken(AuthDTO refreshTokenReqiest) {
        AuthDTO response = new AuthDTO();
        try {
            String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
            User users = usersRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                response.setStatus(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenReqiest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatus(200);
            return response;

        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }


    public AuthDTO logout() {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setToken(null);
        AuthDTO response = new AuthDTO();
        response.setStatus(200);
        response.setMessage("Successfully logged out");
        return response;
    }

    public AuthDTO getAllUsers() {
        AuthDTO AuthDTO = new AuthDTO();

        try {
            List<User> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                // AuthDTO.setUserList(result);
                AuthDTO.setStatus(200);
                AuthDTO.setMessage("Successful");
            } else {
                AuthDTO.setStatus(404);
                AuthDTO.setMessage("No users found");
            }
            return AuthDTO;
        } catch (Exception e) {
            AuthDTO.setStatus(500);
            AuthDTO.setMessage("Error occurred: " + e.getMessage());
            return AuthDTO;
        }
    }

    // public AuthDTO getMyInfo(String email) {
    //     AuthDTO AuthDTO = new AuthDTO();
    //     try {
    //         Optional<User> userOptional = usersRepo.findByEmail(email);
    //         if (userOptional.isPresent()) {
    //             AuthDTO.setUser(userOptional.get());
    //             AuthDTO.setStatus(200);
    //             AuthDTO.setMessage("successful");
    //         } else {
    //             AuthDTO.setStatus(404);
    //             AuthDTO.setMessage("User not found for update");
    //         }

    //     } catch (Exception e) {
    //         AuthDTO.setStatus(500);
    //         AuthDTO.setMessage("Error occurred while getting user info: " + e.getMessage());
    //     }
    //     return AuthDTO;

    // }
    
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

    /**
     * @return PasswordEncoder return the passwordEncoder
     */
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * @param passwordEncoder the passwordEncoder to set
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

}
