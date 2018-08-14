package com.damian.rest;

import com.damian.model.*;
import com.damian.repository.*;
import com.damian.service.DbFileService;
import com.damian.service.OrderService;
import com.damian.util.PdfDeliveryConfirmation;
import com.damian.util.PdfGenerator;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.MultipartConfigElement;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    private static final Logger logger = Logger.getLogger(OrderController.class);

    private OrderDao orderDao;
    private OrderService orderService;
    private OrderStatusDao orderStatusDao;
    private ProductDao productDao;
    private DbFileDao dbFileDao;
    private DbFileService dbFileService;
    OrderController(DbFileService dbFileService, OrderDao orderDao, OrderService orderService, DeliveryTypeDao deliveryTypeDao, OrderStatusDao orderStatusDao, ProductDao productDao,
                    DbFileDao dbFileDao){
        this.orderDao=orderDao;
        this.orderService=orderService;
        this.orderStatusDao=orderStatusDao;
        this.productDao=productDao;
        this.dbFileDao= dbFileDao;
        this.dbFileService = dbFileService;
        
    }
    @CrossOrigin
    @GetMapping(value = "/order/{id}")
    ResponseEntity<Order> getOrder(@PathVariable Long id){
        Order order = orderDao.findOne(id);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/orders")
    ResponseEntity<List<Order>> getOrders(){
        List<Order> ordersList = orderDao.findAllWithoutDeleted();
        return new ResponseEntity<List<Order>>(ordersList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/orders/daterange")
    ResponseEntity<List<Order>> getOrdersByDateRange(
            @RequestParam(value="startDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(value="endDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate){
        List<Order> ordersList = orderDao.findOrdersByDateRange(startDate,endDate);
        return new ResponseEntity<List<Order>>(ordersList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/order_status")
    ResponseEntity<List<OrderStatus>> getOrderStatus(){
        List<OrderStatus> ordersStatusList = orderStatusDao.findAllBy();
        return new ResponseEntity<List<OrderStatus>>(ordersStatusList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/orders/products_to_order/daterange")
    ResponseEntity<List<Order>> getProductsToOrder(
            @RequestParam(value="startDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(value="endDate", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate){
        List<Order> productToOrderList = orderDao.findProductToOrder(startDate,endDate);
        return new ResponseEntity<List<Order>>(productToOrderList, HttpStatus.OK);
    }


    @CrossOrigin
    @PostMapping("/orders")
    ResponseEntity<Order> createOrder(@RequestBody Order order )throws URISyntaxException {

        orderService.createOrder(order);

        return new ResponseEntity<Order>(order,HttpStatus.CREATED);
    }


//    @RequestMapping(value = "/upload", method = RequestMethod.POST)
//    public @ResponseBody ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
//
//                DbFile uploadedFile = new DbFile();
//                uploadedFile.setFileName(file.getOriginalFilename());
//                uploadedFile.setFileType(file.getContentType());
//                uploadedFile.setData(file.getBytes());
//
//                dbFileDao.save(uploadedFile);
//
//        return new ResponseEntity<>("ds",HttpStatus.CREATED);
//    }
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

    @Bean(name = "multipartResolver")     
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(112000000); //12MB
        return multipartResolver;
    }

}
