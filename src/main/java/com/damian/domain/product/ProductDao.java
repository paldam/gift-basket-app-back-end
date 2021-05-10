package com.damian.domain.product;

import com.damian.dto.NumberProductsToChangeStock;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
@Repository
@Transactional(readOnly = true)
public interface ProductDao extends CrudRepository<Product, Long> , JpaSpecificationExecutor<Product> {

    public List<Product> findAllByOrderByIdDesc();
    public List<Product> findAllBy();
    public List<Product> findAllByIsArchivalNot(Integer i);
    public Product findById(Integer id);
    public Optional<Product> findAllById(Integer id);
    public void deleteById(Integer id);


    @QueryHints({ @QueryHint(name = "hibernate.query.passDistinctThrough", value = "false") }) // tip to don't pass distinct to sql
    @Query(value = "SELECT  DISTINCT p   FROM Product p LEFT JOIN FETCH p.productSubType pst left JOIN fetch p.productSeason left Join FETCH p.suppliers LEFT JOIN FETCH pst.productType WHERE p.id =?1")
    public Optional<Product> findOneWithSubTypeProductSeasonSupplierProductType(Integer id);

    @Query(value = "SELECT * from products Join product_suppliers  on products.id = product_suppliers.id WHERE " +
        "product_suppliers.supplier_id= ?1 AND products.is_archival = 0;", nativeQuery = true)
    public List<Product> findBySupplier_SupplierId(Integer id);

    @QueryHints({ @QueryHint(name = "hibernate.query.passDistinctThrough", value = "false") })
    @Query(value = "SELECT  DISTINCT p   FROM Product p LEFT JOIN FETCH p.productSubType pst left JOIN fetch p.productSeason left Join FETCH p.suppliers LEFT JOIN FETCH pst.productType WHERE p.isArchival != 1 or p.isArchival = null")
    public List<Product> findAllWithoutDeleted();

    @QueryHints({ @QueryHint(name = "hibernate.query.passDistinctThrough", value = "false") })
    @Query(value = "SELECT DISTINCT p   FROM Product p LEFT JOIN FETCH p.productSubType pst left JOIN fetch p.productSeason left Join FETCH p.suppliers LEFT JOIN FETCH pst.productType WHERE p.id IN ?1 AND p.isArchival = 0")
    public List<Product> findAllWithoutDeletedByIds(List<Integer> ids, Sort sort);

    @Transactional
    @Modifying
    @Query(value = "update products set supplier_id =  ?1 WHERE id = ?2", nativeQuery = true)
    void update(Integer supp, Integer prod_id);

    @Transactional
    @Modifying
    @Query(value = "update products set stock =  stock + ?2, last_stock_edit_date = CURRENT_TIMESTAMP  WHERE id = ?1"
        , nativeQuery = true)
    void updateStock(Integer productId, Integer addValue);

    @Transactional
    @Modifying
    @Query(value = "update products set tmp_ordered =  tmp_ordered + ?2 , last_number_of_ordered_edit_date = " +
        "CURRENT_TIMESTAMP WHERE id = ?1", nativeQuery = true)
    void addProductToDeliver(Integer productId, Integer addValue);

    @Transactional
    @Modifying
    @Query(value = "update products set tmp_ordered =  0  WHERE id = ?1", nativeQuery = true)
    void resetProductToDeliver(Integer productId);

    @Transactional
    @Modifying
    @Query(value = "update products set tmp_ordered =  ?2  WHERE id = ?1", nativeQuery = true)
    void setProductToDeliver(Integer productId, Integer valueToSet);

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
    void updateStockTmpAdd(Integer productId, Integer addValue);

    @Transactional
    @Modifying
    @Query(value = "update products set tmp_stock = tmp_stock - ?2 WHERE id = ?1", nativeQuery = true)
    void updateStockTmpMinus(Integer productId, Long minusValue);

    @Transactional
    @Modifying
    @Query(value = "update products set tmp_stock = 0, stock = 0, tmp_ordered = 0", nativeQuery = true)
    void resetDbPrductsStates();

    @Query(value = "SELECT NEW com.damian.dto.NumberProductsToChangeStock(p.id,sum(oi.quantity*bi.quantity)) FROM " +
        "Order o JOIN o.orderItems oi " + "JOIN oi.basket b " + "JOIN b.basketItems bi JOIN bi.product p WHERE o" +
        ".orderId=?1  GROUP BY p.id")
    public List<NumberProductsToChangeStock> numberProductsToChangeStock(Long id);

    @Query(value = "SELECT image FROM products WHERE id=?1", nativeQuery = true)
    public byte[] getProductImageByBasketId(Long productId);
}
