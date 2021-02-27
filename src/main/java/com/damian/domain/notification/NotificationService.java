package com.damian.domain.notification;

import com.damian.domain.order.Order;
import com.damian.domain.user.Authority;
import com.damian.domain.user.User;
import com.damian.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    private final NotificationDao notificationDao;
    private final UserRepository userRepository;

    public NotificationService(NotificationDao notificationDao, UserRepository userRepository) {
        this.notificationDao = notificationDao;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveNotifications(Order order, Order originOrder) {
        List<User> userList = userRepository.getLogisticWarehouseProductionUsers();
        userList.forEach(user -> {
            if (user.getAuthorities().contains(new Authority("produkcja"))) {
                if (Objects.equals(order.getProductionUser().getId(), user.getId())) {
                    notificationDao.saveNotifications("Dodano zamówienie nr: " + order.getOrderFvNumber()
                        + " " + "połączone z zamówieniem nr: " + originOrder.getOrderFvNumber(), user.getId(), originOrder.getOrderId());
                }
            } else {
                notificationDao.saveNotifications("Dodano zamówienie nr: " + order.getOrderFvNumber() +
                    " połączone z" + " zamówieniem nr: " + originOrder.getOrderFvNumber(), user.getId(), originOrder.getOrderId());
            }
        });
    }
}
