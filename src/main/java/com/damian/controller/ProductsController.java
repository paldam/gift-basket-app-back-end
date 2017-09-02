package com.damian.controller;


import com.damian.repository.ProductsDao;
import com.damian.model.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductsController {

    @Autowired
    ProductsDao productsDao;


    @GetMapping("/a")
    ResponseEntity<List<Products>> listAllUser(){

        List<Products> lista = productsDao.findAll();
        return new ResponseEntity<List<Products>>(lista, HttpStatus.OK);
    }

    @GetMapping("/b")
    ResponseEntity<List<Products>> listAllUser2(){

        List<Products> lista = productsDao.findAll();
        return new ResponseEntity<List<Products>>(lista, HttpStatus.OK);
    }

    @GetMapping("/c")
    ResponseEntity<List<Products>> listAllUser3(){

        List<Products> lista = productsDao.findAll();
        return new ResponseEntity<List<Products>>(lista, HttpStatus.OK);
    }


}
