package com.mobisoft.taskmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobisoft.taskmanagement.dto.NotificationDTO;
import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.service.NotificationService;

@RestController
@Validated
@RequestMapping("/api/v1")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notifications/getAllNotifications")
    public ResponseEntity<BaseResponse<List<NotificationDTO>>> getAllNotifications() {
        List<NotificationDTO> notifications = notificationService.getAllNotifications();
        BaseResponse<List<NotificationDTO>> response = new BaseResponse<>(200, "Liste des notifications", notifications);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/notifications/getNotificationById/{id}")
    public ResponseEntity<BaseResponse<NotificationDTO>> getNotificationById(@PathVariable Long id) {
        NotificationDTO notification = notificationService.getNotificationById(id);
        BaseResponse<NotificationDTO> response = new BaseResponse<>(200, "Détails de la notification", notification);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/notifications/getUnreadNotifications")
    public ResponseEntity<BaseResponse<List<NotificationDTO>>> getUnreadNotifications() {
        List<NotificationDTO> notifications = notificationService.getUnreadNotifications();
        BaseResponse<List<NotificationDTO>> response = new BaseResponse<>(200, "Liste des notifications non lues", notifications);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/notifications/getByProjectId/{projectId}")
    public ResponseEntity<BaseResponse<List<NotificationDTO>>> getNotificationsByProjectId(@PathVariable String projectId) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByProjectId(projectId);
        BaseResponse<List<NotificationDTO>> response = new BaseResponse<>(200, "Liste des notifications pour le projet", notifications);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
