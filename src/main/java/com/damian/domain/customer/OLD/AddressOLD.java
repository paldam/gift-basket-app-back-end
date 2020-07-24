package com.damian.domain.customer.OLD;

import com.damian.domain.customer.Customer;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;


@Entity
@Table(name = "addresses_old")
public class AddressOLD {
    private Long addressId;
    private String address;
    private String zipCode;
    private String cityName;
    private CustomerOLD customer;
    private byte isPrimaryAddress;


    public AddressOLD() {
    }

    public AddressOLD(Integer  customerId, String address, String zipCode, String cityName) {
        this.address = address;
        this.zipCode = zipCode;
        this.cityName = cityName;
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


    @JsonBackReference
    @ManyToOne (cascade = CascadeType.MERGE,fetch =FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    public CustomerOLD getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerOLD customer) {
        this.customer = customer;
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
    @Column(name = "is_primary_address",nullable = false)
    public byte getIsPrimaryAddress() {
        return isPrimaryAddress;
    }

    public void setIsPrimaryAddress(byte isPrimaryAddress) {
        this.isPrimaryAddress = isPrimaryAddress;
    }

    public String AddressDesc(){
        return
            address + " " + zipCode +   " " + cityName ;
    }
}
