package com.damian.domain.prize;

import com.damian.domain.user.User;
import com.damian.domain.user.UserRepository;
import com.damian.security.SecurityUtils;
import com.itextpdf.tool.xml.PipelineException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

@Service
public class PrizeOrderService {

    private final int PRIZE_ORDER_STATUS_NOWE = 1;

    private PrizeOrderDao prizeOrderDao;
    private UserRepository userRepository;
    private PrizeDao prizeDao;

    public PrizeOrderService(PrizeDao prizeDao, PrizeOrderDao prizeOrderDao, UserRepository userRepository) {
        this.prizeOrderDao = prizeOrderDao;
        this.userRepository = userRepository;
        this.prizeDao = prizeDao;
    }

    @Transactional
    public PrizeOrder saveOrder(PrizeOrder order) throws NoPointsExceptions, PointsExceptions {
        Optional<User> curentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        order.setUser(curentUser.orElseThrow(() -> new AccessDeniedException("Access Denied")));
        PrizeOrderStatus prizeOrderStatus = new PrizeOrderStatus(PRIZE_ORDER_STATUS_NOWE);
        order.setPrizeOrderStatus(prizeOrderStatus);
        Integer pointsBeforeOrder = userRepository.getPoints(curentUser.get().getLogin());
        Integer pointsAfterOrder = pointsBeforeOrder - order.getOrderTotalAmount();
        if (pointsAfterOrder < 0) {
            throw new NoPointsExceptions("Brak punktów");
        }
        for (PrizeOrderItems prizeOrderItems : order.getPrizeOrderItems()) {
            Prize prize = prizeDao.findByIdNr(prizeOrderItems.getPrize().getId());
            if (prize.getStock() - prizeOrderItems.getQuantity() < 0) {
                throw new PointsExceptions("Brak nagrody na stanie magazynowym");
            }
            if (prize.getStock() - prizeOrderItems.getQuantity() <= 0) {
                prize.setAvailable(false);
                prizeDao.save(prize);
            }
            prizeDao.updateStockMinus(prizeOrderItems.getPrize().getId(), prizeOrderItems.getQuantity());
        }
        curentUser.get().setPoints(pointsAfterOrder);
        userRepository.save(curentUser.get());
        return prizeOrderDao.save(order);
    }

    public void updateOrder(PrizeOrder order) {
        prizeOrderDao.save(order);
    }
}
