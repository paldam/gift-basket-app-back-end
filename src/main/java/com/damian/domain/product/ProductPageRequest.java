package com.damian.domain.product;

import java.util.List;

public class ProductPageRequest {
    private List<Product> productList;
    private long totalRowsOfRequest;

    public ProductPageRequest() {

    }

    public ProductPageRequest(List<Product> productList, long totalRowsOfRequest) {
        this.productList = productList;
        this.totalRowsOfRequest = totalRowsOfRequest;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public long getTotalRowsOfRequest() {
        return totalRowsOfRequest;
    }

    public void setTotalRowsOfRequest(long totalRowsOfRequest) {
        this.totalRowsOfRequest = totalRowsOfRequest;
    }
}
