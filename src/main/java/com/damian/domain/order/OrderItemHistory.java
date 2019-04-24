package com.damian.domain.order;
import com.damian.domain.basket.Basket;

import javax.persistence.*;

@Entity
@Table(name = "order_items_history")
public class OrderItemHistory  {
    private Integer orderItemId;
    private Basket basket;
    private Integer quantity;
    //private Order order;


    public OrderItemHistory() {
    }

    public OrderItemHistory( Basket basket, Integer quantity) {
        this.basket = basket;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id", nullable = false)
    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }


    @ManyToOne
    @JoinColumn(name = "basket_id")
    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
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
