package com.damian.domain.basket;

import com.damian.domain.order.OrderItem;
import com.damian.domain.order.OrderPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

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

    public BasketPageRequest getBasketsPege(int page, int size, String text, String orderBy, int sortingDirection, boolean onlyArchival, List<Integer> basketSeasonFilter) {
        Sort.Direction sortDirection = sortingDirection == -1 ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Basket> basketPage;
        //        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, orderBy));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, orderBy));
        if (onlyArchival == false) {
            if (basketSeasonFilter.size() == 0) {
                basketPage = basketDao.findAll(BasketSpecyficationJpa.getBasketsWithSearchFilter(text).and(BasketSpecyficationJpa.getWithoutDeleted()), pageable);
            } else {
                basketPage = basketDao.findAll(BasketSpecyficationJpa.getBasketsWithSearchFilter(text).and(BasketSpecyficationJpa.getOrderWithSeasons(basketSeasonFilter).and(BasketSpecyficationJpa.getWithoutDeleted())), pageable);
            }
        } else {
            if (basketSeasonFilter.isEmpty()) {
                basketPage = basketDao.findAll(BasketSpecyficationJpa.getBasketsWithSearchFilter(text).and(BasketSpecyficationJpa.getOnlyDeleted()), pageable);
            } else {
                basketPage = basketDao.findAll(BasketSpecyficationJpa.getBasketsWithSearchFilter(text).and(BasketSpecyficationJpa.getOnlyDeleted()).and(BasketSpecyficationJpa.getOrderWithSeasons(basketSeasonFilter)), pageable);
            }
        }
        return new BasketPageRequest(basketPage.getContent(), basketPage.getTotalElements());
    }
}
