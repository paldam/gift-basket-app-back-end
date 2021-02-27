package com.damian.domain.prize;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface LoyaltyNumbersDao extends CrudRepository<LoyaltyNumbers,Long> {

    Optional<LoyaltyNumbers> findAllByNumber(String number);


}
