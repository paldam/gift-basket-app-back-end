package com.damian.domain.notification;

import com.damian.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationDao extends JpaRepository<Notification, Long> {

//    @Query(value = "SELECT count(*) FROM notifications ", nativeQuery = true)
//    public long getCountOfAllNotification();

    @Query(value = "SELECT count(*) FROM notifications where  user_id = ?1 and wasRead = 0", nativeQuery = true)
    public Long getCountOfAllNotification(Integer userId);

    @Query(value = "SELECT id,notification_date,notification_text,user_id,order_id,wasRead FROM notifications where  user_id = ?1 ORDER  by notification_date DESC", nativeQuery = true)
    public List<Notification> getNotificationByUser(Integer userId);


    @Transactional
    @Modifying
    @Query(value = "INSERT into notifications (notification_text,user_id,wasRead,order_id)  values (?1,?2,0,?3) ", nativeQuery = true)
    void saveNotifications( String notification_text, Long user_id,Long order_id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE notifications set wasRead = true where id = ?1", nativeQuery = true)
    void markAsReaded( Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE notifications set wasRead = true where user_id = ?1", nativeQuery = true)
    void markAllAsReaded( Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE from notifications where user_id = ?1", nativeQuery = true)
    void deleteByUser( Long id);

}


