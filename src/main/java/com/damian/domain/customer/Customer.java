package com.damian.domain.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {
    private Integer customerId;
    private String name;
    private String email;
    private String phoneNumber;
    private String additionalInformation;
    private Company company;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //only deserialization
    private List<Address> addresses;

    public Customer() {
    }

    public Customer(Integer customerId,String name, String email, String phoneNumber, String additionalInformation, Company company, List<Address> addresses) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.additionalInformation = additionalInformation;
        this.company = company;
        this.addresses = addresses;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id",nullable=false)

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }



    @Basic
    @Column(name = "name", length = 300)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Basic
    @Column(name = "email", length = 300)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "phone_number", length = 100, columnDefinition = "varchar(100)")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "additional_information", length = 200, columnDefinition = "varchar(200)")
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    public Company getCompany() {
        return company;
    }




    public void setCompany(Company company) {
        this.company = company;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",nullable=true)
    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }


    @Override
    public String toString() {
        return "Customer{" +
            "customerId=" + customerId +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", additionalInformation='" + additionalInformation + '\'' +
            ", company=" + company +
            ", addresses=" + addresses +
            '}';
    }
}
