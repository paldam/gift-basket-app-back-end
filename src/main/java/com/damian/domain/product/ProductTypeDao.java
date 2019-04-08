package com.damian.domain.product;

import com.damian.domain.product.ProductType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProductTypeDao extends CrudRepository<ProductType,Long> {


    public List<ProductType> findAll();
}
