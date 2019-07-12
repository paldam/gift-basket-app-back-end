package com.damian.domain.product;

import javax.persistence.*;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "supplier_id", nullable = false)
    private Integer supplierId;
    @Basic
    @Column(name = "name", nullable = false, length = 200)
    private String supplierName;
    @Basic
    @Column(name = "contact_person", nullable = true, length = 200)
    private String contactPerson;
    @Basic
    @Column(name = "phone", nullable = true, length = 200)
    private String phone;
    @Basic
    @Column(name = "email", nullable = true, length = 200)
    private String email;
    @Basic
    @Column(name = "address", nullable = true, length = 300)
    private String address;
    @Basic
    @Column(name = "web_site", nullable = true, length = 300)
    private String webSite;

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
}


