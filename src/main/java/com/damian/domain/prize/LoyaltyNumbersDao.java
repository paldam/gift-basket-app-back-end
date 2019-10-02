package com.damian.domain.prize;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LoyaltyNumbersDao extends CrudRepository<LoyaltyNumbers,Long> {

    Optional<LoyaltyNumbers> findAllByNumber(String number);


}
