package com.damian.repository;

import com.damian.model.ProductType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProductTypeDao extends CrudRepository<ProductType,Long> {


    public List<ProductType> findAll();
}
