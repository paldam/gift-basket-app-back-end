package com.damian.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

/**
 * Created by Damian on 03.11.2017.
 */

public class ProductToOrder {
    private Integer id ;
    private String product_name;
    private Supplier supplier;
    private Integer stock;
    private Integer tmpOrdered;
    private Long suma ;
    private String capacity;


    public ProductToOrder(Integer id, String product_name,Supplier supplier,Integer stock,Integer tmpOrdered, Long suma,String capacity) {
        this.id = id;
        this.product_name = product_name;
        this.supplier = supplier;
        this.stock = stock;
        this.tmpOrdered = tmpOrdered;
        this.suma = suma;
        this.capacity = capacity;

    }

    public Long getSuma() {
        return suma;
    }

    public void setSuma(Long suma) {
        this.suma = suma;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTmpOrdered() {
        return tmpOrdered;
    }

    public void setTmpOrdered(Integer tmpOrdered) {
        this.tmpOrdered = tmpOrdered;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
}

