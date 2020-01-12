package com.damian.domain.prize;

import com.damian.domain.user.User;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "prize_orders")
public class PrizeOrder {

    private Long id;
    private Date orderDate;
    private List<PrizeOrderItems> prizeOrderItems;
    private String additionalInformation;
    private Integer orderTotalAmount;
    private String nameLastname;
    private String address;
    private String zipCode;
    private String city;
    private String phone;
    private String email;
    private User user;
    private PrizeOrderStatus prizeOrderStatus;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prize_order_id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "order_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "prize_order_id", referencedColumnName = "prize_order_id")
    public List<PrizeOrderItems> getPrizeOrderItems() {
        return prizeOrderItems;
    }

    public void setPrizeOrderItems(List<PrizeOrderItems> prizeOrderItems) {
        this.prizeOrderItems = prizeOrderItems;
    }

    @Basic
    @Column(name = "additional_information", length = 3000)
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
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
    @Column(name = "name", length = 1000)
    public String getNameLastname() {
        return nameLastname;
    }

    public void setNameLastname(String nameLastname) {
        this.nameLastname = nameLastname;
    }

    @Basic
    @Column(name = "address", length = 1000)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "zip_code", length = 1000)
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Basic
    @Column(name = "city", length = 1000)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "phone", length = 25)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "email", length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ManyToOne
    @JoinColumn(name = "user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "prize_order_status_id")
    public PrizeOrderStatus getPrizeOrderStatus() {
        return prizeOrderStatus;
    }

    public void setPrizeOrderStatus(PrizeOrderStatus prizeOrderStatus) {
        this.prizeOrderStatus = prizeOrderStatus;
    }
}

