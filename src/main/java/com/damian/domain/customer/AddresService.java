package com.damian.domain.customer;

import com.damian.domain.order.Order;
import com.damian.domain.order.OrderDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

@Service
public class AddresService {

    private AddressDao addressDao;
    private OrderDao orderDao;
    private ZipCodeDao zipCodeDao;
    private ProvinceDao provinceDao;

    public AddresService(AddressDao addressDao, OrderDao orderDao,ZipCodeDao zipCodeDao,ProvinceDao provinceDao) {
        this.addressDao = addressDao;
        this.orderDao = orderDao;
        this.zipCodeDao = zipCodeDao;
        this.provinceDao = provinceDao;
    }

    public void changePrimaryAddr(Long id, Integer customerId) {
        this.addressDao.setAllCustomerAdrressPrimaryNo(customerId);
        this.addressDao.setAddreesAsPrimary(id);
    }

    private boolean existsAtLeastOneOrder(Long id) {
        List<Order> ordersTmp = orderDao.findByAddress_AddressId(id);
        return !ordersTmp.isEmpty();
    }

    @Transactional
    public void convertZipCode() {
        List<ZipCode> zipCodes = zipCodeDao.findAll();
        zipCodes.forEach(zipCode -> {
            List<Province> provincesTmp = provinceDao.findByName(zipCode.getProvinceName());
            if (provincesTmp.isEmpty()) {
                zipCode.setProvince(null);
            } else {
                zipCode.setProvince(provincesTmp.get(0));
                zipCodeDao.save(zipCode);
            }
        });
    }

    @Transactional
    public void convertZipCode2() {
        List<Address> addressList = addressDao.findAll();
        addressList.forEach(address -> {
            address.getZipCode();
            List<ZipCode> zipTmp = zipCodeDao.findByZipCodeCode(address.getZipCode());
            if (zipTmp.isEmpty()) {
                address.setProvince(null);
            } else {
                address.setProvince(zipTmp.get(0).getProvince());
                addressDao.save(address);
            }
        });
    }
}
