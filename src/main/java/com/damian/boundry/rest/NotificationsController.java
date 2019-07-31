package com.damian.boundry.rest;

import com.damian.domain.notification.Notification;
import com.damian.domain.notification.NotificationDao;
import com.damian.domain.order.OrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationsController {
    private NotificationDao notificationDao;

    public NotificationsController(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    @CrossOrigin
    @GetMapping("/notificationslist")
    ResponseEntity<List<Notification>> getNotifications() {
        List<Notification> notificationList = notificationDao.findAll();
        return new ResponseEntity<List<Notification>>(notificationList , HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/notifications/count")
    ResponseEntity<Long> getNotificationsCount() {
        Long notificationsCount = notificationDao.count();
        return new ResponseEntity<Long>(notificationsCount , HttpStatus.OK);
    }

}
