package com.damian.domain.order;

import com.damian.domain.customer.Address;
import com.damian.domain.customer.Customer;
import com.damian.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;
@Audited
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    private Long orderId;
    private String orderFvNumber;
    private String userName;
    private Customer customer;
    private List<OrderItem> orderItems;
    private Date orderDate;
    private String additionalInformation;
    private String textToCard;
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
    private User productionUser;
    private User loyaltyUser;
    private Boolean isAllreadyComputedPoints;
    private Boolean isPaid;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "customer_id")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
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
    @Column(name = "text_to_card", length = 1000)
    public String getTextToCard() {
        return textToCard;
    }

    public void setTextToCard(String textToCard) {
        this.textToCard = textToCard;
    }

    @Basic
    @Column(name = "delivery_date", columnDefinition = "DATE")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "delivery_type")
    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "production_user")
    public User getProductionUser() {
        return productionUser;
    }

    public void setProductionUser(User productionUser) {
        this.productionUser = productionUser;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "loyalty_user")
    public User getLoyaltyUser() {
        return loyaltyUser;
    }

    public void setLoyaltyUser(User loyaltyUser) {
        this.loyaltyUser = loyaltyUser;
    }

    @Basic
    @Column(name = "allready_computed_points", columnDefinition = "boolean default false")
    public Boolean getAllreadyComputedPoints() {
        return isAllreadyComputedPoints;
    }

    public void setAllreadyComputedPoints(Boolean allreadyComputedPoints) {
        isAllreadyComputedPoints = allreadyComputedPoints;
    }

    @Basic
    @Column(name = "paid", columnDefinition = "boolean default false")
    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public Order() {
    }

    public Order(Order order){
        this.orderFvNumber = order.orderFvNumber;
        this.userName = order.userName;
        this.customer = order.customer;
        this.orderItems = order.orderItems;
        this.orderDate = order.orderDate;
        this.additionalInformation = order.additionalInformation;
        this.textToCard = order.textToCard;
        this.deliveryDate = order.deliveryDate;
        this.weekOfYear = order.weekOfYear;
        this.deliveryType = order.deliveryType;
        this.orderStatus = order.orderStatus;
        this.orderTotalAmount = order.orderTotalAmount;
        this.cod = order.cod;
        this.address = order.address;
        this.additionalSale = order.additionalSale;
        this.contactPerson = order.contactPerson;
        this.productionUser = order.productionUser;
        this.loyaltyUser = order.loyaltyUser;
        this.isAllreadyComputedPoints = order.isAllreadyComputedPoints;
    }
}
