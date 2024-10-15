package com.mobisoft.taskmanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.mobisoft.taskmanagement.service.ImplAuthService;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private ImplAuthService implAuthService;
    @Autowired

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
    //     httpSecurity.csrf(AbstractHttpConfigurer::disable)
    //             .cors(Customizer.withDefaults())
    //             .authorizeHttpRequests(request -> request
    //             .requestMatchers("/api/**", "/public/**").permitAll()
    //             // .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
    //             .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
    //             .anyRequest().authenticated()
    //         );

    //             // .sessionManagement(manager->manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //             // .authenticationProvider(authenticationProvider()).addFilterBefore(
    //             //         jwtAuthFilter, UsernamePasswordAuthenticationFilter.class
    //             // );
    //     return httpSecurity.build();
    // }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // Configuration CORS par défaut
                .authorizeHttpRequests(request -> request
                    // .requestMatchers("/statistique/global").permitAll() // Permet l'accès à cet endpoint
                    .requestMatchers("/api/**", "/public/**").permitAll() // Autoriser ces chemins sans authentification
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll() // Autoriser Swagger UI
                    .anyRequest().authenticated() // Toutes les autres requêtes nécessitent une authentification
                );
        return httpSecurity.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(implAuthService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }


}
