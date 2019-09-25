package com.damian.domain.prize;

import com.damian.domain.user.User;
import com.damian.domain.user.UserRepository;
import com.damian.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class PrizeOrderService {

    private PrizeOrderDao prizeOrderDao;
    private UserRepository userRepository;

    public PrizeOrderService(PrizeOrderDao prizeOrderDao, UserRepository userRepository) {
        this.prizeOrderDao = prizeOrderDao;
        this.userRepository = userRepository;
    }

    @Transactional
    public PrizeOrder saveOrder(PrizeOrder order) throws NoPointsExceptions {
        Optional<User> curentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        order.setUser(curentUser.get());
        PrizeOrderStatus prizeOrderStatus = new PrizeOrderStatus(1);
        order.setPrizeOrderStatus(prizeOrderStatus);


        Integer pointsBeforeOrder = userRepository.getPoints(curentUser.get().getLogin());

        Integer pointsAfterOrder = pointsBeforeOrder - order.getOrderTotalAmount();


        if(pointsAfterOrder <0) {
            throw new NoPointsExceptions("Brak punktów");
        }

        curentUser.get().setPoints(pointsAfterOrder);

        userRepository.save( curentUser.get());

        return prizeOrderDao.save(order);
    }


    public void updateOrder(PrizeOrder order) {

        prizeOrderDao.save(order);


    }
}
