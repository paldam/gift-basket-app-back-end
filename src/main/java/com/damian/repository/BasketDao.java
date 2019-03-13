package com.damian.repository;


import com.damian.model.Basket;
import com.damian.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BasketDao extends CrudRepository<Basket,Long> {
    public List<Basket> findAllBy();
    public List<Basket> findAllByOrderByBasketIdDesc();


    @Query(value = "SELECT * FROM baskets WHERE basket_type != 99 AND basket_type != 999", nativeQuery = true)
    public List<Basket> findAllWithoutDeleted();

    @Query(value = "SELECT * FROM baskets WHERE  basket_type != 999", nativeQuery = true)
    public List<Basket> findAllWithDeleted();

    @Query(value = "SELECT * FROM baskets WHERE basket_type = 100 ", nativeQuery = true)
    public List<Basket> findAllBasketForExternalPartner();

    @Query(value = "SELECT * FROM baskets WHERE basket_type = 99", nativeQuery = true)
    public List<Basket> findAllDeleted();

    @Query(value = "SELECT * FROM baskets WHERE basket_type = 100", nativeQuery = true)
    public List<Basket> findALLExportBasket();

    @Query(value = "select * from baskets INNER join basket_items on baskets.basket_id = basket_items.basket_id where basket_items.product_id = ?1 and (baskets.basket_type = 1 Or baskets.basket_type =2) ", nativeQuery = true)
    public List<Basket> BasketListByProduct(Integer productId);
}
