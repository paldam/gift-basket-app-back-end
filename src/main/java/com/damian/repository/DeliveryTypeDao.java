package com.damian.repository;


import com.damian.model.DeliveryType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeliveryTypeDao extends CrudRepository<DeliveryType,Long> {

    public List<DeliveryType> findAllBy();
}
