package com.damian.domain.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AddressDao extends JpaRepository<Address,Long> {

    List<Address> findAll();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM addresses WHERE address_id = ?1", nativeQuery = true)
    public void deleteByAddressId(Long id);

    @Modifying
    @Query(value = "DELETE FROM addresses WHERE customer_id = ?1", nativeQuery = true)
    public void deleteByCustomerId(Integer id);


    @Query(value = "SELECT * FROM addresses WHERE customer_id = ?1", nativeQuery = true)
    public List<Address> findAllAddrByCustomerId(Integer id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE addresses SET is_primary_address = 0 WHERE customer_id = ?1", nativeQuery = true)
    public void setAllCustomerAdrressPrimaryNo(Integer id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE addresses SET is_primary_address = 1 WHERE address_id = ?1", nativeQuery = true)
    public void setAddreesAsPrimary(Long id);

    @Query(value = "SELECT * FROM addresses_old WHERE customer_id = ?1 AND is_primary_address = 1 LIMIT 1 "
        , nativeQuery = true)
    public Optional<Address> findCustomerPrimaryAddrById(Integer id);

    @Query(value = "SELECT addresses.* FROM customers " +
        "join company on customers.company_id = company.company_id " +
        "JOIN orders on customers.customer_id = orders.customer_id " +
        "join addresses on orders.address_id = addresses.address_id " +
        "where customers.company_id =  ?1"
        ,nativeQuery = true)
    public Set<Address> findAddressByCompanyId(Long id);



    @Query(value = "SELECT addresses.* FROM orders join addresses on orders.address_id = addresses.address_id " +
        "join customers on orders.customer_id = customers.customer_id " +
        "WHERE customers.customer_id = ?1 ORDER BY orders.order_date DESC LIMIT 1"
        ,nativeQuery = true)
    public Optional<Address> findCustomerLastAddr(Integer id);







}
