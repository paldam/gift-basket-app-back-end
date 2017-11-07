package com.damian.service;

import com.damian.model.Customer;
import com.damian.model.Order;
import com.damian.model.OrderItem;
import com.damian.repository.CustomerDao;
import com.damian.repository.OrderDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private OrderDao orderDao;
    private CustomerDao customerDao;

    OrderService(OrderDao orderDao, CustomerDao customerDao){
        this.orderDao = orderDao;
        this.customerDao= customerDao;
    }

   @Transactional
   public void createOrder(Order order){

        Customer customer = order.getCustomer();

        if (customer.getCustomerId() != null){
            Customer customerToSave = order.getCustomer();
            //order.setCustomer(null);
            customerDao.saveAndFlush(customer);
            orderDao.saveAndFlush(order);

            //orderDao.de
        }else{
            Customer savedCustomer = customerDao.saveAndFlush(customer);
            order.setCustomer(savedCustomer);
            orderDao.save(order);
        }

        order.getOrderItems().forEach(orderItem->{
            orderItem.getBasket().getBasketItems().forEach(basketItems -> {
                System.out.println(basketItems.getProduct().getProductName() + basketItems.getQuantity());

            });
        });
    }
}
