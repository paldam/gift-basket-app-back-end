package com.damian.domain.basket;

import com.damian.util.FtpConnectionException;
import com.damian.util.FtpService;
import com.damian.util.FtpSpace;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;
import java.io.*;

@Service
public class BasketExtService {

    private final FtpService ftpService;
    private final BasketDao basketDao;

    public BasketExtService(FtpService ftpService, BasketDao basketDao) {
        this.ftpService = ftpService;
        this.basketDao = basketDao;
    }

    public void editExternalBasket(BasketExt basket) {
        Basket currBasketState = basketDao.findByBasketId(basket.getBasketId());
        currBasketState.setBasketName(basket.getBasketName());
        currBasketState.setBasketTotalPrice(basket.getBasketTotalPrice());
        currBasketState.setIsAlcoholic(basket.getIsAlcoholic());
        currBasketState.setBasketType(basket.getBasketType());
        int total = 0;
        for (BasketItems bi : basket.getBasketItems()) {
            total += bi.getProduct().getPrice() * bi.getQuantity();
        }
        currBasketState.setBasketProductsPrice(total);
        basketDao.save(currBasketState);
    }

    public void saveExternalBasket(BasketExt basket) throws FtpConnectionException {
        basket.setBasketProductsPrice(computeTotalProductsPriceInBasket(basket));
        Basket externalBasket = new Basket(basket);
        externalBasket.setBasketType(new BasketType(100, "eksportowy"));
        externalBasket.setBasketSezon(new BasketSezon(0));
        Basket savedBasket = basketDao.save(externalBasket);
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


