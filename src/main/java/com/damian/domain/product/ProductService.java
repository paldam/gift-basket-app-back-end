package com.damian.domain.product;

import com.damian.security.SecurityUtils;
import com.damian.security.UserPermissionDeniedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public void changeStockEndResetOfProductsToDelivery(Integer productId, Integer addValue) {
        productDao.updateStock(productId, addValue);
        Product productTmp = productDao.findById(productId);
        int valueToSetTmpOrdered = 0;
        if (addValue >= productTmp.getTmpOrdered()) {
            valueToSetTmpOrdered = 0;
        } else {
            valueToSetTmpOrdered = productTmp.getTmpOrdered() - addValue;
        }
        productDao.setProductToDeliver(productId, valueToSetTmpOrdered);
    }

    public void addNumberOfProductsDelivery(Integer productId, Integer addValue) {
        productDao.addProductToDeliver(productId, addValue);
    }

    public void resetProductsState() throws UserPermissionDeniedException {
        if (SecurityUtils.getCurrentUserLogin().equals("paldam") && SecurityUtils.isCurrentUserInRole("admin")) {
            productDao.resetDbPrductsStates();
        } else {
            throw new UserPermissionDeniedException("Brak uprawnień do wykonania tego polecenia");
        }
    }

    @Transactional
    public void setMultiDelivery(Integer[] ids, Integer[] values) {
        for (int i = 0; i < ids.length; i++) {
            if (values[i] > 0) productDao.addProductToDeliver(ids[i], values[i]);
        }
    }

    @Transactional
    public Product editProductWithoutImage(Product product) {
        Product productTmp =
            productDao.findAllById(product.getId())
                .orElseThrow(EntityNotFoundException::new);
        product.setProductImageData(productTmp.getProductImageData());
        return productDao.save(product);
    }

    @Transactional
    public Product addProductWithImg(Product product , MultipartFile[] productMultipartFiles) {
        try {
            product.setProductImageData(productMultipartFiles[0].getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        product.setIsProductImg(1);
        return productDao.save(product);
    }

    public byte[] prepareProductImage(Long productId) {
        byte[] productFile = productDao.getProductImageByBasketId(productId);
        Optional<byte[]> imgOpt = Optional.ofNullable(productFile);
        if (!imgOpt.isPresent()) {
            productFile = new byte[0];
        }
        return productFile;
    }

    @Transactional(readOnly = true)
    public ProductPageRequest getProductsPage(int page, int size, String text, String orderBy, int sortingDirection,
                                              List<Integer> productSubTypeFilter, List<Integer> productSuppliersFilter, Boolean onlyAvailable) {
        Sort.Direction sortDirection = sortingDirection == -1 ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Product> productsPage;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, orderBy));


        productsPage = productDao.findAll(
            ProductSpecificationJpa.getProductWithSearchFilter(text)
            .and(ProductSpecificationJpa.getProductWithSpecType(productSubTypeFilter)
            .and(ProductSpecificationJpa.getProductWithSpecSupplier(productSuppliersFilter)
                .and(ProductSpecificationJpa.getOnlyAvailable(onlyAvailable)
                    .and(ProductSpecificationJpa.getNonArchival())
            ))), pageable);

        List<Integer> productsIds = new ArrayList<>();
        List<Product> productListWithFetchJoin = new ArrayList<>(); // tip to avoid pagination in memory
        if( productsPage.getTotalElements() != 0) {
            productsPage.get().forEach(product -> {
                productsIds.add(product.getId());
            });
            productListWithFetchJoin = productDao.findAllWithoutDeletedByIds(productsIds, Sort.by(sortDirection, orderBy));
        }
        return new ProductPageRequest(productListWithFetchJoin, productsPage.getTotalElements());
    }
}
