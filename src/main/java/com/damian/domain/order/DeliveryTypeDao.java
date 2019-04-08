package com.damian.domain.order;


import com.damian.domain.order.DeliveryType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeliveryTypeDao extends CrudRepository<DeliveryType,Long> {

    public List<DeliveryType> findAllBy();
}
