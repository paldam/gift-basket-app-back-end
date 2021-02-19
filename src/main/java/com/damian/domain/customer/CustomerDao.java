package com.damian.domain.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface CustomerDao extends JpaRepository<Customer, Long> {

    public List<Customer> findAllBy();


    @QueryHints({ @QueryHint(name = "hibernate.query.passDistinctThrough", value = "false") })
    @Query(value = "SELECT distinct c FROM Customer c left join fetch c.company co where c.customerId =?1")
    public Optional<Customer> findByCustomerId(Integer id);


    @Query(value = "SELECT distinct c FROM Customer c left join fetch c.company co ")
    public List<Customer> findAllFetchCompany();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM customers WHERE customer_id = ?1", nativeQuery = true)
    public void deleteByCustomerId(Integer id);
}
