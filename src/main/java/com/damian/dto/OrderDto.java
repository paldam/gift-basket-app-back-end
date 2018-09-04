package com.damian.dto;

import com.damian.model.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;


public class OrderDto {

    private Long orderId;
    private String orderFvNumber;
    private Customer customer;
    private Date orderDate;
    private String additionalInformation;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Europe/Warsaw")
    private Date deliveryDate;
    private DeliveryType deliveryType;
    private OrderStatus orderStatus;
    private Integer orderTotalAmount;
    private Long dbFileId;

    public OrderDto(Long orderId, String orderFvNumber, Customer customer, Date orderDate,
                    String additionalInformation, Date deliveryDate, DeliveryType deliveryType,
                    OrderStatus orderStatus, Integer orderTotalAmount, Long dbFileId) {
        this.orderId = orderId;
        this.orderFvNumber = orderFvNumber;
        this.customer = customer;
        this.orderDate = orderDate;
        this.additionalInformation = additionalInformation;
        this.deliveryDate = deliveryDate;
        this.deliveryType = deliveryType;
        this.orderStatus = orderStatus;
        this.orderTotalAmount = orderTotalAmount;
        this.dbFileId = dbFileId;
    }



    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderFvNumber() {
        return orderFvNumber;
    }

    public void setOrderFvNumber(String orderFvNumber) {
        this.orderFvNumber = orderFvNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(Integer orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }


    public Long getDbFileId() {
        return dbFileId;
    }

    public void setDbFileId(Long dbFileId) {
        this.dbFileId = dbFileId;
    }


}