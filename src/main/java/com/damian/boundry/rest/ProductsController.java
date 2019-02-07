package com.damian.boundry.rest;



import com.damian.domain.basket.Basket;
import com.damian.domain.basket.BasketDao;
import com.damian.model.*;
import com.damian.repository.*;
import com.damian.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@RestController
public class ProductsController {

    private ProductDao productsDao;
    private ProductTypeDao productsTypeDao;
   private SupplierDao supplierDao;
    private  ProductService productService;
    private BasketDao basketDao;

    public ProductsController(ProductService productService, BasketDao basketDao, ProductTypeDao productsTypeDao, ProductDao productsDao, SupplierDao supplierDao) {
        this.productsTypeDao = productsTypeDao;
        this.productsDao = productsDao;
        this.supplierDao = supplierDao;
        this.productService = productService;
        this.basketDao = basketDao;
    }


    @CrossOrigin
    @GetMapping(value = "/products",produces = "application/json; charset=utf-8")
    ResponseEntity<List<Product>> listAllProducts(){

            List<Product> productsList = productsDao.findAllWithoutDeleted();
            return new ResponseEntity<List<Product>>(productsList, HttpStatus.OK);

    }

    @CrossOrigin
    @GetMapping(value = "/products/supplier",produces = "application/json; charset=utf-8")
    ResponseEntity<List<Supplier>> listAllSuppliers(){

        List<Supplier> supplierList = supplierDao.findAllByOrderBySupplierNameAsc();
        return new ResponseEntity<List<Supplier>>(supplierList, HttpStatus.OK);

    }

    @CrossOrigin
    @GetMapping(value = "/productsbysupplier/{id}",produces = "application/json; charset=utf-8")
    ResponseEntity<List<Product>> listAllSuppliers(@PathVariable Integer id){

        List<Product> productList = productsDao.findBySupplier_SupplierId(id);
        return new ResponseEntity<List<Product>>(productList, HttpStatus.OK);

    }

    @CrossOrigin
    @GetMapping(value = "/products/{id}",produces = "application/json; charset=utf-8")
    ResponseEntity<Product> getProductById(@PathVariable Integer id){



            Product product = productsDao.findById(id);
            return new ResponseEntity<Product>(product, HttpStatus.OK);

    }


    @CrossOrigin
    @GetMapping(value = "/baskets_by_product/{id}",produces = "application/json; charset=utf-8")
    ResponseEntity<List<Basket>> getBasketsByProductId(@PathVariable Integer id){

        List<Basket> basketsList = basketDao.BasketListByProduct(id);
        return new ResponseEntity<List<Basket>>(basketsList, HttpStatus.OK);

    }


    @CrossOrigin
    @PostMapping("/products/supplier/")
    ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier )throws URISyntaxException {
        supplierDao.save(supplier);

        return new ResponseEntity<Supplier>(supplier,HttpStatus.CREATED);


    }


    @CrossOrigin
    @PostMapping(value = "/product/stock",produces = "application/json; charset=utf-8")
    ResponseEntity changeProductsStock(@RequestParam Integer productId, Integer addValue) {

         productService.changeStockEndResetOfProductsToDelivery(productId,addValue);

        return new ResponseEntity<Supplier>(HttpStatus.OK);

    }

    @CrossOrigin
    @PostMapping(value = "/product/order",produces = "application/json; charset=utf-8")
    ResponseEntity addProductsToDelivery(@RequestParam Integer productId, Integer addValue) {

        productService.addNumberOfProductsDelivery(productId,addValue);

        return new ResponseEntity<Supplier>(HttpStatus.OK);

    }

//    @CrossOrigin
//    @PostMapping(value = "/product/order/reset",produces = "application/json; charset=utf-8")
//    ResponseEntity resetProductsToDelivery(@RequestParam Integer productId) {
//
//        productsDao.resetProductToDeliver(productId);
//
//        return new ResponseEntity<Supplier>(HttpStatus.OK);
//
//    }

    @CrossOrigin
    @DeleteMapping(value = "/products/{id}")
    ResponseEntity deleteProduct(@PathVariable Integer id){

        Product selectedProduct = productsDao.findById(id);

        if (Objects.isNull(selectedProduct)) {
            return new ResponseEntity("Nie znaleziono Produktu o id " + id, HttpStatus.NOT_FOUND);
        }else{
            productsDao.deleteById(id);
            return new ResponseEntity(id, HttpStatus.OK);
        }
    }



    @CrossOrigin
    @PostMapping("/products")
    ResponseEntity<Product> createProduct(@RequestBody Product products )throws URISyntaxException {

        products.setTmpOrdered(0);
        productsDao.save(products);

        return new ResponseEntity<Product>(products,HttpStatus.CREATED);


    }



}
