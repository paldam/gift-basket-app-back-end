package com.damian.domain.prize;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrizeOrderDao extends JpaRepository<PrizeOrder, Long> {


    public List<PrizeOrder> findAllByOrderByOrderDateDesc();
    public Optional<PrizeOrder> findById(Long id);


}
