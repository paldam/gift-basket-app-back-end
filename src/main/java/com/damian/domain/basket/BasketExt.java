package com.damian.domain.basket;

import java.util.List;

public class BasketExt {

    private Long basketId;
    private String basketName;
    private BasketType basketType;
    private List<BasketItems> basketItems;
    private Integer basketTotalPrice;
    private String season;
    private byte[] basketImg;
    private String basketImgContentType;
    private Integer isAlcoholic;
    private Integer isAvailable;
    private Integer basketProductsPrice;

    public BasketExt() {
    }

    public BasketExt(Basket basket) {
        this.basketId = basket.getBasketId();
        this.basketName = basket.getBasketName();
        this.basketType = basket.getBasketType();
        this.basketItems = basket.getBasketItems();
        this.basketTotalPrice = basket.getBasketTotalPrice();
        this.season = basket.getSeason();
        this.isAlcoholic = basket.getIsAlcoholic();
        this.isAvailable = basket.getIsAvailable();
        this.basketProductsPrice = basket.getBasketProductsPrice();
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

    public List<BasketItems> getBasketItems() {
        return basketItems;
    }

    public void setBasketItems(List<BasketItems> basketItems) {
        this.basketItems = basketItems;
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

    public byte[] getBasketImg() {
        return basketImg;
    }

    public void setBasketImg(byte[] basketImg) {
        this.basketImg = basketImg;
    }

    public String getBasketImgContentType() {
        return basketImgContentType;
    }

    public void setBasketImgContentType(String basketImgContentType) {
        this.basketImgContentType = basketImgContentType;
    }

    public Integer getIsAlcoholic() {
        return isAlcoholic;
    }

    public void setIsAlcoholic(Integer isAlcoholic) {
        this.isAlcoholic = isAlcoholic;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Integer getBasketProductsPrice() {
        return basketProductsPrice;
    }

    public void setBasketProductsPrice(Integer basketProductsPrice) {
        this.basketProductsPrice = basketProductsPrice;
    }


}


