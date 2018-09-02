package com.damian.service;

import com.damian.repository.ProductDao;
import org.springframework.stereotype.Service;

/**
 * Created by Damian on 30.08.2018.
 */
@Service
public class ProductService {
    private ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }



    public void changeStock(Integer productId, Integer addValue)  {

              productDao.updateStock(productId,addValue);

    }

}
