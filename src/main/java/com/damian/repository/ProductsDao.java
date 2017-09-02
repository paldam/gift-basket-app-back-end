package com.damian.repository;

import com.damian.model.Products;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**fe
 * Created by Damian on 29.08.2017.
 */


@Repository
@Transactional
public interface ProductsDao extends CrudRepository<Products,Long> {

    public List<Products> findAll();



}
