package com.damian.dto;

/**
 * Created by Damian on 31.08.2018.
 */
public class NumberOfBasketOrderedByDate {
    private String basketName ;
    private Long quantity;


    public NumberOfBasketOrderedByDate(String basketName, Long quantity) {
        this.basketName = basketName;
        this.quantity = quantity;
    }

    public String getBasketName() {
        return basketName;
    }

    public void setBasketName(String basketName) {
        this.basketName = basketName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
