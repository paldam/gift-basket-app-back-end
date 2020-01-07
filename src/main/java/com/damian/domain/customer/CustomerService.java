package com.damian.domain.customer;

import com.damian.dto.CustomerAddressDTO;
import com.damian.domain.order.Order;
import com.damian.domain.order.OrderDao;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    private static final Logger logger = Logger.getLogger(CustomerService.class);
    private CustomerDao customerDao;
    private AddressDao addressDao;
    private OrderDao orderDao;

    public CustomerService(CustomerDao customerDao, AddressDao addressDao, OrderDao orderDao) {
        this.addressDao =addressDao;
        this.customerDao = customerDao;
        this.orderDao = orderDao;
    }


    public List<CustomerAddressDTO> getCustomerWithAddressList() {

        List<Customer> custList = customerDao.findAllBy();
        List<CustomerAddressDTO> custAddrList = new ArrayList<>();

         custList.forEach(customer -> {

          Address tmpAddr = addressDao.findCustomerPrimaryAddrById(customer.getCustomerId())
              .orElseThrow(EntityNotFoundException::new);





          custAddrList.add(new CustomerAddressDTO(customer,tmpAddr));
         });

         return  custAddrList;
    }

    @Transactional
    public ResponseEntity deleteCustomer(Integer customerId) {
        if (existsAtLeastOneCustomerOrders(customerId)) {
            return new ResponseEntity("Nie można usunąć klienta, Podany klient jest powiązany z conajmniej jednym zamówieniem", HttpStatus.FORBIDDEN);
        } else {
            customerDao.deleteByCustomerId(customerId);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    private boolean existsAtLeastOneCustomerOrders(Integer id) {
        List<Order> ordersTmp = orderDao.findByCustomerId(id);
        if (ordersTmp.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

}
