package com.damian.repository;

import com.damian.domain.order_file.DbFile;
import org.springframework.data.jpa.repository.JpaRepository;

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
