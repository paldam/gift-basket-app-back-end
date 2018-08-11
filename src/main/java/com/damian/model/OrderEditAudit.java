package com.damian.model;
import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "order_edit_audits")
public class OrderEditAudit {
    private Long orderEditAuditId;
    private Order order;
    private User user;
    private Date changeDate;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_edit_audit_id", nullable = false)
    public Long getOrderEditAuditId() {
        return orderEditAuditId;
    }

    public void setOrderEditAuditId(Long orderEditAuditId) {
        this.orderEditAuditId = orderEditAuditId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Basic
    @Column(name = "change_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }
}

