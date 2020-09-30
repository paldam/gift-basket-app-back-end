package com.damian.domain.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProvinceDao extends JpaRepository<Province,Long> {

    public List<Province> findByName(String name);

}
