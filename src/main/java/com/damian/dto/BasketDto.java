package com.damian.dto;

import com.damian.domain.basket.Basket;
import com.damian.domain.basket.BasketType;

public class BasketDto {

    private Long basketId;
    private String basketName;
    private BasketType basketType;
    private Integer basketTotalPrice;
    private String season;
    private Integer stock;

    public BasketDto() {
    }

    public BasketDto(Basket basket) {
        this.basketId = basket.getBasketId();
        this.basketName = basket.getBasketName();
        this.basketType = basket.getBasketType();
        this.basketTotalPrice = basket.getBasketTotalPrice();
        this.season = basket.getSeason();
        this.stock = basket.getStock();
    }

    public Long getBasketId() {
        return basketId;
    }

    public void setBasketId(Long basketId) {
        this.basketId = basketId;
    }

    public String getBasketName() {
        return basketName;
    }

    public void setBasketName(String basketName) {
        this.basketName = basketName;
    }

    public BasketType getBasketType() {
        return basketType;
    }

    public void setBasketType(BasketType basketType) {
        this.basketType = basketType;
    }

    public Integer getBasketTotalPrice() {
        return basketTotalPrice;
    }

    public void setBasketTotalPrice(Integer basketTotalPrice) {
        this.basketTotalPrice = basketTotalPrice;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
