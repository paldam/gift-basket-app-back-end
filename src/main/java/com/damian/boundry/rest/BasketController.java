package com.damian.boundry.rest;

import com.damian.domain.basket.*;
import com.damian.domain.order.OrderItem;
import com.damian.dto.BasketDto;
import com.damian.dto.BasketExtStockDao;
import com.damian.util.FtpConnectionException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

@CrossOrigin
@RestController
public class BasketController {

    private BasketExtService basketExtService;
    private BasketService basketService;
    private BasketDao basketDao;
    private BasketTypeDao basketTypeDao;
    private BasketSezonDao basketSezonDao;
    private BasketImageDao basketImageDao;

    public BasketController(BasketImageDao basketImageDao ,BasketSezonDao basketSezonDao, BasketService basketService, BasketDao basketDao, BasketTypeDao basketTypeDao, BasketExtService basketExtService) {
        this.basketDao = basketDao;
        this.basketService = basketService;
        this.basketTypeDao = basketTypeDao;
        this.basketExtService = basketExtService;
        this.basketSezonDao = basketSezonDao;
        this.basketImageDao = basketImageDao;
    }
    
    @GetMapping("/basket/{id}")
    ResponseEntity<Basket> getBaskets(@PathVariable Long id) {
        return basketDao.findById(id)
            .map(basket -> ResponseEntity.ok().body(basket))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @Transactional
//    @GetMapping("/reformat")
//    ResponseEntity<List<Basket>> getAll() {
//        List<Basket> basketList = basketDao.findAllBy();
//        basketList.forEach(basket -> {
//            if (basket.getBasketImageData() != null) {
//                BasketImage newBasket = new BasketImage(basket.getBasketImageData());
//                BasketImage savedBasket = basketImageDao.save(newBasket);
//                basket.setBasketImage(savedBasket);
//            } else basket.setBasketImage(null);
//        });
//        return new ResponseEntity<>( HttpStatus.OK);
//    }


    @PostMapping("/basket/add")
    ResponseEntity<Basket> createBasket(@RequestBody Basket basket) {
        Basket savedBasket = basketService.addBasket(basket);
        return new ResponseEntity<>(savedBasket, HttpStatus.CREATED);
    }

    @PostMapping("/baskets_seasons/add")
    ResponseEntity<BasketSezon> createBasketSeason(@RequestBody BasketSezon basketSezon) {

        Optional<BasketSezon> optBasketSezon = basketSezonDao.findByBasketSezonName(basketSezon.getBasketSezonName());

       if(basketSezon.getBasketSezonId() == null && optBasketSezon.isPresent()) {
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }else{

           if(basketSezon.getIsActive() == null) {
               basketSezon.setIsActive(true);
           }
           BasketSezon savedBasketSezon = basketSezonDao.save(basketSezon);
           return new ResponseEntity<>(savedBasketSezon, HttpStatus.CREATED);
       }



    }

    @PostMapping("/baskets")
    ResponseEntity<Basket> createBasketWithImg(@RequestPart("basketimage") MultipartFile[] basketMultipartFiles, @RequestPart("basketobject") Basket basket) {
        Basket savedBasket = basketService.addBasketWithImg(basket, basketMultipartFiles);
        return new ResponseEntity<>(savedBasket, HttpStatus.CREATED);
    }

    @PostMapping("/basketext")
    ResponseEntity<BasketExt> createExternalBasket(@RequestBody BasketExt basketExt) {

        try{
            basketExtService.saveExternalBasket(basketExt);
        }catch (FtpConnectionException ftpE){
            return new ResponseEntity<>(basketExt, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(basketExt, HttpStatus.CREATED);
    }

    @PostMapping("/basketswithoutimage")
    ResponseEntity<Basket> editBasketWithoutImage(@RequestBody Basket basket) {
        Basket savedBasket = basketService.editBasketWithoutImage(basket);
        return new ResponseEntity<>(savedBasket, HttpStatus.CREATED);
    }

    @GetMapping("/baskets")
    ResponseEntity<List<Basket>> getAllBasketsWithoutDeleted() {
        List<Basket> basketList = basketDao.findAllWithoutDeleted();
        return new ResponseEntity<>(basketList, HttpStatus.OK);
    }

    @GetMapping("/baskets_seasons")
    ResponseEntity<List<BasketSezon>> getBasketsSeasons() {
        List<BasketSezon> basketList = basketSezonDao.findByIsActiveTrue();
        return new ResponseEntity<>(basketList, HttpStatus.OK);
    }

    @GetMapping("/basketpage")
    ResponseEntity<BasketPageRequest> getBasketsPage(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size") int size, @RequestParam(value = "searchtext", required = false) String text, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(value = "sortingDirection", required = false, defaultValue = "1") int sortingDirection, @RequestParam(value = "onlyArchival", required = false) boolean onlyArchival, @RequestParam(value = "basketSeasonFilter", required = false) List<Integer> basketSeasonFilter) {
        BasketPageRequest basketsPage = basketService.getBasketsPage(page, size, text, orderBy, sortingDirection,
            onlyArchival, basketSeasonFilter);
        return new ResponseEntity<>(basketsPage, HttpStatus.OK);
    }

    @GetMapping("/basketsdto")
    ResponseEntity<List<BasketDto>> getBasketsDto() {
        List<BasketDto> basketList = basketDao.findBasketDto();
        return new ResponseEntity<>(basketList, HttpStatus.OK);
    }

    @GetMapping("/basketswithdeleted")
    ResponseEntity<List<Basket>> getBasketsWithDeleted() {
        List<Basket> basketList = basketDao.findAllWithDeleted();
        return new ResponseEntity<>(basketList, HttpStatus.OK);
    }

    @GetMapping("/deletedbaskets/")
    ResponseEntity<List<Basket>> getDeletedBaskets() {
        List<Basket> basketList = basketDao.findAllDeleted();
        return new ResponseEntity<>(basketList, HttpStatus.OK);
    }

    @GetMapping("/baskets/types")
    ResponseEntity<List<BasketType>> getBasketsTypes() {
        List<BasketType> basketTypesList = basketTypeDao.findAllBy();
        return new ResponseEntity<>(basketTypesList, HttpStatus.OK);
    }

    @RequestMapping(value = {"/basket/find/{priceMin}/{priceMax}/{basketPrice}/{productsSubTypes}", "/basket/find/{priceMin}/{priceMax}/{basketPrice}"}, method = RequestMethod.GET)
    public ResponseEntity<List<Basket>> getBasketsWithFilters(@PathVariable Integer priceMin, @PathVariable Integer priceMax, @PathVariable Boolean basketPrice, @PathVariable Optional<List<Integer>> productsSubTypes) {
        List<Basket> filteredBasketList = basketService.getBasketFilterMod(priceMin, priceMax, basketPrice, productsSubTypes);
        return new ResponseEntity<>(filteredBasketList, HttpStatus.OK);
    }

    @GetMapping("/basketimage/{basketId}")
    public ResponseEntity<Resource> getBasketImage(@PathVariable Long basketId) {
        byte[] basketImage = basketService.prepareBasketImage(basketId);
        HttpHeaders header = new HttpHeaders();
        header.setAccessControlExposeHeaders(Collections.singletonList("Content-Disposition"));
        header.set("Content-Disposition", "attachment; filename=basket image");
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("image/jpeg"))
            .headers(header)
            .body(new ByteArrayResource(basketImage));
    }

    @RequestMapping(value = "/basket/pdf/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getBasketProductsListPdf(@PathVariable Long id) throws IOException {
        ByteArrayInputStream pdfFile = basketService.prepareBasketProductsListPdf(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");
        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(pdfFile));
    }

    @RequestMapping(value = "/basket/pdf/catalogname/{id}", method = RequestMethod.GET, produces =
        MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getBasketProductsListPdfCatalogNameVersion(@PathVariable Long id) throws IOException {
        ByteArrayInputStream pdfFile = basketService.prepareBasketProductsListCatalogNameVersionPdf(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order.pdf");
        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(pdfFile));
    }

    @PostMapping("/basketextedit")
    ResponseEntity<BasketExt> rditExternalBasket(@RequestBody BasketExt basketExt) {
        basketExtService.editExternalBasket(basketExt);
        return new ResponseEntity<>(basketExt, HttpStatus.CREATED);
    }

    @PostMapping("/basketextstatus")
    ResponseEntity<BasketExt> externalBasketStatus(@RequestBody BasketExt basketExt) {
        Basket basketToChange = new Basket(basketExt);
        basketDao.save(basketToChange);
        return new ResponseEntity<>(basketExt, HttpStatus.CREATED);
    }

    @GetMapping(value = "/basketsextlist",produces = "application/json; charset=utf-8")
    ResponseEntity<List<BasketExt>> getBasketsExtList() {
        List<Basket> basketList = basketDao.findALLExportBasket();
        List<BasketExt> basketExtList = new ArrayList<>();
        basketList.forEach(basket -> {
            BasketExt basketTmp = new BasketExt(basket);
            basketTmp.setBasketTotalPrice(basketTmp.getBasketTotalPrice());
            basketExtList.add(basketTmp);
        });
        return new ResponseEntity<>(basketExtList, HttpStatus.OK);
    }

    @GetMapping("/basket_ext_stock")
    ResponseEntity<List<BasketExtStockDao>> getBasketsExtStock() {
        List<Basket> basketList = basketDao.findALLExportBasket();
        List<BasketExtStockDao> basketExtList = new ArrayList<>();
        basketList.forEach(basket -> basketExtList.add(new BasketExtStockDao(basket)));
        return new ResponseEntity<>(basketExtList, HttpStatus.OK);
    }

    @PostMapping(value = "/baskets/stockadd", produces = "application/json; charset=utf-8")
    ResponseEntity<List<OrderItem>> addBasketsToStock(@RequestBody List<OrderItem> orderItems) {
        if (orderItems.isEmpty()) {
            return new ResponseEntity<>(orderItems, HttpStatus.BAD_REQUEST);
        }
        basketService.addBasketToStock(orderItems);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @GetMapping(value = "/baskets/stockadd/{basketId}/{newValue}", produces = "application/json; charset=utf-8")
    ResponseEntity<Long> addNewBasketsStateOfStock(@PathVariable Long basketId, @PathVariable Integer newValue) {
        basketDao.saveNewStockOfBasket(basketId, newValue);
        return new ResponseEntity<>(basketId, HttpStatus.OK);
    }

    @GetMapping(value = "/extbaskets",produces = "application/json; charset=utf-8" )
    ResponseEntity<List<Basket>> getBasketsForExternalPartner() {
        List<Basket> basketList = basketDao.findAllBasketForExternalPartner();
        return new ResponseEntity<>(basketList, HttpStatus.OK);
    }
}
