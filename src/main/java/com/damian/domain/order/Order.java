package com.damian.domain.order;

import com.damian.domain.customer.Address;
import com.damian.domain.customer.Customer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Audited
@Entity
@Table(name = "orders")
public class Order implements Serializable {
    private Long orderId;
    private String orderFvNumber;
    private String userName;
    // private User user;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Customer customer;
    private List<OrderItem> orderItems;
    private Date orderDate;
    private String additionalInformation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Warsaw")
    private Date deliveryDate;
    private Integer weekOfYear;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DeliveryType deliveryType;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private OrderStatus orderStatus;
    private Integer orderTotalAmount;
    private Integer cod;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Address address;
    private Integer additionalSale;
    private String contactPerson;


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
    @ManyToOne(fetch = FetchType.EAGER)
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "customer_id")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }



    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
    @Column(name = "delivery_date", columnDefinition = "DATE")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @ManyToOne
    @Audited(targetAuditMode = NOT_AUDITED)
    @JoinColumn(name = "delivery_type")
    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    @ManyToOne
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

    @ManyToOne(fetch = FetchType.EAGER)
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



    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderFvNumber='" + orderFvNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", customer=" + customer +
                ", orderItems=" + orderItems +
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
