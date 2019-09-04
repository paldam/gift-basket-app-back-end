package com.damian.domain.basket;

import com.damian.domain.basket.BasketType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Damian on 25.09.2017.
 */
public interface BasketSezonDao extends CrudRepository<BasketSezon,Long> {
    public List<BasketSezon> findAllBy();
    public Optional<BasketSezon> findByBasketSezonName(String name);

}
