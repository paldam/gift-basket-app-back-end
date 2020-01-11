package com.damian.domain.basket;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface BasketTypeDao extends CrudRepository<BasketType,Long> {
    public List<BasketType> findAllBy();
}
