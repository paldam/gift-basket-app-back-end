package com.damian.domain.customer;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers_2")
public class Customer_2 {
    private Integer customerId;
    private String name;
    private String email;
    private String phoneNumber;
    private String additionalInformation;
    private Company company;
    private List<Address2> address;

    public Customer_2() {
    }

    public Customer_2( String name, String email, String phoneNumber, String additionalInformation, Company company, List<Address2> address) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.additionalInformation = additionalInformation;
        this.company = company;
        this.address = address;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id",nullable = false)
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


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    public List<Address2> getAddress() {
        return address;
    }

    public void setAddress(List<Address2> address) {
        this.address = address;
    }
}
