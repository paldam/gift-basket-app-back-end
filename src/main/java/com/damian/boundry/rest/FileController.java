package com.damian.boundry.rest;

import com.damian.domain.order_file.*;
import com.damian.dto.FileDto;
import com.damian.dto.ProductToCollectOrder;
import com.damian.domain.order.Order;
import com.damian.domain.order.OrderItem;
import com.damian.domain.order.OrderDao;
import com.damian.domain.order.OrderService;
import com.damian.util.*;
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
import java.util.*;

@RestController
@CrossOrigin
public class FileController {

    private DbFileDao dbFileDao;
    private DbFileService dbFileService;
    private OrderDao orderDao;
    private OrderService orderService;
    private FileService fileService;

    public FileController(DbFileDao dbFileDao, FileService fileService ,DbFileService dbFileService, OrderDao orderDao, OrderService orderService) {
        this.dbFileDao= dbFileDao;
        this.dbFileService = dbFileService;
        this.orderDao= orderDao;
        this.orderService = orderService;
        this.fileService =fileService;
    }

    @PostMapping("/uploadfiles")
    public ResponseEntity uploadMultipleOrderFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("orderId") Long orderId) {
        dbFileService.uploadFiles(files, orderId);
        return new ResponseEntity<>("ds", HttpStatus.CREATED);
    }


    @GetMapping(value = "/orderfile/{id}")
    public ResponseEntity<List<DbFile>> getFiles(@PathVariable Long id){
        List<DbFile> fileList = dbFileDao.findByOrderId(id) ;
        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }


    @GetMapping(value = "/orderfiledto")
    public ResponseEntity<List<FileDto>> getFilesDto(){
        List<FileDto> fileDtoList = dbFileService.getFileDto() ;
        return new ResponseEntity<>(fileDtoList, HttpStatus.OK);
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<Resource> downloadOrderFile(@PathVariable Long fileId) {
        Optional<DbFile> dbFile = dbFileDao.findByFileId(fileId);
        if (dbFile.isPresent()) {
            HttpHeaders header = new HttpHeaders();
            header.setAccessControlExposeHeaders(Collections.singletonList("Content-Disposition"));
            header.set("Content-Disposition", "attachment; filename=" + dbFile.get().getFileName());
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(dbFile.get().getFileType())).headers(header).body(new ByteArrayResource(dbFile.get().getData()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/file/delete/{fileId}")
    public ResponseEntity deleteFile(@PathVariable Long fileId) {
        return dbFileDao.findByFileId(fileId)
            .map(dbFile -> ResponseEntity.ok().body("Usunięto plik"))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/order/pdf/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getOrderSummaryPdf(@PathVariable Long id) throws IOException {
        ByteArrayInputStream pdf = fileService.preparePdfFile(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");
        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(pdf));
    }

    @RequestMapping(value = "/order/pdf/alltoday", method = RequestMethod.GET)
    public ResponseEntity getAllTodayOrderPdf() throws IOException {
        Optional<ByteArrayInputStream> todayPdf = fileService.prepareAllTodayPdfFile();
        if (todayPdf.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=order.pdf");
            return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(todayPdf.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body("Brak zamówień z dnia dzisiejszego");
        }
    }

    @RequestMapping(value = "/order/pdf/product_to_collect/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getProductsToCollectOrder(@PathVariable Long id) throws IOException {
        List<ProductToCollectOrder> productToCollectOrderTmp = orderDao.findProductToCollectOrder(id);
        Order order = orderDao.findByOrderId(id);
        ByteArrayInputStream pdf = PdfProductToCollectOrder.generateProductToCollectOrderPdf(productToCollectOrderTmp, id, order);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");
        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(pdf));
    }

    @RequestMapping(value = "/order/multipdf", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getMultiPdf(@RequestParam("ordersIdList") List<Long> ordersToPrintList) throws IOException {
        orderService.getOrderListFromIdList(ordersToPrintList);
        ByteArrayInputStream pdf = PdfGenerator.generatePdf(orderService.getOrderListFromIdList(ordersToPrintList),PdfType.ORDER_SUMMARY);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");
        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(pdf));
    }

    @RequestMapping(value = "/order/multideliverypdf", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getMultiDeliveryPdf(@RequestParam("ordersIdList") List<Long> ordersToPrintList) throws IOException {
        orderService.getOrderListFromIdList(ordersToPrintList);
        ByteArrayInputStream pdf = PdfGenerator.generatePdf(orderService.getOrderListFromIdList(ordersToPrintList),PdfType.CONFIRMATION_OF_DELIVERY);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");
        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(pdf));
    }

    @RequestMapping(value = "/order/deliverypdf/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getDeliveryPdf(@PathVariable Long id) throws IOException {
        Order orderToGenerate = orderDao.findByOrderId(id);
        ByteArrayInputStream pdf = PdfGenerator.generatePdf(Collections.singletonList(orderToGenerate),PdfType.CONFIRMATION_OF_DELIVERY);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");
        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(pdf));
    }

    @RequestMapping(value = "/order/deliverypdfwithmodyfication/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getDeliveryPdfWithModifications(@PathVariable Long id, @RequestBody List<OrderItem> orderItems) throws IOException {
        Order orderToGenerate = orderDao.findByOrderId(id);
        orderToGenerate.setOrderItems(orderItems);

        ByteArrayInputStream pdf = PdfGenerator.generatePdf(Collections.singletonList(orderToGenerate),PdfType.CONFIRMATION_OF_DELIVERY);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");
        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(pdf));
    }
    
}
