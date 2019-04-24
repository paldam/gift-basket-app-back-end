package com.damian.domain.order;

import com.damian.domain.customer.Address;
import com.damian.domain.customer.Customer;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "orders_history2")
public class OrderHistory implements Serializable {
    private Long orderId;
    private String orderFvNumber;
    private String userName;
    // private User user;
    private Customer customer;
    private List<OrderItemHistory> orderItemHistories;
    private Date orderDate;
    private String additionalInformation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Warsaw")
    private Date deliveryDate;
    private Integer weekOfYear;
    private DeliveryType deliveryType;
    private OrderStatus orderStatus;
    private Integer orderTotalAmount;
    private Integer cod;
    private Address address;
    private Integer additionalSale;
    private String contactPerson;


    public OrderHistory() {
    }

    public OrderHistory(Order order) {
        this.orderFvNumber = order.getOrderFvNumber();
        this.userName = order.getUserName();
        this.customer = order.getCustomer();
        this.orderDate = order.getOrderDate();
        this.additionalInformation = order.getAdditionalInformation();
        this.deliveryDate = order.getDeliveryDate();
        this.weekOfYear = order.getWeekOfYear();
        this.deliveryType = order.getDeliveryType();
        this.orderStatus = order.getOrderStatus();
        this.orderTotalAmount = order.getOrderTotalAmount();
        this.cod = order.getCod();
        this.address = order.getAddress();
        this.additionalSale = order.getAdditionalSale();
        this.contactPerson = order.getContactPerson();

         List<OrderItemHistory> orderItemHistoryTmp = new ArrayList<>();

         order.getOrderItems().forEach(orderItem -> {
             orderItemHistoryTmp.add(new OrderItemHistory(orderItem.getBasket(),orderItem.getQuantity()));
         });

         this.orderItemHistories = orderItemHistoryTmp;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    public Long getOrderHistoryId() {
        return orderId;
    }

    public void setOrderHistoryId(Long orderHistoryId) {
        this.orderId = orderHistoryId;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    public List<OrderItemHistory> getOrderItemsHistory() {
        return orderItemHistories;
    }

    public void setOrderItemsHistory(List<OrderItemHistory> orderItemsHistory) {
        this.orderItemHistories = orderItemsHistory;
    }

    @Basic
    @Column(name = "week_of_year", length = 300)
    public Integer getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(Integer weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    @Basic
    @Column(name = "order_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
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
    @Column(name = "delivery_date", columnDefinition = "DATE")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Basic
    @Column(name = "additional_sale", length = 10, columnDefinition = "INT DEFAULT 0")
    public Integer getAdditionalSale() {
        return additionalSale;
    }

    public void setAdditionalSale(Integer additionalSale) {
        this.additionalSale = additionalSale;
    }


    @Basic
    @Column(name = "contact_person", length = 500)
    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    @Override
    public String toString() {
        return "Order{" +
            "orderHistoryId=" + orderId +
            ", orderFvNumber='" + orderFvNumber + '\'' +
            ", userName='" + userName + '\'' +
            ", customer=" + customer +
            ", orderItems="  +
            ", orderDate=" + orderDate +
            ", additionalInformation='" + additionalInformation + '\'' +
            ", deliveryDate=" + deliveryDate +
            ", weekOfYear=" + weekOfYear +
            ", deliveryType=" + deliveryType +
            ", orderStatus=" + orderStatus +
            ", orderTotalAmount=" + orderTotalAmount +
            ", cod=" + cod +
            ", address=" + address +
            ", additionalSale=" + additionalSale +
            ", contactPerson='" + contactPerson + '\'' +
            '}';
    }
}
