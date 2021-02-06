package com.damian.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSeasonDao extends JpaRepository<ProductSeason,Long> {


    List<ProductSeason> findByIsActiveTrue();

}


