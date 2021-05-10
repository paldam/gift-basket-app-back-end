package com.damian.domain.basket;

import com.damian.domain.order.Order;
import com.damian.dto.BasketDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
public interface BasketDao extends CrudRepository<Basket, Long>, JpaSpecificationExecutor<Basket> {

    public List<Basket> findAllBy();
    public List<Basket> findAllByOrderByBasketIdDesc();

    public Basket findByBasketId(Long basketId);

    @QueryHints({ @QueryHint(name = "hibernate.query.passDistinctThrough", value = "false") })
    @Query(value = "SELECT distinct b FROM Basket b LEFT JOIN FETCH b.basketItems bi LEFT JOIN FETCH b.basketType bt LEFT JOIN FETCH b.basketSezon bs left join FETCH  bi.product p LEFT JOIN FETCH p.productSeason ps LEFT JOIN FETCH p.productSubType pst LEFT JOIN FETCH p.suppliers  WHERE b.basketId= ?1")
    public Optional<Basket> findById(Long basketId);

    @QueryHints({ @QueryHint(name = "hibernate.query.passDistinctThrough", value = "false") })
    @Query(value = "SELECT distinct bi.basketImage FROM Basket b join b.basketImage bi WHERE b.basketId=?1")
    public byte[] getBasketImageByBasketId(Long basketId);

    @Query(value = "SELECT NEW com.damian.dto.BasketDto(b) FROM Basket b WHERE b.basketType.basketTypeId != 99 AND b" +
        ".basketType.basketTypeId != 999")
    public List<BasketDto> findBasketDto();

    @Query(value = "SELECT * FROM baskets WHERE basket_type != 99 AND basket_type != 999", nativeQuery = true)
    public List<Basket> findAllWithoutDeleted();

    @Query(value = "SELECT * FROM baskets WHERE  basket_type != 999", nativeQuery = true)
    public List<Basket> findAllWithDeleted();

    @Query(value = "SELECT * FROM baskets WHERE basket_type = 100 ", nativeQuery = true)
    public List<Basket> findAllBasketForExternalPartner();

    @Query(value = "SELECT * FROM baskets WHERE basket_type = 99", nativeQuery = true)
    public List<Basket> findAllDeleted();

    @Query(value = "SELECT * FROM baskets WHERE basket_type = 100", nativeQuery = true)
    public List<Basket> findALLExportBasket();

    @Query(value = "select * from baskets INNER join basket_items on baskets.basket_id = basket_items.basket_id where" +
        " basket_items.product_id = ?1 and (baskets.basket_type = 1 Or baskets.basket_type =2) ", nativeQuery = true)
    public List<Basket> BasketListByProduct(Integer productId);

    @Transactional
    @Modifying
    @Query(value = "update baskets set stock =  stock + ?2 , last_stock_edit_date = CURRENT_TIMESTAMP" +
        " WHERE basket_id=?1", nativeQuery = true)
    void addBasketToStock(Long basketId, Integer addValue);

    @Transactional
    @Modifying
    @Query(value = "update baskets set stock =  stock - ?2 , last_stock_edit_date = CURRENT_TIMESTAMP WHERE basket_id" +
        " = ?1", nativeQuery = true)
    void minusBasketToStock(Long basketId, Integer minusValue);

    @Transactional
    @Modifying
    @Query(value = "update baskets set stock =  ?2 , last_stock_edit_date = CURRENT_TIMESTAMP WHERE basket_id = ?1",
        nativeQuery = true)
    void saveNewStockOfBasket(Long basketId, Integer newValue);

    @Query(value = "select * from baskets join basket_items on baskets.basket_id = basket_items.basket_id join " +
        "products ON basket_items.product_id = products.id" + " where product_sub_type_id IN ?3 AND baskets" +
        ".basket_total_price >= ?1 AND baskets.basket_total_price <=?2 GROUP BY baskets.basket_id having count" +
        "(distinct products.product_sub_type_id) = ?4", nativeQuery = true)
    public List<Basket> findBasketsWithFilter(Integer priceMin, Integer priceMax, List<Integer> subTypeList,
                                              Integer subTypeListLength);

    @Query(value = "select * from baskets join basket_items on baskets.basket_id = basket_items.basket_id join " +
        "products ON basket_items.product_id = products.id" + " where product_sub_type_id IN ?3 AND baskets" +
        ".basket_products_price >= ?1 AND baskets.basket_products_price <=?2 GROUP BY baskets.basket_id having count" +
        "(distinct products.product_sub_type_id) = ?4", nativeQuery = true)
    public List<Basket> findBasketsWithFilterByProductsPrice(Integer priceMin, Integer priceMax,
                                                             List<Integer> subTypeList, Integer subTypeListLength);

    @Query(value = "select * from baskets  join basket_items on baskets.basket_id = basket_items.basket_id  join " +
        "products ON basket_items.product_id = products.id" + " where  baskets.basket_total_price >= ?1 AND baskets" +
        ".basket_total_price <=?2 GROUP BY baskets.basket_id", nativeQuery = true)
    public List<Basket> findBasketsWithFilterWithoutTypes(Integer priceMin, Integer priceMax);

    @Query(value = "select * from baskets  join basket_items on baskets.basket_id = basket_items.basket_id  join " +
        "products ON basket_items.product_id = products.id" + " where  baskets.basket_products_price >= ?1 AND " +
        "baskets.basket_products_price <=?2 GROUP BY baskets.basket_id", nativeQuery = true)
    public List<Basket> findBasketsWithFilterWithoutTypesByProductsPrice(Integer priceMin, Integer priceMax);
}
