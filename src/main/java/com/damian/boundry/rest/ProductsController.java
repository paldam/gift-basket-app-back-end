package com.damian.boundry.rest;

import com.damian.domain.basket.Basket;
import com.damian.domain.basket.BasketDao;
import com.damian.domain.product.*;
import com.damian.security.UserPermissionDeniedException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(produces = "application/json; charset=utf-8")
public class ProductsController {

    private ProductDao productsDao;
    private ProductTypeDao productsTypeDao;
    private SupplierDao supplierDao;
    private ProductService productService;
    private BasketDao basketDao;
    private ProductSubTypeDao productSubTypeDao;
    private ProductSeasonDao productSeasonDao;

    public ProductsController(ProductSeasonDao productSeasonDao,ProductSubTypeDao productSubTypeDao,
                              ProductService productService, BasketDao basketDao,
                              ProductTypeDao productsTypeDao, ProductDao productsDao, SupplierDao supplierDao) {
        this.productSeasonDao = productSeasonDao;
        this.productsTypeDao = productsTypeDao;
        this.productsDao = productsDao;
        this.supplierDao = supplierDao;
        this.productService = productService;
        this.basketDao = basketDao;
        this.productSubTypeDao = productSubTypeDao;
    }

    @DeleteMapping(value = "/products/supplier/{id}")
    ResponseEntity deleteSupplier(@PathVariable Integer id) {
        return supplierDao.findBySupplierId(id)
            .map(supplier -> {
                supplierDao.delete(supplier);
                return ResponseEntity.ok().body(id);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/products", produces = "application/json; charset=utf-8")
    ResponseEntity<List<Product>> listAllProducts() {
        List<Product> productsList = productsDao.findAllWithoutDeleted();
        return new ResponseEntity<>(productsList, HttpStatus.OK);
    }


    @GetMapping("/productspage")
    ResponseEntity<ProductPageRequest> getProductsPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size") int size,
                                                       @RequestParam(value = "searchtext", required = false) String text,
                                                       @RequestParam(value = "orderBy", required = false) String orderBy,
                                                       @RequestParam(value = "sortingDirection", required = false, defaultValue = "1") int sortingDirection,
                                                       @RequestParam(value = "productSubTypeFilter", required = false) List<Integer> productSubTypeFilter,
                                                       @RequestParam(value = "productSuppliersFilter", required = false) List<Integer> productSuppliersFilter) {

        ProductPageRequest productPage = productService.getProductsPage(page, size, text, orderBy, sortingDirection
            , productSubTypeFilter,productSuppliersFilter);
        return new ResponseEntity<>(productPage, HttpStatus.OK);
    }

    @GetMapping(value = "/products/supplier", produces = "application/json; charset=utf-8")
    ResponseEntity<List<Supplier>> listAllSuppliers() {
        List<Supplier> supplierList = supplierDao.findAllByOrderBySupplierNameAsc();
        return new ResponseEntity<>(supplierList, HttpStatus.OK);
    }

    @GetMapping(value = "/products/types", produces = "application/json; charset=utf-8")
    ResponseEntity<List<ProductType>> listAllProductTypes() {
        List<ProductType> typeList = productsTypeDao.findAll(Sort.by(Sort.Direction.ASC, "typeName"));
        return new ResponseEntity<>(typeList, HttpStatus.OK);
    }

    @GetMapping(value = "/products/sub_types", produces = "application/json; charset=utf-8")
    ResponseEntity<List<ProductSubType>> listAllProductSubTypes() {
        List<ProductSubType> typeList = productSubTypeDao.findAll(Sort.by(Sort.Direction.ASC, "subTypeName"));
        return new ResponseEntity<>(typeList, HttpStatus.OK);
    }

    @GetMapping(value = "/products/seasons", produces = "application/json; charset=utf-8")
    ResponseEntity<List<ProductSeason>> listProductSeasons() {
        List<ProductSeason> seasonList = productSeasonDao.findByIsActiveTrue();
        return new ResponseEntity<>(seasonList, HttpStatus.OK);
    }

    @PostMapping("/products/seasons")
    ResponseEntity<ProductSeason> createProductsSeasons(@RequestBody ProductSeason productSeason) {

        if(productSeason.getIsActive() == null) {
            productSeason.setIsActive(true);
        }

        productSeasonDao.save(productSeason);
        return new ResponseEntity<>(productSeason, HttpStatus.CREATED);
    }


    @PostMapping("/products/subtypes")
    ResponseEntity<ProductSubType> createProductsSubTypes(@RequestBody ProductSubType productSubType) {
        productSubTypeDao.save(productSubType);
        return new ResponseEntity<>(productSubType, HttpStatus.CREATED);
    }

    @PostMapping("/products/types")
    ResponseEntity<ProductType> createProductsTypes(@RequestBody ProductType productType) {
        productsTypeDao.save(productType);
        return new ResponseEntity<>(productType, HttpStatus.CREATED);
    }

    @GetMapping("/products/types/inactive")
    ResponseEntity<List<ProductType>> getInactiveProductsTypes() {
        List<ProductType> productType = productsTypeDao.findInactive();
        return new ResponseEntity<>(productType, HttpStatus.CREATED);
    }


    @GetMapping("/products/types/inactive/{id}")
    ResponseEntity<?> setInactiveProductsTypes(@PathVariable Integer id) {
        productsTypeDao.setInactive(id);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }


    @GetMapping("/products/types/active/{id}")
    ResponseEntity<?> setActiveProductsTypes(@PathVariable Integer id) {
        productsTypeDao.setActive(id);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/products/types/{id}")
    ResponseEntity<?> deleteProductsTypes(@PathVariable Integer id) {
        return  productsTypeDao.findByTypeId(id)
            .map(productType -> {
                productsTypeDao.delete(productType);
               return ResponseEntity.ok().body(id);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/products/subtypes/{id}")
    ResponseEntity<?> deleteProductsSubTypes(@PathVariable Integer id) {
        return productSubTypeDao.findBySubTypeId(id)
            .map(productSubType -> {
                 productSubTypeDao.delete(productSubType);
                return ResponseEntity.ok().body(id);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/productsbysupplier/{id}", produces = "application/json; charset=utf-8")
    ResponseEntity<List<Product>> listAllSuppliers(@PathVariable Integer id) {
        List<Product> productList = productsDao.findBySupplier_SupplierId(id);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping(value = "/products/{id}", produces = "application/json; charset=utf-8")
    ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productsDao.findById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping(value = "/baskets_by_product/{id}", produces = "application/json; charset=utf-8")
    ResponseEntity<List<Basket>> getBasketsByProductId(@PathVariable Integer id) {
        List<Basket> basketsList = basketDao.BasketListByProduct(id);
        return new ResponseEntity<>(basketsList, HttpStatus.OK);
    }

    @GetMapping(value = "/products/multidelivery/{ids}/{values}", produces = "application/json; charset=utf-8")
    ResponseEntity<List<Basket>> setMultiDelivery(@PathVariable Integer[] ids, @PathVariable Integer[] values) {
        productService.setMultiDelivery(ids, values);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/products/supplier/")
    ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) {
        supplierDao.save(supplier);
        return new ResponseEntity<>(supplier, HttpStatus.CREATED);
    }

    @PostMapping(value = "/product/stock", produces = "application/json; charset=utf-8")
    ResponseEntity changeProductsStock(@RequestParam Integer productId, Integer addValue) {
        productService.changeStockEndResetOfProductsToDelivery(productId, addValue);
        return new ResponseEntity<Supplier>(HttpStatus.OK);
    }

    @PostMapping(value = "/product/order", produces = "application/json; charset=utf-8")
    ResponseEntity addProductsToDelivery(@RequestParam Integer productId, Integer addValue) {
        productService.addNumberOfProductsDelivery(productId, addValue);
        return new ResponseEntity<Supplier>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/products/{id}")
    ResponseEntity deleteProduct(@PathVariable Integer id) {
        Product selectedProduct = productsDao.findById(id);

        if (Optional.ofNullable(selectedProduct).isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            productsDao.deleteById(id);
            return ResponseEntity.ok(id);
        }
    }

    @PostMapping("/products")
    ResponseEntity<Product> createProduct(@RequestBody Product products) {
        if (products.getTmpOrdered() == null) {
            products.setTmpOrdered(0);
        }
        productsDao.save(products);
        return new ResponseEntity<>(products, HttpStatus.CREATED);
    }

    @GetMapping("/productimage/{productId}")
    public ResponseEntity<Resource> getProductImage(@PathVariable Long productId) {
        byte[] productImage = productService.prepareProductImage(productId);
        HttpHeaders header = new HttpHeaders();
        header.setAccessControlExposeHeaders(Collections.singletonList("Content-Disposition"));
        header.set("Content-Disposition", "attachment; filename=product image");
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("image/jpeg"))
            .headers(header)
            .body(new ByteArrayResource(productImage));
    }

    @PostMapping("/productwithimg")
    ResponseEntity<Product> createProductWithImg(@RequestPart("productimage") MultipartFile[] productMultipartFiles,
                                               @RequestPart("productobject") Product product) {
        if (product.getTmpOrdered() == null) {
            product.setTmpOrdered(0);
        }

        Product savedProduct = productService.addProductWithImg(product, productMultipartFiles);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PostMapping("/productwithoutimage")
    ResponseEntity<Product> editProductWithoutImage(@RequestBody Product product) {
        Product savedProduct = productService.editProductWithoutImage(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }



    @PostMapping("/products/resetstates")
    ResponseEntity<?> resetStates() {
        try {
            productService.resetProductsState();
        } catch (UserPermissionDeniedException ex) {
            return new ResponseEntity<>("Nie masz uprawnień do wykoania tej operacji", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
