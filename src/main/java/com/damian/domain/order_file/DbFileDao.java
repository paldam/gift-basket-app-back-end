package com.damian.domain.order_file;

import com.damian.domain.order_file.DbFile;
import com.damian.dto.FileDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by Damian on 12.08.2018.
 */
public interface DbFileDao extends JpaRepository<DbFile,Long> {
    @Override
    public List<DbFile> findAll() ;

    @Query(value = "SELECT NEW com.damian.dto.FileDto(d.fileId,d.orderId) FROM DbFile d ")
    public List<FileDto> findAllFileDto() ;

    public List<DbFile> findByOrderId(Long id)   ;
    public Optional<DbFile> findByFileId(Long id);
    public long countByOrderId(Long id)   ;
   



}
