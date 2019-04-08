package com.damian.domain.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


             Product productTmp = productDao.findById(productId);

             int valueToSetTmpOrdered = 0;

             if( addValue >= productTmp.getTmpOrdered()){
                 valueToSetTmpOrdered = 0;
             }else{
                 valueToSetTmpOrdered = productTmp.getTmpOrdered() - addValue;
             }

              productDao.setProductToDeliver(productId,valueToSetTmpOrdered);

    }

    public void addNumberOfProductsDelivery(Integer productId, Integer addValue)  {

        productDao.addProductToDeliver(productId,addValue);

    }

}
