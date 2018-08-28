package com.damian.model;

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
}


