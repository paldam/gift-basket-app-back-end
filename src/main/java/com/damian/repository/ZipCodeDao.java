package com.damian.repository;

import com.damian.model.ZipCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ZipCodeDao extends JpaRepository<ZipCode,Long> {

    public List<ZipCode> findByZipCodeCode(String code);
   

    @Query(value = "SELECT * FROM zip_codes WHERE code = 1?", nativeQuery = true)
    public List<ZipCode> findCitysByZipCode(String code);
}
