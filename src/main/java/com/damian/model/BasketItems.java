package com.damian.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by Damian on 25.09.2017.
 */
@Entity
@Table(name = "basket_items")
public class BasketItems {
    private Integer basketItemsId;
    private Integer basketId;
    private Product product;
    private Integer quantity;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_items_id", nullable = false)
    public Integer getBasketItemsId() {
        return basketItemsId;
    }

    public void setBasketItemsId(Integer basketItemsId) {
        this.basketItemsId = basketItemsId;
    }

    @Basic
    @Column(name = "basket_id", nullable = false)
    public Integer getBasketId() {
        return basketId;
    }

    public void setBasketId(Integer basketId) {
        this.basketId = basketId;
    }

    @ManyToOne
    @JoinColumn(name = "product_id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Basic
    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasketItems that = (BasketItems) o;

        if (getBasketItemsId() != null ? !getBasketItemsId().equals(that.getBasketItemsId()) : that.getBasketItemsId() != null)
            return false;
        if (getBasketId() != null ? !getBasketId().equals(that.getBasketId()) : that.getBasketId() != null)
            return false;
        if (getProduct() != null ? !getProduct().equals(that.getProduct()) : that.getProduct() != null) return false;
        return getQuantity() != null ? getQuantity().equals(that.getQuantity()) : that.getQuantity() == null;
    }

    @Override
    public int hashCode() {
        int result = getBasketItemsId() != null ? getBasketItemsId().hashCode() : 0;
        result = 31 * result + (getBasketId() != null ? getBasketId().hashCode() : 0);
        result = 31 * result + (getProduct() != null ? getProduct().hashCode() : 0);
        result = 31 * result + (getQuantity() != null ? getQuantity().hashCode() : 0);
        return result;
    }
}
