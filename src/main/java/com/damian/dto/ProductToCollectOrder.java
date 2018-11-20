package com.damian.dto;

import com.damian.model.Supplier;

public class ProductToCollectOrder {

    private String product_name;
    private Long suma ;
    private String capacity;

    public ProductToCollectOrder(String product_name, Long suma, String capacity) {
        this.product_name = product_name;
        this.suma = suma;
        this.capacity = capacity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Long getSuma() {
        return suma;
    }

    public void setSuma(Long suma) {
        this.suma = suma;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
}
