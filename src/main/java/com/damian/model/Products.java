package com.damian.model;

import javax.persistence.*;

/**
 * Created by Damian on 05.09.2017.
 */
@Entity
public class Products {
    private Integer id;
    private Integer capacity;
    private Integer price;
    private String productName;
    private Integer stock;
    private ProductsTypes productType;

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
    @Column(name = "capacity", nullable = false)
    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Basic
    @Column(name = "price", nullable = false)
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    @Column(name = "product_name", nullable = false, length = 50)
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


    @OneToOne
    @JoinColumn(name = "product_type")
    public ProductsTypes getProductType() {
        return productType;
    }

    public void setProductType(ProductsTypes productType) {
        this.productType = productType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Products products = (Products) o;

        if (getId() != null ? !getId().equals(products.getId()) : products.getId() != null) return false;
        if (getCapacity() != null ? !getCapacity().equals(products.getCapacity()) : products.getCapacity() != null)
            return false;
        if (getPrice() != null ? !getPrice().equals(products.getPrice()) : products.getPrice() != null) return false;
        if (getProductName() != null ? !getProductName().equals(products.getProductName()) : products.getProductName() != null)
            return false;
        if (stock != null ? !stock.equals(products.stock) : products.stock != null) return false;
        return getProductType() != null ? getProductType().equals(products.getProductType()) : products.getProductType() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getCapacity() != null ? getCapacity().hashCode() : 0);
        result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
        result = 31 * result + (getProductName() != null ? getProductName().hashCode() : 0);
        result = 31 * result + (stock != null ? stock.hashCode() : 0);
        result = 31 * result + (getProductType() != null ? getProductType().hashCode() : 0);
        return result;
    }
}
