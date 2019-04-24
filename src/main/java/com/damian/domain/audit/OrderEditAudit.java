package com.damian.domain.audit;
import com.damian.domain.order.Order;
import com.damian.domain.order.OrderHistory;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "order_edit_audits")
public class OrderEditAudit {
    private Long orderEditAuditId;
    private String userName;
    private Date changeDate;
    private Long orderId;
    private OrderHistory orderHistory;


    public OrderEditAudit() {
    }

    public OrderEditAudit(String userName, Long orderId, OrderHistory orderHistory) {
        this.userName = userName;
        this.orderId = orderId;
        this.orderHistory = orderHistory;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_edit_audit_id", nullable = false)
    public Long getOrderEditAuditId() {
        return orderEditAuditId;
    }

    public void setOrderEditAuditId(Long orderEditAuditId) {
        this.orderEditAuditId = orderEditAuditId;
    }


  

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "change_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_history_id")
    public OrderHistory getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(OrderHistory orderHistory) {
        this.orderHistory = orderHistory;
    }
}

