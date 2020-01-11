package com.damian.domain.product;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface ProductTypeDao extends CrudRepository<ProductType,Long> {
    public List<ProductType> findAll();
    public Optional<ProductType> findByTypeId(Integer id);
}
