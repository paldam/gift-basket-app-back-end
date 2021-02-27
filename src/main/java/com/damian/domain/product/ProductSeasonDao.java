package com.damian.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional(readOnly = true)
public interface ProductSeasonDao extends JpaRepository<ProductSeason,Long> {


    List<ProductSeason> findByIsActiveTrue();

}


