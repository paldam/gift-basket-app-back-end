package com.damian.domain.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional(readOnly = true)
public interface ProvinceDao extends JpaRepository<Province,Long> {

    public List<Province> findByName(String name);

}
