package com.damian.domain.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AddressDao extends JpaRepository<Address,Long> {

    @Override
    List<Address> findAll();

    @Query(value = "SELECT * FROM addresses WHERE customer_id = ?1 AND is_primary_address = 1 LIMIT 1 ", nativeQuery = true)
    public Address findCustomerPrimaryAddrById(Integer id);

    @Query(value = "SELECT addresses.*  FROM customers join company on customers.company_id = company.company_id JOIN addresses on customers.customer_id = addresses.customer_id where customers.company_id = ?1", nativeQuery = true)
    public List<Address> findAddressByCompanyId(Long id);





    @Transactional
    @Modifying
    @Query(value = "DELETE FROM addresses WHERE address_id = ?1", nativeQuery = true)
    public void deleteByAddressId(Long id);

    @Transactional
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


}
