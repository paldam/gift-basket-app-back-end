package com.damian.domain.order_file;

import com.damian.domain.order_file.DbFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Damian on 12.08.2018.
 */
@Transactional(readOnly = true)
public interface DbFileDao extends JpaRepository<DbFile,Long> {
    @Override
    public List<DbFile> findAll() ;
    public List<DbFile> findByOrderId(Long id)   ;
    public Optional<DbFile> findByFileId(Long id);
    public long countByOrderId(Long id)   ;
   



}
