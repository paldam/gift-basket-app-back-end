package com.damian.boundry.rest;

import com.damian.domain.basket.Basket;
import com.damian.domain.basket.BasketDao;
import com.damian.domain.product.*;
import com.damian.security.UserPermissionDeniedException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

@RestController
public class ProductsController {

    private ProductDao productsDao;
    private ProductTypeDao productsTypeDao;
    private SupplierDao supplierDao;
    private ProductService productService;
    private BasketDao basketDao;
    private ProductSubTypeDao productSubTypeDao;

    public ProductsController(ProductSubTypeDao productSubTypeDao, ProductService productService, BasketDao basketDao, ProductTypeDao productsTypeDao, ProductDao productsDao, SupplierDao supplierDao) {
        this.productsTypeDao = productsTypeDao;
        this.productsDao = productsDao;
        this.supplierDao = supplierDao;
        this.productService = productService;
        this.basketDao = basketDao;
        this.productSubTypeDao = productSubTypeDao;
    }

    @CrossOrigin
    @GetMapping(value = "/products", produces = "application/json; charset=utf-8")
    ResponseEntity<List<Product>> listAllProducts() {
        List<Product> productsList = productsDao.findAllWithoutDeleted();
        return new ResponseEntity<List<Product>>(productsList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/products/supplier", produces = "application/json; charset=utf-8")
    ResponseEntity<List<Supplier>> listAllSuppliers() {
        List<Supplier> supplierList = supplierDao.findAllByOrderBySupplierNameAsc();
        return new ResponseEntity<List<Supplier>>(supplierList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/products/types", produces = "application/json; charset=utf-8")
    ResponseEntity<List<ProductType>> listAllProductTypes() {
        List<ProductType> typeList = productsTypeDao.findAll();
        return new ResponseEntity<List<ProductType>>(typeList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/products/sub_types", produces = "application/json; charset=utf-8")
    ResponseEntity<List<ProductSubType>> listAllProductSubTypes() {
        List<ProductSubType> typeList = productSubTypeDao.findAll();
        return new ResponseEntity<List<ProductSubType>>(typeList, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/products/subtypes")
    ResponseEntity<ProductSubType> createProductsSubTypes(@RequestBody ProductSubType productSubType) throws URISyntaxException {
        productSubTypeDao.save(productSubType);
        return new ResponseEntity<ProductSubType>(productSubType, HttpStatus.CREATED);
    }

    @CrossOrigin
    @PostMapping("/products/types")
    ResponseEntity<ProductType> createProductsTypes(@RequestBody ProductType productType) throws URISyntaxException {
        productsTypeDao.save(productType);
        return new ResponseEntity<ProductType>(productType, HttpStatus.CREATED);
    }

    @CrossOrigin
    @DeleteMapping(value = "/products/types/{id}")
    ResponseEntity deleteProductsTypes(@PathVariable Integer id) {
        ProductType productType = productsTypeDao.findByTypeId(id);
        if (Objects.isNull(productType)) {
            return new ResponseEntity("Nie znaleziono typu produktu o id " + id, HttpStatus.NOT_FOUND);
        } else {
            productsTypeDao.delete(productType);
            return new ResponseEntity(id, HttpStatus.OK);
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/products/subtypes/{id}")
    ResponseEntity deleteProductsSubTypes(@PathVariable Integer id) {
        ProductSubType productSubType = productSubTypeDao.findBySubTypeId(id);
        if (Objects.isNull(productSubType)) {
            return new ResponseEntity("Nie znaleziono typu produktu o id " + id, HttpStatus.NOT_FOUND);
        } else {
            productSubTypeDao.delete(productSubType);
            return new ResponseEntity(id, HttpStatus.OK);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/productsbysupplier/{id}", produces = "application/json; charset=utf-8")
    ResponseEntity<List<Product>> listAllSuppliers(@PathVariable Integer id) {
        List<Product> productList = productsDao.findBySupplier_SupplierId(id);
        return new ResponseEntity<List<Product>>(productList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/products/{id}", produces = "application/json; charset=utf-8")
    ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productsDao.findById(id);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/baskets_by_product/{id}", produces = "application/json; charset=utf-8")
    ResponseEntity<List<Basket>> getBasketsByProductId(@PathVariable Integer id) {
        List<Basket> basketsList = basketDao.BasketListByProduct(id);
        return new ResponseEntity<List<Basket>>(basketsList, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/products/supplier/")
    ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) throws URISyntaxException {
        supplierDao.save(supplier);
        return new ResponseEntity<Supplier>(supplier, HttpStatus.CREATED);
    }

    @CrossOrigin
    @DeleteMapping(value = "/products/supplier/{id}")
    ResponseEntity deleteSupplier(@PathVariable Integer id) {
        Supplier selectedSupplier = supplierDao.findBySupplierId(id);
        if (Objects.isNull(selectedSupplier)) {
            return new ResponseEntity("Nie znaleziono Dostawcy o id " + id, HttpStatus.NOT_FOUND);
        } else {
            supplierDao.delete(selectedSupplier);
            return new ResponseEntity(id, HttpStatus.OK);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/product/stock", produces = "application/json; charset=utf-8")
    ResponseEntity changeProductsStock(@RequestParam Integer productId, Integer addValue) {
        productService.changeStockEndResetOfProductsToDelivery(productId, addValue);
        return new ResponseEntity<Supplier>(HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/product/order", produces = "application/json; charset=utf-8")
    ResponseEntity addProductsToDelivery(@RequestParam Integer productId, Integer addValue) {
        productService.addNumberOfProductsDelivery(productId, addValue);
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
    ResponseEntity deleteProduct(@PathVariable Integer id) {
        Product selectedProduct = productsDao.findById(id);
        if (Objects.isNull(selectedProduct)) {
            return new ResponseEntity("Nie znaleziono Produktu o id " + id, HttpStatus.NOT_FOUND);
        } else {
            productsDao.deleteById(id);
            return new ResponseEntity(id, HttpStatus.OK);
        }
    }

    @CrossOrigin
    @PostMapping("/products")
    ResponseEntity<Product> createProduct(@RequestBody Product products) throws URISyntaxException {
        System.out.println(ANSI_YELLOW + products.toString() + ANSI_RESET);
        if (products.getTmpOrdered() == null) {
            products.setTmpOrdered(0);
        }
        productsDao.save(products);
        return new ResponseEntity<Product>(products, HttpStatus.CREATED);
    }

    @CrossOrigin
    @PostMapping("/products/resetstates")
    ResponseEntity<Product> resetStates() {
        try {
            productService.resetProductsState();
        } catch (UserPermissionDeniedException ex) {
            return new ResponseEntity("Nie masz uprawnień do wykoania tej operacji", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
