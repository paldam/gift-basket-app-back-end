package com.damian.dto;

import com.damian.domain.basket.Basket;

public class BasketExtStockDao {

    private Long basketId;
    private Integer isAvailable;

    public BasketExtStockDao() {
    }

    public BasketExtStockDao(Basket basket) {
        this.basketId = basket.getBasketId();
        this.isAvailable = basket.getIsAvailable();
    }

    public Long getBasketId() {
        return basketId;
    }

    public void setBasketId(Long basketId) {
        this.basketId = basketId;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }
}


