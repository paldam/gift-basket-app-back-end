package com.damian.repository;

import com.damian.dto.FileDto;
import com.damian.model.DbFile;
import com.damian.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Damian on 12.08.2018.
 */
public interface DbFileDao extends JpaRepository<DbFile,Long> {
    @Override
    public List<DbFile> findAll() ;
    public List<DbFile> findByOrderId(Long id)   ;
    public DbFile findByFileId(Long id);
    public long countByOrderId(Long id)   ;
   



}
