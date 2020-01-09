package com.damian.domain.basket;

import com.damian.util.FtpConnectionException;
import com.damian.util.FtpService;
import com.damian.util.FtpSpace;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class BasketExtService {
    private static final Logger logger = Logger.getLogger(BasketExtService.class);

    private FtpService ftpService;
    private BasketDao basketDao;
    private BasketTypeDao basketTypeDao;

    public BasketExtService(FtpService ftpService,BasketDao basketDao, BasketTypeDao basketTypeDao) {
        this.ftpService = ftpService;
        this.basketDao = basketDao;
        this.basketTypeDao = basketTypeDao;
    }




    public void editExternalBasket(BasketExt basket) {

        Basket currBasketState = basketDao.findByBasketId(basket.getBasketId());

        currBasketState.setBasketName(basket.getBasketName());
        currBasketState.setBasketTotalPrice(basket.getBasketTotalPrice());
        currBasketState.setIsAlcoholic(basket.getIsAlcoholic());
        currBasketState.setBasketType(basket.getBasketType());


        Integer total = 0;
        for (BasketItems bi : basket.getBasketItems()) {
            total += bi.getProduct().getPrice() * bi.getQuantity();
        }
        currBasketState.setBasketProductsPrice(total);


        basketDao.save(currBasketState);
    }

    public void saveExternalBasket(BasketExt basket) throws FtpConnectionException {
        basket.setBasketProductsPrice(computeTotalProductsPriceInBasket(basket));
        Basket basketD = new Basket(basket);
        basketD.setBasketType(new BasketType(100, "eksportowy"));
        basketD.setBasketSezon(new BasketSezon(0));
        Basket savedBasket = basketDao.save(basketD);
        InputStream basketImgtoStore = new ByteArrayInputStream(basket.getBasketImg());
        try {
            ftpService.sendFileViaFtp(basketImgtoStore, savedBasket.getBasketId().toString(), FtpSpace.PRIZES);
        } catch (FtpConnectionException ioe) {
            basketDao.delete(savedBasket);
            throw new FtpConnectionException("Problem z połaczeniem FTP");
        }
    }

    private Integer computeTotalProductsPriceInBasket(BasketExt basket) {
        int totalProductsPriceInBasket = 0;
        for (BasketItems bi : basket.getBasketItems()) {
            totalProductsPriceInBasket += bi.getProduct().getPrice() * bi.getQuantity();
        }
        return totalProductsPriceInBasket;
    }



    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }


}


