package com.damian.dto;

public class NumberOfBasketOrderedByDate {

    private Long basketId;
    private String basketName;
    private Long quantity;
    private Long numberOfOrdersWhereBasketOccur;

    public NumberOfBasketOrderedByDate(Long basketId, String basketName, Long quantity,
                                       Long numberOfOrdersWhereBasketOccur) {
        this.basketId = basketId;
        this.basketName = basketName;
        this.quantity = quantity;
        this.numberOfOrdersWhereBasketOccur = numberOfOrdersWhereBasketOccur;
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
