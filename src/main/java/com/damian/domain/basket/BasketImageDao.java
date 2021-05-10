package com.damian.domain.basket;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface BasketImageDao extends CrudRepository<BasketImage,Long> {

}
