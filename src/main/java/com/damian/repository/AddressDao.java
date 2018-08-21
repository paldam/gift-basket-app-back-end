package com.damian.repository;

import com.damian.model.Address;
import com.damian.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Damian on 15.08.2018.
 */
public interface AddressDao extends JpaRepository<Address,Long> {
    @Override
    List<Address> findAll();

    @Query(value = "SELECT * FROM addresses WHERE customer_id = ?1 AND is_primary_address = 1 LIMIT 1 ", nativeQuery = true)
    public Address findCustomerPrimaryAddrById(Integer id);


}
