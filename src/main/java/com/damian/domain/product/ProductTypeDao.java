package com.damian.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductTypeDao extends JpaRepository<ProductType,Long> {
    List<ProductType> findAll();
    Optional<ProductType> findByTypeId(Integer id);


    @Query(value = "SELECT * from products_types WHERE is_active_for_pdf_export = 0 ", nativeQuery = true)
    public List<ProductType> findInactive();

    @Transactional
    @Modifying
    @Query(value = "update products_types set is_active_for_pdf_export =  0 WHERE type_id = ?1", nativeQuery = true)
    void setInactive( Integer prod_id);

    @Transactional
    @Modifying
    @Query(value = "update products_types set is_active_for_pdf_export =  1 WHERE type_id = ?1", nativeQuery = true)
    void setActive( Integer prod_id);
}
