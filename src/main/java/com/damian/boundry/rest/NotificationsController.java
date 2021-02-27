package com.damian.boundry.rest;

import com.damian.domain.notification.Notification;
import com.damian.domain.notification.NotificationDao;
import com.damian.domain.user.User;
import com.damian.domain.user.UserRepository;
import com.damian.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@RestController
public class NotificationsController {

    public static final List<SseEmitter> emitters = Collections.synchronizedList( new ArrayList<>());
    public static final List<SseEmitter> newOrderEmitters = Collections.synchronizedList( new ArrayList<>());

    private NotificationDao notificationDao;
    private UserRepository userRepository;

    public NotificationsController(NotificationDao notificationDao, UserRepository userRepository) {
        this.notificationDao = notificationDao;
        this.userRepository = userRepository;
    }

    @GetMapping("/notificationslist")
    public ResponseEntity<List<Notification>> getNotificationsForCurrentUser() {

        System.out.println("notificationslist ....");
        Optional<User> userTmp = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        System.out.println("user ....xx" + userTmp.get().getLogin());
        List<Notification> notificationList;
        if (userTmp.isPresent()) {
            notificationList = notificationDao.getNotificationByUser((userTmp.get().getId().intValue()));
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("OK...." + userTmp.get().getLogin());
        return new ResponseEntity<>(notificationList, HttpStatus.OK);
    }

    @GetMapping("/notifications/count")
    public ResponseEntity<Long> getNotificationsCount() {
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        Long notificationsCount;
        if (currentUser.isPresent()) {
            notificationsCount = notificationDao.getCountOfAllNotification((currentUser.get().getId().intValue()));
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(notificationsCount, HttpStatus.OK);
    }

    @GetMapping("/notifications/markasreaded/{id}")
    public ResponseEntity markNotifyAsReaded(@PathVariable Long id) {
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        Long ALL_NOTIFICATIONS = 0L;
        if (id.equals(ALL_NOTIFICATIONS)) {
            if (currentUser.isPresent()) {
                notificationDao.markAllAsReaded(currentUser.get().getId());
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            notificationDao.markAsReaded(id);
        }
        return new ResponseEntity<>("Oznaczono jako przeczytane", HttpStatus.OK);
    }

    @RequestMapping(path = "/notification", method = RequestMethod.GET)
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        return emitter;
    }

    @RequestMapping(path = "/new_order_notification", method = RequestMethod.GET)
    public SseEmitter streamNewOrderEmit() {
        SseEmitter newOrderEmitter = new SseEmitter(Long.MAX_VALUE);
        newOrderEmitters.add(newOrderEmitter);
        newOrderEmitter.onCompletion(() -> newOrderEmitters.remove(newOrderEmitter));
        return newOrderEmitter;
    }

}
