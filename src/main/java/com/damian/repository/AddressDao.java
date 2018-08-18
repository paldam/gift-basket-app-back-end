package com.damian.repository;

import com.damian.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Damian on 15.08.2018.
 */
public interface AddressDao extends JpaRepository<Address,Long> {
    @Override
    List<Address> findAll();
}
