package com.damian.service;

import com.damian.repository.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

/**
 * Created by Damian on 30.08.2018.
 */
@Service
public class ProductService {
    private ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }


@Transactional
    public void changeStockEndResetOfProductsToDelivery(Integer productId, Integer addValue)  {

              productDao.updateStock(productId,addValue);
              productDao.resetProductToDeliver(productId);

    }

    public void addNumberOfProductsDelivery(Integer productId, Integer addValue)  {

        productDao.addProductToDeliver(productId,addValue);

    }

}
