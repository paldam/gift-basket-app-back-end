package com.damian.service;

import com.damian.model.Address;
import com.damian.model.Customer;
import com.damian.model.Order;
import com.damian.model.OrderItem;
import com.damian.repository.AddressDao;
import com.damian.repository.CustomerDao;
import com.damian.repository.OrderDao;
import com.damian.rest.OrderController;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = Logger.getLogger(OrderController.class);
    private OrderDao orderDao;
    private CustomerDao customerDao;
    private AddressDao addressDao;

    OrderService(OrderDao orderDao, CustomerDao customerDao, AddressDao addressDao) {
        this.orderDao = orderDao;
        this.customerDao = customerDao;
        this.addressDao = addressDao;
    }

    @Transactional
    public void createOrder(Order order) {

        Customer customer = order.getCustomer();

        if (customer.getCustomerId() != null) {
            Customer customerToSave = order.getCustomer();
            //order.setCustomer(null);
            customerDao.saveAndFlush(customer);
            orderDao.saveAndFlush(order);

            //orderDao.de
        } else {
            Customer savedCustomer = customerDao.saveAndFlush(customer);
            order.setCustomer(savedCustomer);
            orderDao.save(order);
        }

        order.getOrderItems().forEach(orderItem -> {
            orderItem.getBasket().getBasketItems().forEach(basketItems -> {
                System.out.println(basketItems.getProduct().getProductName() + basketItems.getQuantity());

            });
        });
    }


    @Transactional
    public void changeDBStructure() {


        List<Customer> customerList = customerDao.findAllBy();


        customerList.forEach(customer -> {
            Address address = new Address(customer.getCustomerId(), customer.getAddress(), customer.getZipCode(), customer.getCityName(), customer.getPhoneNumber());


            Long tempAddressId;
            Address addressTmp = addressDao.save(address);
            tempAddressId = addressTmp.getAddressId();


            List<Order> orderListWithSpecificCustomer = orderDao.findByCustomerId(customer.getCustomerId());

              logger.info(orderListWithSpecificCustomer);

            orderListWithSpecificCustomer.forEach(order -> {

                order.setAddressId(tempAddressId);
                orderDao.save(order);
            });

        });


    }

}



