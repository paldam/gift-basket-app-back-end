package com.damian.model;

import javax.persistence.*;


@Entity
@Table(name = "addresses")
public class Address {
    private Long addressId;
    private Integer customerId;
    private String address;
    private String zipCode;
    private String cityName;
    private String phoneNumber;


    public Address() {
    }

    public Address(Integer  customerId, String address, String zipCode, String cityName, String phoneNumber) {
        this.customerId = customerId;
        this.address = address;
        this.zipCode = zipCode;
        this.cityName = cityName;
        this.phoneNumber = phoneNumber;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id",nullable = false)
    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
    

    @Column(name = "customer_id",nullable = false)
    public Integer  getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer  customerId) {
        this.customerId = customerId;
    }


    @Basic
    @Column(name = "address", length = 350)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @Basic
    @Column(name = "zip_code", length = 300)
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    @Basic
    @Column(name = "city_name", length = 300)
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Basic
    @Column(name = "phone_number", length = 300)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    
}
