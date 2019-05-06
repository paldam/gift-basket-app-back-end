package com.damian.domain.customer;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "addresses2")
public class Address2 {

    private Long addressId;
    private String address;
    private String zipCode;
    private String cityName;
    private String contackPerson;


    public Address2() {
    }

    public Address2(String address, String zipCode, String cityName, String contackPerson) {
        this.address = address;
        this.zipCode = zipCode;
        this.cityName = cityName;
        this.contackPerson = contackPerson;
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
    @Column(name = "contact_person", length = 300)
    public String getContackPerson() {
        return contackPerson;
    }

    public void setContackPerson(String contackPerson) {
        this.contackPerson = contackPerson;
    }
}
