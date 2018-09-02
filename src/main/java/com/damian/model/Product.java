package com.damian.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Damian on 05.09.2017.
 */
@Entity
@Table(name = "products")
public class Product {
    private Integer id;
    private String capacity;
    private Integer price;
    private String productName;
    private Integer stock;
    private String deliver;
    private Integer isArchival;
    private Supplier supplier;
    private Date lastStockEditDate;


    @Basic
    @Column(name = "is_archival")
    public Integer getIsArchival() {
        return isArchival;
    }

    public void setIsArchival(Integer isArchival) {
        this.isArchival = isArchival;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "capacity")
    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    @Basic
    @Column(name = "price")
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    @Column(name = "product_name", nullable = false, length = 300)
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Basic
    @Column(name = "stock")
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Basic
    @Column(name = "deliver")
    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }


    @Basic
    @Column(name = "last_stock_edit_date")
    public Date getLastStockEditDate() {
        return lastStockEditDate;
    }

    public void setLastStockEditDate(Date lastStockEditDate) {
        this.lastStockEditDate = lastStockEditDate;
    }
}