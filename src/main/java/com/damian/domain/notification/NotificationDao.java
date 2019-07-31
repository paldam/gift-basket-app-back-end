package com.damian.domain.notification;

import com.damian.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationDao extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT count(*) FROM notifications ", nativeQuery = true)
    public long getCountOfAllNotification();
}
