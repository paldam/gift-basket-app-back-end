package com.damian.model;
import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "order_edit_audits")
public class OrderEditAudit {
    private Long orderEditAuditId;
    private String userName;
    private Date changeDate;
    private Long orderId;


    public OrderEditAudit() {
    }

    public OrderEditAudit(String userName, Long orderId) {
        this.userName = userName;
        this.orderId = orderId;
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
}

