package com.damian.repository;

import com.damian.dto.NumberProductsToChangeStock;
import com.damian.model.Basket;
import com.damian.model.Product;
import com.damian.model.ProductToOrder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**fe
 * Created by Damian on 29.08.2017.
 */


@Repository
@Transactional
public interface ProductDao extends CrudRepository<Product,Long> {

    public List<Product> findAllByOrderByIdDesc();

    public List<Product> findAllBy();

    public List<Product> findAllByIsArchivalNot(Integer i);

    public Product findById(Integer id);

    public void deleteById(Integer id);

    public List<Product> findBySupplier_SupplierId(Integer id);

    @Query(value = "SELECT * FROM products WHERE is_archival != 1 or is_archival = null", nativeQuery = true)
    public List<Product> findAllWithoutDeleted();

    @Transactional
    @Modifying
    @Query(value = "update products set supplier_id =  ?1 WHERE id = ?2", nativeQuery = true)
    void update(Integer supp, Integer prod_id);


    @Transactional
    @Modifying
    @Query(value = "update products set stock =  stock + ?2, last_stock_edit_date = CURRENT_TIMESTAMP  WHERE id = ?1", nativeQuery = true)
    void updateStock(Integer productId,Integer addValue);


    @Transactional
    @Modifying
    @Query(value = "update products set stock =  stock + ?2  WHERE id = ?1", nativeQuery = true)
    void updateStockAdd(Integer productId, Long addValue);

    @Transactional
    @Modifying
    @Query(value = "update products set stock =  stock -?2 WHERE id = ?1", nativeQuery = true)
    void updateStockMinus(Integer productId, Long minusValue);

    @Transactional
    @Modifying
    @Query(value = "update products set tmp_stock = tmp_stock + ?2 WHERE id = ?1", nativeQuery = true)
    void updateStockTmpAdd(Integer productId, Long addValue);

    @Transactional
    @Modifying
    @Query(value = "update products set tmp_stock = tmp_stock - ?2 WHERE id = ?1", nativeQuery = true)
    void updateStockTmpMinus(Integer productId, Long minusValue);



    @Query(value = "SELECT NEW com.damian.dto.NumberProductsToChangeStock(p.id,sum(oi.quantity*bi.quantity)) FROM Order o JOIN o.orderItems oi " +
            "JOIN oi.basket b " +
            "JOIN b.basketItems bi JOIN bi.product p WHERE o.orderId=?1  GROUP BY p.id")

    public List<NumberProductsToChangeStock> numberProductsToChangeStock(Long id);
}