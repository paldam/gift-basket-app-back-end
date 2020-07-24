package com.damian.domain.customer;

import com.damian.domain.order.Order;
import com.damian.domain.order.OrderDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class AddresService {

    private AddressDao addressDao;
    private OrderDao orderDao;

    public AddresService(AddressDao addressDao, OrderDao orderDao) {
        this.addressDao = addressDao;
        this.orderDao = orderDao;
    }

    @Transactional
    public void changePrimaryAddr(Long id, Integer customerId) {
        this.addressDao.setAllCustomerAdrressPrimaryNo(customerId);
        this.addressDao.setAddreesAsPrimary(id);
    }

    private boolean existsAtLeastOneOrder(Long id) {
        List<Order> ordersTmp = orderDao.findByAddress_AddressId(id);
        return !ordersTmp.isEmpty();
    }
}
