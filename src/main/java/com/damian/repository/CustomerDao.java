package com.damian.repository;

import com.damian.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Damian on 04.10.2017.
 */
public interface CustomerDao extends JpaRepository<Customer,Long> {
    public List<Customer> findAllBy();
}
