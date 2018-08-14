package com.damian.rest;

import com.damian.model.DbFile;
import com.damian.model.Order;
import com.damian.repository.DbFileDao;
import com.damian.repository.OrderDao;
import com.damian.service.DbFileService;
import com.damian.util.PdfDeliveryConfirmation;
import com.damian.util.PdfGenerator;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by Damian on 14.08.2018.
 */
@RestController
public class FileController {

    private DbFileDao dbFileDao;
    private DbFileService dbFileService;
    private OrderDao orderDao;
    public FileController(DbFileDao dbFileDao, DbFileService dbFileService, OrderDao orderDao) {
        this.dbFileDao= dbFileDao;
        this.dbFileService = dbFileService;
        this.orderDao= orderDao;
    }

    @CrossOrigin
    @GetMapping(value = "/orderfile/{id}")
    ResponseEntity<List<DbFile>> getFiles(@PathVariable Long id){
        List<DbFile> fileList = dbFileDao.findByOrderId(id) ;
        return new ResponseEntity<List<DbFile>>(fileList, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/uploadfiles")
    public ResponseEntity uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("orderId") Long orderId)  {


        dbFileService.uploadFiles(files,orderId);

        return new ResponseEntity<>("ds",HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/file/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {


        DbFile dbFile = dbFileDao.findOne(fileId);

        HttpHeaders header  = new HttpHeaders();
        header.setAccessControlExposeHeaders(Collections.singletonList("Content-Disposition"));;
        header.set("Content-Disposition", "attachment; filename=" + dbFile.getFileName());


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .headers(header)
                .body(new ByteArrayResource(dbFile.getData()));
    }

    @CrossOrigin
    @DeleteMapping("/file/delete/{fileId}")
    public ResponseEntity deleteFile(@PathVariable Long fileId) {
        

        DbFile selectedFile = dbFileDao.findByFileId(fileId) ;

        if (Objects.isNull(selectedFile)) {
            return new ResponseEntity("Nie znaleziono pliku o id " + fileId, HttpStatus.NOT_FOUND);
        }else{
            dbFileDao.delete(fileId);
            return new ResponseEntity(fileId, HttpStatus.OK);
        }
    }


    @CrossOrigin
    @RequestMapping(value = "/order/pdf/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPdf(@PathVariable Long id) throws IOException {


        Order orderToGenerate = orderDao.findOne(id);
        PdfGenerator pdfGenerator = new PdfGenerator();
        ByteArrayInputStream bis = pdfGenerator.generatePdf(orderToGenerate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");
        new InputStreamResource(bis)  ;
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @CrossOrigin
    @RequestMapping(value = "/order/deliverypdf/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getDeliveryPdf(@PathVariable Long id) throws IOException {


        Order orderToGenerate = orderDao.findOne(id);
        PdfDeliveryConfirmation pdfDeliveryConfirmation  = new PdfDeliveryConfirmation();
        ByteArrayInputStream bis = pdfDeliveryConfirmation.generatePdf(orderToGenerate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
    
}
