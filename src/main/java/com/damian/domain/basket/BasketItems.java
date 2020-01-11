package com.damian.domain.basket;

import com.damian.domain.product.Product;
import javax.persistence.*;

@Entity
@Table(name = "basket_items")
public class BasketItems {

    private Integer basketItemsId;
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
}
