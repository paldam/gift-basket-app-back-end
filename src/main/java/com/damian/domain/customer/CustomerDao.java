package com.damian.domain.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomerDao extends JpaRepository<Customer,Long> {

    public List<Customer> findAllBy();
    public Optional<Customer> findByCustomerId(Integer id) ;

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM customers WHERE customer_id = ?1", nativeQuery = true)
    public void deleteByCustomerId(Integer id);
}
