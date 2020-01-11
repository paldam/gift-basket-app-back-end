package com.damian.domain.product;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface ProductSubTypeDao extends CrudRepository<ProductSubType,Long>  {
        public List<ProductSubType> findAll();
        public Optional<ProductSubType> findBySubTypeId(Integer id);


}
