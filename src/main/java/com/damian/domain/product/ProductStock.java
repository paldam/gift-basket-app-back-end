package com.damian.domain.product;

public class ProductStock {
    private Integer productId;
    private Integer stock;

    public ProductStock(Integer productId, Integer stock) {
        this.productId = productId;
        this.stock = stock;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
