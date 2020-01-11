package com.damian.domain.basket;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface BasketSezonDao extends CrudRepository<BasketSezon,Long> {
    public List<BasketSezon> findAllBy();
    public Optional<BasketSezon> findByBasketSezonName(String name);

}
