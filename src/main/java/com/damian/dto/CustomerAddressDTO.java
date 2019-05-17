package com.damian.dto;

import com.damian.domain.customer.Address;
import com.damian.domain.customer.Customer;

/**
 * Created by Damian on 19.08.2018.
 */
public class CustomerAddressDTO {


    private Integer customerId;
    private String organizationName;
    private String name;
    //private List<Address> addresses;
    private String email;
    private Long addressId;
    private String address;
    private String zipCode;
    private String cityName;
    private String phoneNumber;
    //private Customer customer;
    //private byte isPrimaryAddress;


    public CustomerAddressDTO(Customer cust, Address addr) {
        this.customerId = cust.getCustomerId() ;
        this.organizationName = cust.getCompany().getCompanyName(); //TODO
        this.name = cust.getName();
        this.email = cust.getEmail();
        this.phoneNumber = cust.getPhoneNumber();
        this.addressId = addr.getAddressId();
        this.address = addr.getAddress();
        this.zipCode = addr.getZipCode();
        this.cityName = addr.getCityName();

    }


    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
