package com.damian.domain.order;
import com.damian.domain.order.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryDao extends JpaRepository<OrderHistory,Long>{

}
