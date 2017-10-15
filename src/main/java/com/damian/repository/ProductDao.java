package com.damian.repository;

import com.damian.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**fe
 * Created by Damian on 29.08.2017.
 */


@Repository
@Transactional
public interface ProductDao extends CrudRepository<Product,Long> {

    public List<Product> findAllByOrderByIdDesc();
    public List<Product> findAllBy();
    public Product findById(Integer id);
    public void deleteById(Integer id);

}
