package com.damian.controller;



import com.damian.model.BasketType;
import com.damian.model.Product;
import com.damian.model.ProductType;
import com.damian.repository.BasketTypeDao;
import com.damian.repository.ProductTypeDao;
import com.damian.repository.ProductDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
public class ProductsController {

    ProductDao productsDao;
    ProductTypeDao productsTypeDao;


    public ProductsController(ProductTypeDao productsTypeDao, ProductDao productsDao) {
        this.productsTypeDao = productsTypeDao;
        this.productsDao = productsDao;
    }


    @CrossOrigin
    @GetMapping(value = "/products")
    ResponseEntity<List<Product>> listAllProducts(){

        List<Product> productsList = productsDao.findAllByOrderByIdDesc();
        return new ResponseEntity<List<Product>>(productsList, HttpStatus.OK);

    }


    @CrossOrigin
    @GetMapping(value = "/products/{id}",produces = "application/json; charset=utf-8")
    ResponseEntity<Product> getProductById(@PathVariable Integer id){

        Product product = productsDao.findById(id);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

//    @CrossOrigin
//    @GetMapping("/products/types")
//    ResponseEntity<List<ProductType>> listAllProductsTypes(){
//
//        List<ProductType> productsTypeList = productsTypeDao.findAll();
//        return new ResponseEntity<List<ProductType>>(productsTypeList, HttpStatus.OK);
//    }

    @CrossOrigin
    @PostMapping("/products")
    ResponseEntity<Product> createProduct(@RequestBody Product products )throws URISyntaxException {
        productsDao.save(products);

        return new ResponseEntity<Product>(products,HttpStatus.CREATED);


    }



}
