
package com.damian.model;

import javax.persistence.*;

@Entity
@Table(name = "order_status")
public class OrderStatus {
    private Integer orderStatusId;
    private String orderStatusName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_status_id", nullable = false)
    public Integer getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    @Basic
    @Column(name = "order_status_name")
    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderStatus that = (OrderStatus) o;

        if (getOrderStatusId() != null ? !getOrderStatusId().equals(that.getOrderStatusId()) : that.getOrderStatusId() != null)
            return false;
        return getOrderStatusName() != null ? getOrderStatusName().equals(that.getOrderStatusName()) : that.getOrderStatusName() == null;
    }

    @Override
    public int hashCode() {
        int result = getOrderStatusId() != null ? getOrderStatusId().hashCode() : 0;
        result = 31 * result + (getOrderStatusName() != null ? getOrderStatusName().hashCode() : 0);
        return result;
    }
}