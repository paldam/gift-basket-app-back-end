package com.damian.domain.prize;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PrizeDao extends CrudRepository<Prize,Long> {


    public List<Prize> findAllBy();

    @Query(value = "SELECT * FROM prizes WHERE isAvailable = 1", nativeQuery = true)
    public List<Prize> findAllWithoutDel();

}
