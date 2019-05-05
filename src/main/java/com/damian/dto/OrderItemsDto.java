package com.damian.dto;

import com.damian.domain.basket.Basket;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class OrderItemsDto {

    private Integer orderItemId;
    private Basket basket;
    private Integer quantity;
    private Boolean added;

    public OrderItemsDto() {
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getAdded() {
        return added;
    }

    public void setAdded(Boolean added) {
        this.added = added;
    }
}
