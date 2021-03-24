package com.damian.domain.order;

import com.damian.domain.product.ProductToOrder;
import com.damian.domain.product.ProductToOrderDto;
import com.damian.dto.NumberOfBasketOrderedByDate;
import com.damian.dto.ProductToCollectOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface OrderDao extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    public List<Order> findAllBy();
    public List<Order> findAllByOrderByOrderIdDesc();
    public List<Order> findAllByOrderStatus_OrderStatusId(Integer i);
    public Order findByOrderId(Integer id);

    @QueryHints({ @QueryHint(name = "hibernate.query.passDistinctThrough", value = "false") })
    @Query(value = "SELECT o FROM Order o LEFT JOIN FETCH o.productionUser pu LEFT JOIN FETCH o.address ad LEFT JOIN FETCH o.customer c LEFT JOIN FETCH c.company cp JOIN FETCH o.deliveryType dt JOIN FETCH o.orderStatus os LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.basket WHERE o.orderId = ?1")
    public Order findByOrderId(Long id);

    public List<Order> findByAddress_AddressId(Long id);

    public List<Order> findByCustomer_CustomerId(Integer id);

    @Query(value = "SELECT * FROM orders WHERE DATE (order_date) = CURDATE() AND order_status_id != 99 ",
        nativeQuery = true)
    public Optional<List<Order>> findAllTodaysOrders();


    @Query(value = "SELECT distinct o FROM Order o LEFT JOIN FETCH o.productionUser pu LEFT JOIN FETCH o.address ad" +
        " LEFT JOIN FETCH o.customer c LEFT JOIN FETCH c.company cp JOIN FETCH o.deliveryType dt" +
        " JOIN FETCH o.orderStatus os LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.basket WHERE o.orderStatus.orderStatusId= ?1")
     List<Order> findAllOrderByOrderId(Integer orderStatus );

    @QueryHints({ @QueryHint(name = "hibernate.query.passDistinctThrough", value = "false") })
    @Query(value = "SELECT distinct o FROM Order o LEFT JOIN FETCH o.productionUser pu LEFT JOIN FETCH o.address ad" +
        " LEFT JOIN FETCH o.customer c LEFT JOIN FETCH c.company cp JOIN FETCH o.deliveryType dt" +
        " JOIN FETCH o.orderStatus os LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.basket b left Join fetch b.basketSezon bs LEFT JOIN FETCH b.basketType bt WHERE o.orderId IN ?1")
    List<Order> findAllOrderWithCollectionsByIds(List<Long> ids, Sort sort);

    @Query(value = "SELECT * FROM orders Join order_items on orders.order_id = order_items.order_id where order_items" +
        ".order_item_id = ?1  ", nativeQuery = true)
    public Order findOrderByOrderItemId(Long orderItemId);

    @QueryHints({ @QueryHint(name = "hibernate.query.passDistinctThrough", value = "false") })
    @Query(value = "SELECT distinct o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH o.customer c LEFT JOIN FETCH o.orderStatus os LEFT JOIN FETCH o.deliveryType dte LEFT JOIN FETCH c.company LEFT JOIN FETCH o.productionUser pu WHERE c.customerId = ?1")
    public List<Order> findByCustomerId(Integer id);

    @Query(value = "Select REV from orders_audit where (order_id = (SELECT order_id FROM `orders_audit` WHERE REV = " +
        "?1)) AND  REV < ?1 ORDER BY REV DESC Limit 1;", nativeQuery = true)
    public Optional<BigInteger> getRevisionNumberOFfPreviousOrderState(Long id);

    @Query(value = "select audit_info.rev_id, audit_info.change_time, audit_info.user, orders_audit.order_id  from " +
        "audit_info join orders_audit on audit_info.rev_id = orders_audit.REV where orders_audit.order_id = ?1 Order " +
        "By audit_info.change_time DESC ", nativeQuery = true)
    public List<Object[]> getOrderHistoryById(Integer id);

    @Transactional
    @Modifying
    @Query(value = "update orders set stock =  stock + ?2  WHERE id = ?1", nativeQuery = true)
    void changeStatus(Integer id);

    @Transactional
    @Modifying
    @Query(value = "update order_items set warehouse_state = ?2  WHERE order_item_id  = ?1", nativeQuery = true)
    void changeSpecyfiedOrderItemProgressOnWarehouse(Integer orderItemId, Long newStateValueOnWarehouse);

    @Transactional
    @Modifying
    @Query(value = "update order_items set production_state = ?2  WHERE order_item_id  = ?1", nativeQuery = true)
    void changeSpecyfiedOrderItemProgressOnProduction(Integer orderItemId, Long newStateValueOnProduction);

    @Transactional
    @Modifying
    @Query(value = "update order_items set logistics_state = ?2  WHERE order_item_id  = ?1", nativeQuery = true)
    void changeSpecyfiedOrderItemProgressOnLogistics(Integer orderItemId, Long newStateValueOnLogistics);

    @Transactional
    @Modifying
    @Query(value = "update order_items set warehouse_state = warehouse_state + ?2  WHERE order_item_id  = ?1",
        nativeQuery = true)
    void changeSpecifiedOrderItemProgressOnWarehouseByAddValue(Integer orderItemId, Long newStateValueOnWarehouse);

    @Transactional
    @Modifying
    @Query(value = "update order_items set production_state = production_state + ?2  WHERE order_item_id  = ?1",
        nativeQuery = true)
    void changeSpecifiedOrderItemProgressOnProductionByAddValue(Integer orderItemId, Long newStateValueOnProduction);

    @Transactional
    @Modifying
    @Query(value = "update order_items set logistics_state = logistics_state + ?2  WHERE order_item_id  = ?1",
        nativeQuery = true)
    void changeSpecifiedOrderItemProgressOnLogisticsByAddValue(Integer orderItemId, Long newStateValueOnLogistics);

    @Transactional
    @Modifying
    @Query(value = "update orders set paid = ?2  WHERE order_id  = ?1", nativeQuery = true)
    void changePaymentStatus(Long orderId, Integer status);



    @Transactional
    @Modifying
    @Query(value = "update orders set production_user =  ?2  WHERE order_id IN  ?1", nativeQuery = true)
    public void assignOrdersToSpecifiedProduction(List<Integer> ordersIds, Long productionId);

    @Transactional
    @Modifying
    @Query(value = "update orders set is_ready_to_loyalty_program = true  WHERE order_id IN  ?1", nativeQuery = true)
    public void markAsReadyToLoyaltyProgram(List<Integer> ordersIds);

    @Query(value = "SELECT * FROM orders WHERE order_id IN ?1", nativeQuery = true)
    public List<Order> findByOrderIds(List<Integer> ordersIds);

    @Query(value = "SELECT * FROM orders WHERE order_status_id =5 AND loyalty_user is not null And " +
        "allready_computed_points = false and order_date > '2019.09.10'", nativeQuery = true)
    public List<Order> findAllOrderForLoyaltyProgram();

    @Query(value = "SELECT * FROM orders WHERE production_user = ?1 and  order_status_id !=99 and order_date > '2020.06.01'  ORDER BY order_date " +
        "DESC", nativeQuery = true)
    public List<Order> getAllOrdersByProductionUserId(Long id);

    @Query(value = "select o from Order o join fetch o.orderStatus os join fetch o.deliveryType dt join fetch  o.orderItems oi join fetch o.customer c join fetch c.company join fetch oi.basket ba " +
        "where ba.basketId =?1 and o.deliveryDate >= ?2 AND o.deliveryDate <=?3  AND  (os.orderStatusId =1 Or os.orderStatusId =6) ")
    public List<Order> findAllOrderByBasketIdAndDeliveryDate(Long basketId, Date startDate, Date endDate);

    @Query(value = "select o from Order o join fetch o.orderStatus os join fetch o.deliveryType dt left join fetch  o.orderItems oi left join fetch o.customer c left join fetch c.company left join fetch oi.basket ba " +
        "where ba.basketId =?1 and o.orderDate >= ?2 AND o.orderDate <=?3  AND  (os.orderStatusId =1 Or os.orderStatusId =6) ")
    public List<Order> findAllOrderByBasketIdAndOrderDate(Long basketId, Date startDate, Date endDate);

    @Query(value = "select new com.damian.dto.NumberOfBasketOrderedByDate(b.basketId,b.basketName,sum(oi.quantity)," +
        "count(o.orderId)) FROM Order o JOIN  o.orderStatus os JOIN o.orderItems oi JOIN oi.basket b  where " + "o.orderDate >= ?1 and o" +
        ".orderDate <= ?2  AND  os.orderStatusId != 99 group by b.basketName")
    public List<NumberOfBasketOrderedByDate> getNumberOfBasketOrderedFilteredByOrderDate(Date startDate, Date endDate);

    @Query(value = "select new com.damian.dto.NumberOfBasketOrderedByDate(b.basketId,b.basketName,sum(oi.quantity)," +
        "count(o.orderId)) FROM Order o JOIN o.orderItems oi JOIN oi.basket b  where " + "o.deliveryDate >= ?1  and  " +
        "o.deliveryDate <= ?2 and  o.orderStatus != 99  group by b.basketName")
    public List<NumberOfBasketOrderedByDate> getNumberOfBasketOrdered(Date startDate, Date endDate);



    @Query(value = "SELECT YEAR(order_date) FROM `orders` WHERE 1 GROUP BY YEAR(order_date) ORDER BY order_date DESC"
        , nativeQuery = true)
    public int[] getOrdersYears();

    @Query(value = "SELECT * FROM orders WHERE order_status_id != 99 ORDER BY order_date DESC ", nativeQuery = true)
    public List<Order> findAllWithoutDeleted();

    @Query(value = "SELECT count(*) FROM orders WHERE order_status_id != 99", nativeQuery = true)
    public long getCountOfAllOrdersWithoutDeleted();

    @Query(value = "SELECT * FROM orders WHERE order_status_id != 99 ", nativeQuery = true)
    public Page<Order> findAllWithoutDeletedPage(Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE order_status_id != 99 ", nativeQuery = true)
    public Page<Order> findAllWithoutDeletedSortByOrderDate(Pageable pageable);

    @Query(value = "SELECT * FROM orders as orders JOIN customers as customers ON orders.customer_id = customers" +
        ".customer_id " + "WHERE orders.order_status_id != 99 " + "AND (orders.fv_number LIKE %?1% OR orders" +
        ".additional_information LIKE %?1% OR customers.organizationName LIKE %?1% OR customers.name LIKE %?1%)",
        nativeQuery = true)
    public Page<Order> findAllWithoutDeletedWithSearchFilter(String text, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE delivery_date BETWEEN ?1 AND ?2 AND delivery_type =3", nativeQuery =
        true)
    public List<Order> findOrdersByDateRange(Date startDate, Date endDate);

    @Query(value = "select p.id as id, p.productName as productName, p.stock as stock, p.tmpOrdered as tmpOrdered," +
        " p.capacity as capacity , p.lastNumberOfOrderedEditDate as lastNumberOfOrderedEditDate ,s as suppliers, st as productSubType, 0 as valueForDeliver, (sum(oi.quantity*bi.quantity)-sum(oi.stateOnWarehouse *bi.quantity)) as suma " +
        "from Order o join o.orderItems oi join oi.basket ba join ba.basketItems bi join bi.product p left join  p.suppliers s " +
        "LEFT join  p.productSubType st left join fetch  st.productType pt " +
        "where (o.orderStatus.orderStatusId=1 Or o.orderStatus.orderStatusId=6) AND o.orderDate >= ?1 AND o.orderDate <=?2 GROUP BY p.id")
    public List<ProductToOrderDto> findProductToOrder(Date startDate, Date endDate);


    @Query(value = "SELECT NEW com.damian.domain.product.ProductToOrder(p,(sum(oi.quantity*bi.quantity)-sum(oi.stateOnWarehouse *bi.quantity)),p" +
        ".lastNumberOfOrderedEditDate) FROM Order o JOIN o.orderItems oi " + "JOIN oi.basket b " + "JOIN b" +
        ".basketItems bi JOIN bi.product p left join  p.suppliers left join p.productSubType WHERE (o.orderStatus.orderStatusId=1 OR o.orderStatus.orderStatusId=6) AND" +
        " o.deliveryDate >= ?1 AND o.deliveryDate <= ?2 AND o.paid =1  GROUP BY p.id")
    public List<ProductToOrder> findProductToOrder2(Date startDate, Date endDate);


    @Query(value = "select p.id as id, p.productName as productName, p.stock as stock, p.tmpOrdered as tmpOrdered," +
        " p.capacity as capacity ,p.price as price, p.lastNumberOfOrderedEditDate as lastNumberOfOrderedEditDate ,s as suppliers, st as productSubType, 0 as valueForDeliver, sum(oi.quantity*bi.quantity) as suma " +
        "from Order o join o.orderItems oi join oi.basket ba join ba.basketItems bi join bi.product p left join  p.suppliers s " +
        "LEFT join  p.productSubType st left join fetch  st.productType pt " +
        "where (o.orderStatus.orderStatusId != 99)  AND o.orderDate >= ?1 AND o.orderDate <=?2 GROUP BY p.id")
    public List<ProductToOrderDto> findProductToOrderWithoutDeletedOrderByOrderDate(Date startDate, Date endDate);

    @Query(value = "SELECT NEW com.damian.dto.ProductToCollectOrder(p.productName,sum(oi.quantity*bi.quantity),p" +
        ".capacity) FROM Order o JOIN o.orderItems oi " + "JOIN oi.basket b " + "JOIN b.basketItems bi JOIN bi" +
        ".product p WHERE  o.orderId = ?1  GROUP BY p.productName")
    public List<ProductToCollectOrder> findProductToCollectOrder(Long orderId);

    @Query(value = "SELECT NEW com.damian.dto.OrderDto(o.orderId,o.orderFvNumber,o.customer, o.orderDate,o" +
        ".additionalInformation,o.deliveryDate," + " o.deliveryType,o.orderStatus, o.orderTotalAmount, o.address, f" +
        ".dbFile) FROM Order o JOIN a. DbFile", nativeQuery = true)
    public List<Order> getOrdersDto();
}
