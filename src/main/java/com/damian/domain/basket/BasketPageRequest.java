package com.damian.domain.basket;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BasketPageRequest {

    private List<Basket> basketsList;
    private long totalRowsOfRequest;

    public BasketPageRequest() {
    }

    public BasketPageRequest(List<Basket> basketsList, long totalRowsOfRequest) {
        this.basketsList = basketsList;
        this.totalRowsOfRequest = totalRowsOfRequest;
    }

    public List<Basket> getBasketsList() {
        return basketsList;
    }

    public void setBasketsList(List<Basket> basketsList) {
        this.basketsList = basketsList;
    }

    public long getTotalRowsOfRequest() {
        return totalRowsOfRequest;
    }

    public void setTotalRowsOfRequest(long totalRowsOfRequest) {
        this.totalRowsOfRequest = totalRowsOfRequest;
    }
}



