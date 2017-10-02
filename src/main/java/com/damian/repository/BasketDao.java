package com.damian.repository;


import com.damian.model.Basket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BasketDao extends CrudRepository<Basket,Long> {
    public List<Basket> findAllBy();
    public List<Basket> findAllByOrderByBasketIdDesc();
}
