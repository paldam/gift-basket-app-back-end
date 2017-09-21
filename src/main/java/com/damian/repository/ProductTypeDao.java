package com.damian.repository;

import com.damian.model.ProductsTypes;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProductTypeDao extends CrudRepository<ProductsTypes,Long> {


    public List<ProductsTypes> findAll();
}
