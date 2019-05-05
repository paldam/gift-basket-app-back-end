package com.damian.boundry.rest;

import com.damian.dto.FileDto;
import com.damian.dto.ProductToCollectOrder;
import com.damian.domain.order_file.DbFile;
import com.damian.domain.order.Order;
import com.damian.domain.order.OrderItem;
import com.damian.domain.order_file.DbFileDao;
import com.damian.domain.order.OrderDao;
import com.damian.domain.order_file.DbFileService;
import com.damian.domain.order.OrderService;
import com.damian.util.PdfDeliveryConfirmation;
import com.damian.util.PdfGenerator;
import com.damian.util.PdfMultiDeliveryConfirmation;
import com.damian.util.PdfProductToCollectOrder;
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
import java.util.ArrayList;
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
    private OrderService orderService;
    public FileController(DbFileDao dbFileDao, DbFileService dbFileService, OrderDao orderDao, OrderService orderService) {
        this.dbFileDao= dbFileDao;
        this.dbFileService = dbFileService;
        this.orderDao= orderDao;
        this.orderService = orderService;
    }

    @CrossOrigin
    @GetMapping(value = "/orderfile/{id}")
    ResponseEntity<List<DbFile>> getFiles(@PathVariable Long id){
        List<DbFile> fileList = dbFileDao.findByOrderId(id) ;
        return new ResponseEntity<List<DbFile>>(fileList, HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping(value = "/orderfiledto")
    ResponseEntity<List<FileDto>> getFilesDto(){
        List<FileDto> fileDtoList = dbFileService.getFileDto() ;
        return new ResponseEntity <List<FileDto>>(fileDtoList, HttpStatus.OK);
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



        DbFile dbFile = dbFileDao.findByFileId(fileId);

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
            dbFileDao.deleteById(fileId);
            return new ResponseEntity(fileId, HttpStatus.OK);
        }
    }


    @CrossOrigin
    @RequestMapping(value = "/order/pdf/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPdf(@PathVariable Long id) throws IOException {


        Order orderToGenerate = orderDao.findByOrderId(id);
        List<Order> orderList = new ArrayList<>();
        orderList.add(orderToGenerate);
        PdfGenerator pdfGenerator = new PdfGenerator();
        ByteArrayInputStream bis = pdfGenerator.generatePdf(orderList);

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
    @RequestMapping(value = "/order/pdf/product_to_collect/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getProductsToCollectOrder(@PathVariable Long id) throws IOException {


        List<ProductToCollectOrder> productToCollectOrderTmp = orderDao.findProductToCollectOrder(id);

       PdfProductToCollectOrder pdfGenerator = new PdfProductToCollectOrder();
        ByteArrayInputStream bis = pdfGenerator.generateProductToCollectOrderPdf(productToCollectOrderTmp,id);

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
    @RequestMapping(value = "/order/multipdf", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getMultiPdf(@RequestParam("ordersIdList") List<Long> ordersToPrintList) throws IOException {

        orderService.getOrderListFromIdList(ordersToPrintList);


        PdfGenerator pdfGenerator = new PdfGenerator();
        ByteArrayInputStream bis = pdfGenerator.generatePdf(orderService.getOrderListFromIdList(ordersToPrintList));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }



    @CrossOrigin
    @RequestMapping(value = "/order/multideliverypdf", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getMultiDeliveryPdf(@RequestParam("ordersIdList") List<Long> ordersToPrintList) throws IOException {

        orderService.getOrderListFromIdList(ordersToPrintList);


        PdfMultiDeliveryConfirmation pdfMultiDeliveryConfirmation = new PdfMultiDeliveryConfirmation();
        ByteArrayInputStream bis = pdfMultiDeliveryConfirmation.generateMultiPdf(orderService.getOrderListFromIdList(ordersToPrintList));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }



    @CrossOrigin
    @RequestMapping(value = "/order/deliverypdf/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getDeliveryPdf(@PathVariable Long id) throws IOException {


        Order orderToGenerate = orderDao.findByOrderId(id);
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




    @CrossOrigin
    @RequestMapping(value = "/order/deliverypdfwithmodyfication/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getDeliveryPdfwithModyfications(@PathVariable Long id, @RequestBody List<OrderItem> orderItems) throws IOException {


        Order orderToGenerate = orderDao.findByOrderId(id);
        orderToGenerate.setOrderItems(orderItems);
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
