package com.damian.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface ProductTypeDao extends JpaRepository<ProductType,Long> {
    List<ProductType> findAll();
    Optional<ProductType> findByTypeId(Integer id);

}
