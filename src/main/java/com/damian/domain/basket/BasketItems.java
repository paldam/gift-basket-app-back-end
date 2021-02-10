package com.damian.domain.basket;

import com.damian.domain.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "basket_items")
public class BasketItems {

    private Integer basketItemsId;
    private Product product;
    private Integer quantity;
    private Integer position;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_items_id", nullable = false)
    public Integer getBasketItemsId() {
        return basketItemsId;
    }

    public void setBasketItemsId(Integer basketItemsId) {
        this.basketItemsId = basketItemsId;
    }

    @ManyToOne(fetch=FetchType.LAZY)
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

    @Basic
    @Column(name = "position", nullable = false)
    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
