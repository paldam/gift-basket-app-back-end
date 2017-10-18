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
import java.util.Objects;

@RestController
public class ProductsController {

    ProductDao productsDao;
    ProductTypeDao productsTypeDao;


    public ProductsController(ProductTypeDao productsTypeDao, ProductDao productsDao) {
        this.productsTypeDao = productsTypeDao;
        this.productsDao = productsDao;
    }


    @CrossOrigin
    @GetMapping(value = "/products",produces = "application/json; charset=utf-8")
    ResponseEntity<List<Product>> listAllProducts(){

        List<Product> productsList = productsDao.findAllWithoutDeleted();
        return new ResponseEntity<List<Product>>(productsList, HttpStatus.OK);

    }


    @CrossOrigin
    @GetMapping(value = "/products/{id}",produces = "application/json; charset=utf-8")
    ResponseEntity<Product> getProductById(@PathVariable Integer id){

        Product product = productsDao.findById(id);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

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
        productsDao.save(products);

        return new ResponseEntity<Product>(products,HttpStatus.CREATED);


    }



}
