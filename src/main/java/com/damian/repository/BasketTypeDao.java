package com.damian.repository;

import com.damian.model.BasketType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Damian on 25.09.2017.
 */
public interface BasketTypeDao extends CrudRepository<BasketType,Long> {
    public List<BasketType> findAllBy();
}
