package com.damian.domain.prize;

import javax.persistence.*;

@Entity
@Table(name = "prize_order_status")
public class PrizeOrderStatus {
    private Integer orderStatusId;
    private String orderStatusName;

    public PrizeOrderStatus() {
    }

    public PrizeOrderStatus(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

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
}



