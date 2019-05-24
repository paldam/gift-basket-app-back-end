package com.damian.dto;

import com.damian.domain.customer.Address;
import com.damian.domain.customer.Customer;
import com.damian.domain.order.DeliveryType;
import com.damian.domain.order.OrderItem;
import com.damian.domain.order.OrderStatus;
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
    private Integer weekOfYear;
    private DeliveryType deliveryType;
    private OrderStatus orderStatus;
    private Integer orderTotalAmount;
    private Long dbFileId;
    private List<OrderItem> orderItems;
    private Integer additionalSale;
    private Address address;

    public OrderDto(Long orderId, String orderFvNumber, Customer customer, Date orderDate,
                    String additionalInformation, Date deliveryDate,Integer weekOfYear, DeliveryType deliveryType,
                    OrderStatus orderStatus, Integer orderTotalAmount, Long dbFileId, List<OrderItem> orderItems,Integer additionalSale) {
        this.orderId = orderId;
        this.orderFvNumber = orderFvNumber;
        this.customer = customer;
        this.orderDate = orderDate;
        this.additionalInformation = additionalInformation;
        this.deliveryDate = deliveryDate;
        this.weekOfYear =weekOfYear;
        this.deliveryType = deliveryType;
        this.orderStatus = orderStatus;
        this.orderTotalAmount = orderTotalAmount;
        this.dbFileId = dbFileId;
        this.orderItems = orderItems;
        this.additionalSale = additionalSale;
    }

    public OrderDto(Long orderId, String orderFvNumber, Customer customer, Date orderDate,
                    String additionalInformation, Date deliveryDate,Integer weekOfYear, DeliveryType deliveryType,
                    OrderStatus orderStatus, Integer orderTotalAmount, Long dbFileId, List<OrderItem> orderItems,Integer additionalSale,Address address) {
        this.orderId = orderId;
        this.orderFvNumber = orderFvNumber;
        this.customer = customer;
        this.orderDate = orderDate;
        this.additionalInformation = additionalInformation;
        this.deliveryDate = deliveryDate;
        this.weekOfYear =weekOfYear;
        this.deliveryType = deliveryType;
        this.orderStatus = orderStatus;
        this.orderTotalAmount = orderTotalAmount;
        this.dbFileId = dbFileId;
        this.orderItems = orderItems;
        this.additionalSale = additionalSale;
        this.address = address;
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

    public Integer getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(Integer weekOfYear) {
        this.weekOfYear = weekOfYear;
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Integer getAdditionalSale() {
        return additionalSale;
    }

    public void setAdditionalSale(Integer additionalSale) {
        this.additionalSale = additionalSale;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
