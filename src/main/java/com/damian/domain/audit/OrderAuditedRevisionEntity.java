package com.damian.domain.audit;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import javax.persistence.*;
import java.util.Objects;

@RevisionEntity(AuditingRevisionListener.class)
@Entity
@Table(name = "audit_info")
public class OrderAuditedRevisionEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @RevisionNumber
    private Long revId;
    @RevisionTimestamp
    private Long changeTime;
    private String user;
    private Long orderId;

    public OrderAuditedRevisionEntity() {
    }

    public OrderAuditedRevisionEntity(Long revId, Long changeTime, Long orderId, String user) {
        this.revId = revId;
        this.changeTime = changeTime;
        this.user = user;
        this.orderId = orderId;
    }

    public Long getRevId() {
        return revId;
    }

    public void setRevId(Long revId) {
        this.revId = revId;
    }



    public Long getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Long changeTime) {
        this.changeTime = changeTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderAuditedRevisionEntity that = (OrderAuditedRevisionEntity) o;
        return Objects.equals(revId, that.revId)
            && Objects.equals(changeTime, that.changeTime)
            && Objects.equals(user, that.user)
            && Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(revId, changeTime, user, orderId);
    }
}
