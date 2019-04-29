package com.damian.domain.order;

import com.damian.dto.OrderDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderPageRequest {
    private List<OrderDto> orderDtoList;
    private long totalRowsOfRequest;


    public OrderPageRequest() {
    }

    public OrderPageRequest(List<OrderDto> orderDtoList, long totalRowsOfRequest) {
        this.orderDtoList = orderDtoList;
        this.totalRowsOfRequest = totalRowsOfRequest;
    }

    public List<OrderDto> getOrderDtoList() {
        return orderDtoList;
    }

    public void setOrderDtoList(List<OrderDto> orderDtoList) {
        this.orderDtoList = orderDtoList;
    }

    public long getTotalRowsOfRequest() {
        return totalRowsOfRequest;
    }

    public void setTotalRowsOfRequest(long totalRowsOfRequest) {
        this.totalRowsOfRequest = totalRowsOfRequest;
    }
}
