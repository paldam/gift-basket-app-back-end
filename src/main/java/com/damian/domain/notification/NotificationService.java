package com.damian.domain.notification;

import com.damian.domain.order.Order;
import com.damian.domain.user.Authority;
import com.damian.domain.user.User;
import com.damian.domain.user.UserRepository;
import com.damian.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

@Service
public class NotificationService {

    NotificationDao notificationDao;
    UserRepository userRepository;

    public NotificationService(NotificationDao notificationDao, UserRepository userRepository) {
        this.notificationDao = notificationDao;
        this.userRepository = userRepository;
    }

    public void saveNotifications(Order order, Order originOrder) {
        List<User> userList = userRepository.getLogisticWarehouseProductionUsers();
        userList.forEach(user -> {

            System.out.println(ANSI_YELLOW + user.toString() + ANSI_RESET);


            System.out.println(ANSI_YELLOW + "Uzytkownik"  + user.getLogin() +" Ma oprawnienie produckja " +user.getAuthorities().contains(new Authority("produkcja")) + ANSI_RESET);



            if (user.getAuthorities().contains(new Authority("produkcja"))) {

                if (order.getProductionUser().getId() == user.getId()) {
                    notificationDao.saveNotifications("Dodano zamówienie nr: " + order.getOrderFvNumber() + " połączone z zamówieniem nr: " + originOrder.getOrderFvNumber(), user.getId(), originOrder.getOrderId());
                }
            } else {
                notificationDao.saveNotifications("Dodano zamówienie nr: " + order.getOrderFvNumber() + " połączone z zamówieniem nr: " + originOrder.getOrderFvNumber(), user.getId(), originOrder.getOrderId());
            }
        });
    }
}
