package com.damian.repository;


import com.damian.model.Basket;
import com.damian.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BasketDao extends CrudRepository<Basket,Long> {
    public List<Basket> findAllBy();
    public List<Basket> findAllByOrderByBasketIdDesc();

    @Query(value = "SELECT * FROM baskets WHERE basket_type != 99", nativeQuery = true)
    public List<Basket> findAllWithoutDeleted();
}
