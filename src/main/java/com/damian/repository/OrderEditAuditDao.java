package com.damian.repository;

import com.damian.model.OrderEditAudit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface OrderEditAuditDao extends CrudRepository<OrderEditAudit,Long> {

    public List<OrderEditAudit> findByOrderId(Long id);
}
