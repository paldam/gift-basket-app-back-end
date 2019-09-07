package com.damian.domain.prize;

import org.springframework.stereotype.Service;

@Service
public class PrizeOrderService {

    private PrizeOrderDao prizeOrderDao;

    public PrizeOrderService(PrizeOrderDao prizeOrderDao) {
        this.prizeOrderDao =prizeOrderDao;
    }



    public PrizeOrder saveOrder(PrizeOrder  order){
        return prizeOrderDao.save(order);
    }

}
