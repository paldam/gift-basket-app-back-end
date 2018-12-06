package com.damian.dto;

/**
 * Created by Damian on 31.08.2018.
 */
public class NumberOfBasketOrderedByDate {
    private String basketName ;
    private Long quantity;
    private Long numberOfOrdersWhereBasketOccur;


    public NumberOfBasketOrderedByDate(String basketName, Long quantity,Long numberOfOrdersWhereBasketOccur) {
        this.basketName = basketName;
        this.quantity = quantity;
        this.numberOfOrdersWhereBasketOccur =  numberOfOrdersWhereBasketOccur;
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

    public Long getNumberOfOrdersWhereBasketOccur() {
        return numberOfOrdersWhereBasketOccur;
    }

    public void setNumberOfOrdersWhereBasketOccur(Long numberOfOrdersWhereBasketOccur) {
        this.numberOfOrdersWhereBasketOccur = numberOfOrdersWhereBasketOccur;
    }
}
