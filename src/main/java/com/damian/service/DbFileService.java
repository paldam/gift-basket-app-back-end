package com.damian.service;

import com.damian.dto.FileDto;
import com.damian.model.DbFile;
import com.damian.repository.DbFileDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DbFileService {
    private static final Logger logger = Logger.getLogger(DbFileService.class);

    private DbFileDao dbFileDao;

    public DbFileService(DbFileDao dbFileDao) {
        this.dbFileDao=dbFileDao;
    }

     public void uploadFiles(MultipartFile[] files, Long orderId) {



         Arrays.asList(files).forEach(file -> {
             DbFile uploadedFile = new DbFile();
             uploadedFile.setFileName(file.getOriginalFilename());
             uploadedFile.setFileType(file.getContentType());
             uploadedFile.setOrderId(orderId);

             try{
                 uploadedFile.setData(file.getBytes());
             } catch (IOException ex) {
                 ex.printStackTrace();
             }
             dbFileDao.save(uploadedFile);
         });
         
     }


     public List<FileDto>  getFileDto (){
           List<DbFile> fileList = dbFileDao.findAll();
         List<FileDto> fileDtoList = new ArrayList<>();
         fileList.forEach(file -> {

                 fileDtoList.add(new FileDto(file)) ;
           } );

          return fileDtoList;

     }

}
