package com.damian.service;

import com.damian.model.*;
import com.damian.repository.*;
import com.damian.rest.OrderController;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = Logger.getLogger(OrderController.class);
    private OrderDao orderDao;
    private CustomerDao customerDao;
    private AddressDao addressDao;
    private ProductDao productDao;
    private SupplierDao supplierDao;

    OrderService(OrderDao orderDao, CustomerDao customerDao, AddressDao addressDao, ProductDao productDao, SupplierDao supplierDao
                 ) {
        this.orderDao = orderDao;
        this.customerDao = customerDao;
        this.addressDao = addressDao;
        this.productDao = productDao ;
        this.supplierDao = supplierDao;

    }

    @Transactional
    public void createOrder(Order order) {

        Customer customer = order.getCustomer();


        if (customer.getCustomerId() != null) {
            Customer customerToSave = order.getCustomer();
            //order.setCustomer(null);
            customer.setAddresses(null);
            customerDao.saveAndFlush(customer);
            orderDao.saveAndFlush(order);

        } else {
            Customer savedCustomer = customerDao.saveAndFlush(customer);
            Address tmpAddres = savedCustomer.getAddresses().get(0);
            order.setAddress(tmpAddres);
            order.setCustomer(savedCustomer);
            orderDao.save(order);
        }

    }


//    @Transactional
//    public void changeDBStructure() {
//
//
//        List<Customer> customerList = customerDao.findAllBy();
//
//
//        customerList.forEach(customer -> {
//            Address address = new Address(customer.getCustomerId(), customer.getAddress(), customer.getZipCode(), customer.getCityName());
//
//
//            Long tempAddressId;
//            Address addressTmp = addressDao.save(address);
//            tempAddressId = addressTmp.getAddressId();
//
//
//            List<Order> orderListWithSpecificCustomer = orderDao.findByCustomerId(customer.getCustomerId());
//
//              logger.info(orderListWithSpecificCustomer);
//
//            orderListWithSpecificCustomer.forEach(order -> {
//
//                order.setAddressId(tempAddressId);
//                orderDao.save(order);
//            });
//
//        });
//
//
//    }
     @Transactional
    public void change2(){


      List<Product> tmpListProducts =   productDao.findAllBy();


      tmpListProducts.forEach(product -> {
         Integer tmpSuplierId = supplierDao.findBySupplierName(product.getDeliver()).getSupplierId();
         productDao.update(tmpSuplierId,product.getId());

      });

    }


}



