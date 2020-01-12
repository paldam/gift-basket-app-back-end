package com.damian.dto;

public class NumberProductsToChangeStock {

      private Integer productId;
      private Long quantityToChange;


    public NumberProductsToChangeStock(Integer productId, Long quantityToChange) {
        this.productId = productId;
        this.quantityToChange = quantityToChange;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Long getQuantityToChange() {
        return quantityToChange;
    }

    public void setQuantityToChange(Long quantityToChange) {
        this.quantityToChange = quantityToChange;
    }
}
