package com.damian.domain.notification;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notifications")

public class Notification {

    private Long id;
    private String notificationText;
    private Date notificationDate;


    public Notification() {
    }

    public Notification(String notificationText, Date notificationDate) {
        this.notificationText = notificationText;
        this.notificationDate = notificationDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getOrderId() {
        return id;
    }

    public void setOrderId(Long orderId) {
        this.id = orderId;
    }

    @Basic
    @Column(name = "notification_text", length = 1000, columnDefinition = "varchar(1000)")
    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }


    @Basic
    @Column(name = "notification_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }
}
