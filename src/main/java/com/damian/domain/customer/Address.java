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
    private String phoneNumber;
    private String additionalInformation;

    public Address() {
    }

    public Address(Long addressId, String address, String zipCode, String cityName, String contactPerson,
                   String phoneNumber, String additionalInformation) {
        this.addressId = addressId;
        this.address = address;
        this.zipCode = zipCode;
        this.cityName = cityName;
        this.contactPerson = contactPerson;
        this.phoneNumber = phoneNumber;
        this.additionalInformation = additionalInformation;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
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

    @Basic
    @Column(name = "phone_number", length = 300)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "additional_information", length = 300)
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String addressDesc() {
        return address + " " + zipCode + " " + cityName;
    }


}
