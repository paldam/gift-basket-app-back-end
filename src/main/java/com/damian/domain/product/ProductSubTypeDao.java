package com.damian.domain.product;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductSubTypeDao extends CrudRepository<ProductSubType,Long>  {


        public List<ProductSubType> findAll();
        public ProductSubType findBySubTypeId(Integer id);


}
