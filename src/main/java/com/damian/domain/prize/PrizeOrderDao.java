package com.damian.domain.prize;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PrizeOrderDao extends JpaRepository<PrizeOrder, Long> {






    public List<PrizeOrder> findAllByOrderByOrderDateDesc();
    public Optional<PrizeOrder> findById(Long id);

    @Query(value = "SELECT * FROM prize_orders WHERE prize_orders.user = ?1", nativeQuery = true)
    public List<PrizeOrder> findAllByUser(Long id);
}
