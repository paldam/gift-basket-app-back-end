package com.damian.domain.order;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface DeliveryTypeDao extends CrudRepository<DeliveryType,Long> {
    List<DeliveryType> findAllBy();
}
