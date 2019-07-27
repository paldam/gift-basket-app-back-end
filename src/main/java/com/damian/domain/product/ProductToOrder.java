package com.damian.domain.product;

import com.damian.domain.basket.BasketItems;
import com.damian.domain.order.OrderItem;

import java.util.List;
import java.util.Set;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

/**
 * Created by Damian on 03.11.2017.
 */

public class ProductToOrder {
    private Integer id ;
    private String product_name;
    private Set<Supplier> suppliers;
    private Integer stock;
    private Integer tmpOrdered;
    private Long suma ;
    private String capacity;

    public ProductToOrder() {
    }


    public ProductToOrder(Product p, Long suma) {

        this.id = p.getId();
        this.product_name = p.getProductName();
        this.suppliers = p.getSuppliers();
        this.stock = p.getStock();
        this.tmpOrdered = p.getTmpOrdered();
        this.suma = suma;
        this.capacity = p.getCapacity();
    }

    public ProductToOrder(Integer id, String product_name, Set<Supplier> suppliers, Integer stock, Integer tmpOrdered, Long suma, String capacity) {
        this.id = id;
        this.product_name = product_name;
        this.suppliers = suppliers;
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

    public Set<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSupplier(Set<Supplier> sup) {
        this.suppliers = sup;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
}

