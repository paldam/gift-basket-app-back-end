package com.damian.boundry.rest;

import com.damian.domain.order.Order;
import com.damian.domain.order_file.DbFile;
import com.damian.dto.BasketExtStockDao;
import com.damian.domain.basket.Basket;
import com.damian.domain.basket.BasketExt;
import com.damian.domain.basket.BasketType;
import com.damian.domain.basket.BasketDao;
import com.damian.domain.basket.BasketTypeDao;
import com.damian.domain.basket.BasketExtService;
import com.damian.util.PdfBasketContents;
import com.damian.util.PdfGenerator;
import org.apache.log4j.Logger;
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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

@RestController
public class BasketController {

    private static final Logger logger = Logger.getLogger(BasketController.class);


    private BasketExtService basketExtService;
    private BasketDao basketDao;
   private BasketTypeDao basketTypeDao;

    public BasketController(BasketDao basketDao, BasketTypeDao basketTypeDao, BasketExtService basketExtService) {
        this.basketDao = basketDao;
        this.basketTypeDao = basketTypeDao;
        this.basketExtService = basketExtService;
    }



    @CrossOrigin
    @GetMapping("/baskets")
    ResponseEntity<List<Basket>> getBaskets(){
        List<Basket> basketList = basketDao.findAllWithoutDeleted();
        return new ResponseEntity<List<Basket>>(basketList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/basketswithdeleted")
    ResponseEntity<List<Basket>> getBasketsWithDeleted(){
        List<Basket> basketList = basketDao.findAllWithDeleted();
        return new ResponseEntity<List<Basket>>(basketList, HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/deletedbaskets/")
    ResponseEntity<List<Basket>> getDeletedBaskets(){
        List<Basket> basketList = basketDao.findAllDeleted();
        return new ResponseEntity<List<Basket>>(basketList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/basket/{id}")
    ResponseEntity<Basket> getBaskets(@PathVariable Long id){
        Optional<Basket> basket = basketDao.findById(id);
        return new ResponseEntity<Basket>(basket.get(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/baskets/types")
    ResponseEntity<List<BasketType>> getBasketsTypes(){
        List<BasketType> basketTypesList = basketTypeDao.findAllBy();
        return new ResponseEntity<List<BasketType>>(basketTypesList, HttpStatus.OK);
    }


    @CrossOrigin
    @PostMapping("/basket/add")
    ResponseEntity<Basket> addBasket(@RequestBody Basket basket)throws URISyntaxException {
        basketDao.save(basket);

        return new ResponseEntity<Basket>(basket,HttpStatus.CREATED);


    }


    @CrossOrigin
    @PostMapping("/basketswithoutimage")
    ResponseEntity<Basket> createBasket(@RequestBody Basket basket)throws URISyntaxException {

        Basket basketTmp = basketDao.findById(basket.getBasketId()).get();

        basket.setBasketImageData(basketTmp.getBasketImageData());


        basketDao.save(basket);

        return new ResponseEntity<Basket>(basket,HttpStatus.CREATED);


    }

    @CrossOrigin
    @PostMapping("/baskets")    // handle file and object in one request
    ResponseEntity<Basket> createBasket2(@RequestPart("basketimage") MultipartFile[] basketMultipartFiles,
                                         @RequestPart("basketobject") Basket basket) throws URISyntaxException {



        try{
            basket.setBasketImageData(basketMultipartFiles[0].getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        basket.setIsBasketImg(1);

        basketDao.save(basket);

        return new ResponseEntity<Basket>(basket,HttpStatus.CREATED);
    }


    @CrossOrigin
    @GetMapping("/basketimage/{basketId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long basketId) {



        byte[] basketFile = basketDao.getBasketImageByBasketId(basketId);

        Optional<byte[]> imgOpt = Optional.ofNullable(basketFile);
        if(!imgOpt.isPresent()){
            basketFile= new byte[0];
        }


        HttpHeaders header  = new HttpHeaders();
        header.setAccessControlExposeHeaders(Collections.singletonList("Content-Disposition"));;
        header.set("Content-Disposition", "attachment; filename=zdjecie kosza");


        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("image/jpeg"))
            .headers(header)
            .body(new ByteArrayResource(basketFile));
    }



    @CrossOrigin
    @RequestMapping(value = "/basket/pdf/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getOrderPdf(@PathVariable Long id) throws IOException {


        byte[] img = basketDao.getBasketImageByBasketId(id);


         Optional<byte[]> imgOpt = Optional.ofNullable(img);

        if(!imgOpt.isPresent()){
            img= new byte[0];
        }


        
        Optional<Basket> basketToGen = basketDao.findById(id);

        Basket basketToGenerate = new Basket();
        if(basketToGen.isPresent()) {
            basketToGenerate = basketToGen.get();
        }

        PdfGenerator pdfGenerator = new PdfGenerator();
        ByteArrayInputStream bis = PdfBasketContents.generateBasketProductsListPdf(basketToGenerate,img);

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
    @PostMapping("/basketext")
    ResponseEntity<BasketExt> createExternalBasket(@RequestBody BasketExt basketExt)throws URISyntaxException {


        System.out.println("22222" + basketExt.toString());

         basketExtService.saveExternalBasket(basketExt);

        return new ResponseEntity<BasketExt>(basketExt,HttpStatus.CREATED);

    }

    @CrossOrigin
    @PostMapping("/basketextstatus")
    ResponseEntity<BasketExt> externalBasketStatus(@RequestBody BasketExt basketExt)throws URISyntaxException {

        Basket basketToChange = new Basket(basketExt);
        Basket savedBasket= basketDao.save(basketToChange);

        return new ResponseEntity<BasketExt>(basketExt,HttpStatus.CREATED);

    }

    @CrossOrigin
    @GetMapping("/basketsextlist")
    ResponseEntity<List<BasketExt>> getBasketsExtList(){

        List<Basket> basketList = basketDao.findALLExportBasket();

        List<BasketExt> basketExtList = new ArrayList<>();

        basketList.forEach(basket -> {
            BasketExt basketTmp = new BasketExt(basket) ;
            basketTmp.setBasketTotalPrice(basketTmp.getBasketTotalPrice()) ;

            basketExtList.add(basketTmp) ;
        });


        return new ResponseEntity<List<BasketExt>>(basketExtList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/basket_ext_stock")
    ResponseEntity<List<BasketExtStockDao>> getBasketsExtStock(){

        List<Basket> basketList = basketDao.findALLExportBasket();
        List<BasketExtStockDao> basketExtList = new ArrayList<>();

       basketList.forEach(basket -> basketExtList.add(new BasketExtStockDao(basket)) );


        return new ResponseEntity<List<BasketExtStockDao>>(basketExtList, HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/extbaskets")
    ResponseEntity<List<Basket>> getBasketsForExternalPartner(){
        List<Basket> basketList = basketDao.findAllBasketForExternalPartner();
        return new ResponseEntity<List<Basket>>(basketList, HttpStatus.OK);
    }

}
