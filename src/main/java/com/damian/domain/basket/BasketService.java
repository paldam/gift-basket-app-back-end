package com.damian.domain.basket;

import com.damian.domain.order.OrderItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BasketService {

    private BasketDao basketDao;

    BasketService(BasketDao basketDao) {
        this.basketDao = basketDao;
    }

    @Transactional
    public void addNumberOfProductsDelivery(List<OrderItem> orderItems) {
        orderItems.forEach(orderItem -> {
            basketDao.addBasketToStock(orderItem.getBasket().getBasketId(), orderItem.getQuantity());
        });
    }
}
