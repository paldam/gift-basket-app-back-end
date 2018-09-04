package com.damian.service;

import com.damian.dto.FileDto;
import com.damian.dto.OrderDto;
import com.damian.model.*;
import com.damian.repository.*;
import com.damian.rest.OrderController;
import org.apache.log4j.Logger;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = Logger.getLogger(OrderController.class);
    private OrderDao orderDao;
    private CustomerDao customerDao;
    private AddressDao addressDao;
    private ProductDao productDao;
    private SupplierDao supplierDao;
    private DbFileDao dbFileDao;

    OrderService(OrderDao orderDao, CustomerDao customerDao, AddressDao addressDao, ProductDao productDao,DbFileDao dbFileDao, SupplierDao supplierDao
                 ) {
        this.orderDao = orderDao;
        this.customerDao = customerDao;
        this.addressDao = addressDao;
        this.productDao = productDao ;
        this.supplierDao = supplierDao;
        this.dbFileDao = dbFileDao;

    }

    @Transactional
    public void createOrder(Order order) {

        Customer customer = order.getCustomer();

//        Calendar c = Calendar.getInstance();
//        c.setTime(order.getDeliveryDate());
//        c.add(Calendar.DATE, 1);
//        Date convertedDeliveryDate = c.getTime();
//        order.setDeliveryDate(convertedDeliveryDate);


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

    public List<OrderDto> getOrderDao(){

        List<Order> orderList = orderDao.findAllWithoutDeleted();
         List<OrderDto>   orderDtoList = new ArrayList<>() ;
        List<DbFile>   dbFileDtoList = dbFileDao.findAll() ;
       
   


        orderList.forEach(order -> {
            List<DbFile> result = new LinkedList<>();
            //result =  dbFileDtoList.stream().filter(data -> data.getOrderId() == 835).collect(Collectors.toList());

            result =  dbFileDtoList.stream()
                    .filter(data -> order.getOrderId().equals(data.getOrderId()))
                    .collect(Collectors.toList());
            
            Long fileIdTmp = null;


                 if(result.size() >0) {
                     fileIdTmp = result.get(0).getFileId();
                 }else{
                     fileIdTmp = 0L;
                 }

              orderDtoList.add(new OrderDto(order.getOrderId(), order.getOrderFvNumber(), order.getCustomer(), order.getOrderDate(),
                    order.getAdditionalInformation(), order.getDeliveryDate(),order.getDeliveryType(),
                    order.getOrderStatus(), order.getOrderTotalAmount(), fileIdTmp)) ;
        });

        
        return orderDtoList;

    }


    public List<Order> getOrderListFromIdList(List<Long> orederIdList) {

        List<Order> ordersList = new ArrayList<>();

        orederIdList.forEach(orderId -> {
            Order orderToAdd = orderDao.findOne(orderId);
            ordersList.add(orderToAdd);

        });

        return  ordersList;
    }



}



