package com.damian.domain.prize;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface PrizeOrderDao extends JpaRepository<PrizeOrder, Long> {

    List<PrizeOrder> findAllByOrderByOrderDateDesc();
    Optional<PrizeOrder> findById(Long id);

    @Query(value = "SELECT * FROM prize_orders WHERE prize_orders.user = ?1", nativeQuery = true)
    List<PrizeOrder> findAllByUser(Long id);
}
