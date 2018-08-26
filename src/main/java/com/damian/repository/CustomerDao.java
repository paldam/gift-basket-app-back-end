package com.damian.repository;

import com.damian.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * Created by Damian on 04.10.2017.
 */
public interface CustomerDao extends JpaRepository<Customer,Long> {
    public List<Customer> findAllBy();
    public Customer findByCustomerId(Integer id) ;

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM customers WHERE customer_id = ?1", nativeQuery = true)
    public void deleteByCustomerId(Integer id);


}
