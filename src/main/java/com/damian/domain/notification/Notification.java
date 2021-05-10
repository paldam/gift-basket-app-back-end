package com.damian.domain.notification;

import com.damian.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notifications")
public class Notification {

    private Long id;
    private String notificationText;
    private Date notificationDate;
    private User user;
    private boolean wasRead;
    private Long notiOrderContext;

    public Notification() {
    }

    public Notification(String notificationText, Date notificationDate, User user, Long notiOrderContext) {
        this.notificationText = notificationText;
        this.notificationDate = notificationDate;
        this.user = user;
        this.notiOrderContext = notiOrderContext;
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

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isWasRead() {
        return wasRead;
    }

    @Basic
    @Column(name = "wasRead")
    public void setWasRead(boolean wasRead) {
        this.wasRead = wasRead;
    }

    @Basic
    @Column(name = "order_id")
    public Long getNotiOrderContext() {
        return notiOrderContext;
    }

    public void setNotiOrderContext(Long notiOrderContext) {
        this.notiOrderContext = notiOrderContext;
    }

    @Override
    public String toString() {
        return "Notification{" + "id=" + id + ", notificationText='" + notificationText + '\'' + ", notificationDate" +
            "=" + notificationDate + ", user=" + user + ", wasRead=" + wasRead + ", notiOrderContext=" + notiOrderContext + '}';
    }
}
