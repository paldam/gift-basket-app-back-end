package com.damian.domain.order;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface DeliveryTypeDao extends CrudRepository<DeliveryType,Long> {
    List<DeliveryType> findAllBy();
}
