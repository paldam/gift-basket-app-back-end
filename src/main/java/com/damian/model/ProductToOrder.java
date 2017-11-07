package com.damian.model;

import org.springframework.stereotype.Component;

/**
 * Created by Damian on 03.11.2017.
 */

public class ProductToOrder {
    private Integer id ;
    private String product_name;
    private String deliver;
    private Integer stock;
    private Long suma ;


    public ProductToOrder(Integer id, String product_name,String deliver,Integer stock,Long suma) {
        this.id = id;
        this.product_name = product_name;
        this.deliver = deliver;
        this.stock = stock;
        this.suma = suma;

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

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }
}
