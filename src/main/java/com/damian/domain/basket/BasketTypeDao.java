package com.damian.domain.basket;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BasketTypeDao extends CrudRepository<BasketType,Long> {
    public List<BasketType> findAllBy();
    public BasketType findByBasketTypeId(int id);
}
