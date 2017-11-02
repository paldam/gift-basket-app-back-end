package com.damian.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "orders")
public class Order {
    private Long orderId;
    private String orderFvNumber;
    private String userName;
    // private User user;
    private Customer customer;
    private List<OrderItem> orderItems;
    private Date orderDate;
    private String additionalInformation;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Europe/Warsaw")
    private Date deliveryDate;
    private DeliveryType deliveryType;
    private OrderStatus orderStatus;
    private Integer orderTotalAmount;
    private Integer cod;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "fv_number", length = 300)
    public String getOrderFvNumber() {
        return orderFvNumber;
    }

    public void setOrderFvNumber(String orderFvNumber) {
        this.orderFvNumber = orderFvNumber;
    }

    @Basic
    @Column(name = "added_by_user", length = 300)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @ManyToOne(cascade = CascadeType.ALL,fetch =FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @OneToMany(cascade = CascadeType.ALL,fetch =FetchType.EAGER)
    @JoinColumn(name = "order_id",referencedColumnName ="order_id" )
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }


    @Basic
    @Column(name = "order_date",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Basic
    @Column(name = "additional_information", length = 1000)
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @Basic
    @Column(name = "delivery_date",columnDefinition="DATE")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @ManyToOne
    @JoinColumn(name = "delivery_type")
    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    @ManyToOne
    @JoinColumn(name = "order_status_id")
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Basic
    @Column(name = "order_total_amount")
    public Integer getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(Integer orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    @Basic
    @Column(name = "cod")
    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }
}
