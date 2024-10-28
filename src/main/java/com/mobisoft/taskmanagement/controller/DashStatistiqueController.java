package com.mobisoft.taskmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.dto.GlobalStatsDTO;
import com.mobisoft.taskmanagement.service.DashStatisticsService;

@RestController
@RequestMapping("/api/v1")
public class DashStatistiqueController {

    @Autowired
    private DashStatisticsService dashStatisticsService;

    // @GetMapping("/statistique/global")
    // public ResponseEntity<BaseResponse<GlobalStatsDTO>> getGlobalStats(@RequestHeader("Authorization") String authorizationHeader) {
    //     // Extrait le token JWT en enlevant le préfixe "Bearer "
    //     String token = authorizationHeader.substring(7); // Remove "Bearer "
    //     GlobalStatsDTO stats = dashStatisticsService.getGlobalStats(token);
    //     BaseResponse<GlobalStatsDTO> response = new BaseResponse<>(HttpStatus.OK.value(), "Global statistics", stats);
    //     return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    @GetMapping("/statistique/global")
    public ResponseEntity<BaseResponse<GlobalStatsDTO>> getGlobalStats(@RequestHeader("Authorization") String authorizationHeader,
        @RequestHeader(value = "userId", required = false) String userIdStr) {
        // Extrait le token JWT en enlevant le préfixe "Bearer "
        String token = authorizationHeader.substring(7); // Remove "Bearer "

        Long userId = null;
        if (userIdStr != null) {
            try {
                userId = Long.valueOf(userIdStr); // Convertit le String en Long
            } catch (NumberFormatException e) {
                // Gérer l'erreur de conversion si nécessaire
                System.err.println("Invalid User-ID format: " + userIdStr);
            }
        }
        GlobalStatsDTO stats = dashStatisticsService.getGlobalStats(token, userId);

        BaseResponse<GlobalStatsDTO> response = new BaseResponse<>(HttpStatus.OK.value(), "Global statistics", stats);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/statistique/projetStats")
    public ResponseEntity<BaseResponse<GlobalStatsDTO>> getProjetStats(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestHeader(value = "userId", required = false) String userIdStr) {

        try {
            // Extrait le token JWT en enlevant le préfixe "Bearer "
            String token = authorizationHeader.startsWith("Bearer ")?
            authorizationHeader.substring(7): authorizationHeader;

            Long userId = null;
            if (userIdStr != null) {
                try {
                    userId = Long.valueOf(userIdStr); // Convertit le String en Long
                } catch (NumberFormatException e) {
                    // Gérer l'erreur de conversion si nécessaire
                    System.err.println("Invalid User-ID format: " + userIdStr);
                }
            }

            // Obtient les statistiques globales en appelant le service
            GlobalStatsDTO stats = dashStatisticsService.getProjectStatistics(token,userId);
            // Crée une réponse encapsulée dans BaseResponse
            BaseResponse<GlobalStatsDTO> response = new BaseResponse<>(HttpStatus.OK.value(), "Global statistics", stats);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Gère les exceptions en retournant une réponse d'erreur
            BaseResponse<GlobalStatsDTO> errorResponse = new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erreur lors de la récupération des statistiques", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
