package com.damian.domain.customer.OLD;

import com.damian.domain.customer.Address;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "customers_old")

public class CustomerOLD {
    private Integer customerId;
    private String organizationName;
    private String name;
    private List<AddressOLD> addresses;
    private String email;
    private String phoneNumber;
    private String additionalInformation;

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
    @Column(name = "organizationName", length = 300)
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Basic
    @Column(name = "name", length = 300)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,fetch =FetchType.EAGER)
    public List<AddressOLD> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressOLD> addresses) {
        this.addresses = addresses;
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
}
