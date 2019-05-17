package com.damian.domain.customer;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

    private Long addressId;
    private String address;
    private String zipCode;
    private String cityName;
    private String contactPerson;


    public Address() {
    }

    public Address(Long addressId, String address, String zipCode, String cityName, String contactPerson) {
        this.addressId = addressId;
        this.address = address;
        this.zipCode = zipCode;
        this.cityName = cityName;
        this.contactPerson = contactPerson;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id",insertable=true, updatable=true, unique=true, nullable=false)
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
    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String addressDesc(){
        return
            address + " " + zipCode +   " " + cityName ;
    }
}
