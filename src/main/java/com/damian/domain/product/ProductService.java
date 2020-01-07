package com.damian.domain.product;

import com.damian.boundry.rest.OrderController;
import com.damian.security.SecurityUtils;
import com.damian.security.UserPermissionDeniedException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Damian on 30.08.2018.
 */
@Service
public class ProductService {
    private static final org.apache.log4j.Logger logger = Logger.getLogger(ProductService.class);
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

    @Transactional
    public void resetProductsState() throws UserPermissionDeniedException{

        if (SecurityUtils.getCurrentUserLogin().equals("paldam")){
            productDao.resetDbPrductsStates();
        } else{
            throw new UserPermissionDeniedException("Brak uprawnień do wykonania tego polecenia");
        }

    }


@Transactional
    public void setMultiDelivery(Integer[] ids, Integer[] values)  {
       for(int i=0; i < ids.length; i++){

           if(values[i] >0)
           productDao.addProductToDeliver (ids[i],values[i]);
       }

    }

}
