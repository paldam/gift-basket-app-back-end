package com.damian.boundry.rest;

import com.damian.domain.notification.Notification;
import com.damian.domain.notification.NotificationDao;
import com.damian.domain.order.OrderStatus;
import com.damian.domain.user.User;
import com.damian.domain.user.UserRepository;
import com.damian.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

@RestController
public class NotificationsController {
    private NotificationDao notificationDao;
    private UserRepository userRepository;

    public NotificationsController(NotificationDao notificationDao, UserRepository userRepository) {
        this.notificationDao = notificationDao;
        this.userRepository=userRepository;
    }

    @CrossOrigin
    @GetMapping("/notificationslist")
    ResponseEntity<List<Notification>> getNotificationsForCurrentUser() {
            Optional<User> userTmp = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());



        List<Notification> notificationList = notificationDao.getNotificationByUser((userTmp.get().getId().intValue()));


        return new ResponseEntity<List<Notification>>(notificationList , HttpStatus.OK);
    }



    @CrossOrigin
    @GetMapping("/notifications/count")
    ResponseEntity<Long> getNotificationsCount() {
        Optional<User> userTmp = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        Long notificationsCount = notificationDao.getCountOfAllNotification((userTmp.get().getId().intValue()));
        return new ResponseEntity<Long>(notificationsCount , HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/notifications/markasreaded/{id}")
    ResponseEntity markNotifyAsReaded(@PathVariable Long id) {
        Optional<User> currenUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());

        if(id ==0){
            notificationDao.markAllAsReaded(currenUser.get().getId());
        }else{
            notificationDao.markAsReaded(id);
        }
        return new ResponseEntity<>("Oznaczono jako przeczytane" , HttpStatus.OK);
    }







}
