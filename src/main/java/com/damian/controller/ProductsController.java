package com.damian.controller;



import com.damian.model.Products;
import com.damian.model.ProductsTypes;
import com.damian.model.Test;
import com.damian.repository.ProductTypeDao;
import com.damian.repository.ProductsDao;
import com.damian.repository.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class ProductsController {

    ProductsDao productsDao;
    ProductTypeDao productsTypeDao;

    public ProductsController(ProductTypeDao productsTypeDao, ProductsDao productsDao) {
        this.productsTypeDao = productsTypeDao;
        this.productsDao = productsDao;
    }


    @CrossOrigin
    @GetMapping("/products")
    ResponseEntity<List<Products>> listAllProducts(){

        List<Products> productsList = productsDao.findAllByOrderByIdDesc();
        return new ResponseEntity<List<Products>>(productsList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/products/{id}")
    ResponseEntity<Products> getProductById(@PathVariable Integer id){

        Products product = productsDao.findById(id);
        return new ResponseEntity<Products>(product, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/products/types")
    ResponseEntity<List<ProductsTypes>> listAllProductsTypes(){

        List<ProductsTypes> productsTypeList = productsTypeDao.findAll();
        return new ResponseEntity<List<ProductsTypes>>(productsTypeList, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/products")
    ResponseEntity<Products> createProduct(@RequestBody Products products )throws URISyntaxException {
        productsDao.save(products);

        return new ResponseEntity<Products>(products,HttpStatus.CREATED);


    }



}
