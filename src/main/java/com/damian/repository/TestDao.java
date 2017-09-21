package com.damian.repository;

import com.damian.model.Products;
import com.damian.model.Test;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface TestDao extends CrudRepository<Test,Long>{

    public List<Test> findAll();

}
