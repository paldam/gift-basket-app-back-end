package com.damian.domain.order;

import com.damian.domain.basket.BasketItems;
import com.damian.domain.customer.*;
import com.damian.domain.order_file.DbFile;
import com.damian.domain.order_file.DbFileDao;
import com.damian.domain.product.ProductDao;
import com.damian.domain.product.SupplierDao;
import com.damian.domain.user.User;
import com.damian.domain.user.UserRepository;
import com.damian.dto.NumberProductsToChangeStock;
import com.damian.dto.OrderDto;
import com.damian.domain.order.exceptions.OrderStatusException;
import com.damian.security.SecurityUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

@Service
public class OrderService {

    public static final int ORDER_STATUS_ID_NOWE = 1;
    public static final int ORDER_STATUS_ID_ZREALIZOWANE = 5;
    private static final Logger logger = Logger.getLogger(OrderService.class);
    private OrderDao orderDao;
    private OrderItemDao orderItemDao;

    private CustomerDao customerDao;
    private AddressDao addressDao;
    private ProductDao productDao;
    private SupplierDao supplierDao;
    private DbFileDao dbFileDao;
    private CustomCustomerDao customCustomerDao;
    private CompanyDao companyDao;
    private UserRepository userRepository;

    OrderService(UserRepository userRepository, OrderDao orderDao,OrderItemDao orderItemDao, CompanyDao companyDao, CustomCustomerDao customCustomerDao, CustomerDao customerDao, AddressDao addressDao, ProductDao productDao, DbFileDao dbFileDao, SupplierDao supplierDao) {
        this.companyDao = companyDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.customerDao = customerDao;
        this.addressDao = addressDao;
        this.productDao = productDao;
        this.supplierDao = supplierDao;
        this.dbFileDao = dbFileDao;
        this.customCustomerDao = customCustomerDao;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createOrUpdateOrder(Order order) throws OrderStatusException {
        if (!Objects.isNull(order.getOrderId())) {  // update order
            performChangeOrderStatusOperation(order);
        }
        if (order.getCustomer().getCustomerId() != null) {
            performOrderCustomerFromDB(order);
        } else {
            performOrderWithNewCustomer(order);
        }
    }

    private void performChangeOrderStatusOperation(Order order) throws OrderStatusException {
        Order orderPrevState = orderDao.findByOrderId(order.getOrderId());
        Integer prevOrderStatus = orderPrevState.getOrderStatus().getOrderStatusId();
        Integer newOrderStatus = order.getOrderStatus().getOrderStatusId();
        //TODO
        if (prevOrderStatus == 1 && newOrderStatus == 6) {
            changeStockStatusNoweToWtrakcie(order);
        }
        if (prevOrderStatus == 6 && newOrderStatus == 1) {
            //TODO error
            Order currentStateOfOrder = orderDao.findByOrderId(order.getOrderId());
            List<OrderItem> orderItemsTmp = currentStateOfOrder.getOrderItems();
            for (OrderItem orderItem : orderItemsTmp) {
                if (orderItem.getStateOnProduction() > 0 || orderItem.getStateOnLogistics() > 0 || orderItem.getStateOnWarehouse() > 0) {
                    throw new OrderStatusException("brak możliwości zmiany statusu z 'w trakcie realizacji' na 'nowy' z powodu rozpoczęcia realizacji");
                }
            }
            changeStockStatusWtrakcieToNowe(order);
        }

    }

    public void changeStockStatusNoweToWtrakcie(Order order) {
        order.getOrderItems().forEach(orderItem -> {
            int v1 = orderItem.getQuantity();
            orderItem.getBasket().getBasketItems().forEach(basketItems -> {
                int v2 = basketItems.getQuantity();
                logger.info(basketItems.getProduct().getProductName() + " Liczba " + (v1 * v2));
                productDao.updateStockTmpAdd(basketItems.getProduct().getId(), (new Long(v1 * v2)));
            });
        });
    }

    public void changeStockStatusWtrakcieToNowe(Order order) {
        List<OrderItem> tmp = order.getOrderItems();
        List<NumberProductsToChangeStock> numberProductsToChangeStocks = new ArrayList<>();
        tmp.forEach(oi -> {
            oi.getBasket().getBasketItems().forEach(basketItems -> {
                numberProductsToChangeStocks.add(new NumberProductsToChangeStock(basketItems.getProduct().getId(), new Long(oi.getStateOnWarehouse())));
            });
        });
        numberProductsToChangeStocks.forEach(product -> {
            productDao.updateStockTmpMinus(product.getProductId(), product.getQuantityToChange());
        });
    }

    @Transactional
    public void changeOrderItemProgressWarehouse(Integer orderItemId, Long newStateValueOnWarehouse ) throws OrderStatusException {
        OrderItem currentOrderItemState = orderItemDao.findByOrderItemId(orderItemId);

         System.out.println(ANSI_YELLOW + currentOrderItemState.getQuantity() + " " + newStateValueOnWarehouse + ANSI_RESET);




        if(newStateValueOnWarehouse > currentOrderItemState.getQuantity()){
            throw new OrderStatusException("Brak możliwości zmiany, liczba nie może być większa od całkowitej liczby koszy ");
        }
        if(newStateValueOnWarehouse < 0 ){
            throw new OrderStatusException("Wartość nie może być mniejsza od zera");
        }
        orderDao.changeSpecyfiedOrderItemProgressOnWarehouse(orderItemId,newStateValueOnWarehouse);
    }


    @Transactional
    public void changeOrderProgress(Long id, List<OrderItem> orderItemsList) throws OrderStatusException {
        Order updatingOrder = orderDao.findByOrderId(id);
        List<OrderItem> tmpCurrentOrderItems = updatingOrder.getOrderItems();
        changeStockWhenWarehouseUpdateOrderProgress(id, orderItemsList);

        if (updatingOrder.getOrderStatus().getOrderStatusId() == 1) {
            throw new OrderStatusException("Brak możliwości zmiany przy statusie zamowienia 'nowy' ");
        }

        Boolean completeOrderWatch = true;

        for (OrderItem oi : orderItemsList) {
            if (oi.getStateOnLogistics() > oi.getQuantity() || oi.getStateOnProduction() > oi.getQuantity() || oi.getStateOnWarehouse() > oi.getQuantity()) {
                throw new OrderStatusException("Brak możliwości zmiany, liczba nie może być większa od całkowitej liczby koszy ");
            }
            if (oi.getStateOnLogistics() < 0 || oi.getStateOnProduction() < 0 || oi.getStateOnWarehouse() < 0) {
                throw new OrderStatusException("Wartość nie może być mniejsza od zera");
            }
            if (oi.getStateOnProduction() > oi.getStateOnWarehouse()) {
                throw new OrderStatusException("Stan koszy ukończonych nie może być większy od liczby koszy przygotowanych przez magazyn ");
            }
            if (oi.getStateOnLogistics() > oi.getStateOnProduction()) {
                throw new OrderStatusException("Stan koszy ukończonych nie może być większy od liczby koszy przygotowanych przez produkcje ");
            }
            if (!oi.getQuantity().equals(oi.getStateOnLogistics())) {
                completeOrderWatch = false;
            }
        }
        if (completeOrderWatch) updatingOrder.setOrderStatus(new OrderStatus(ORDER_STATUS_ID_ZREALIZOWANE));
        updatingOrder.setOrderItems(orderItemsList);
        orderDao.save(updatingOrder);
    }

    @Transactional
    public void changeStockWhenWarehouseUpdateOrderProgress(Long orderId, List<OrderItem> orderItemsList) {
        Order currentOrderVersion = orderDao.findByOrderId(orderId);
        List<NumberProductsToChangeStock> numberProductsToChangeStocks = new ArrayList<>();

        currentOrderVersion.getOrderItems().forEach(orderItem -> {
            orderItem.getBasket().getBasketItems().forEach(basketItems -> {
                numberProductsToChangeStocks.add(new NumberProductsToChangeStock(basketItems.getProduct().getId(), new Long(orderItem.getQuantity())));
            });
        });

        numberProductsToChangeStocks.forEach(product -> {
            productDao.updateStockTmpMinus(product.getProductId(), product.getQuantityToChange());
        });


        List<NumberProductsToChangeStock> numberProductsToChangeStocks2 = new ArrayList<>();

        for (OrderItem oi:orderItemsList) {
            Long totalBasketNumber = new Long(0);
            for (BasketItems basketItems: oi.getBasket().getBasketItems()) {
                totalBasketNumber = new Long(oi.getQuantity());
                numberProductsToChangeStocks2.add(new NumberProductsToChangeStock(basketItems.getProduct().getId(),(totalBasketNumber- new Long(oi.getStateOnWarehouse()) )));
            }

        }
        numberProductsToChangeStocks2.forEach(product -> {
            productDao.updateStockTmpAdd(product.getProductId(), product.getQuantityToChange());
        });



//        orderItemsList.forEach(oi -> {
//            Long totalBasketNumber = new Long(oi.getQuantity());
//            oi.getBasket().getBasketItems().forEach(basketItems -> {
//
//                numberProductsToChangeStocks.add(new NumberProductsToChangeStock(basketItems.getProduct().getId(), new Long(oi.getStateOnWarehouse())));
//            });
//        });
//        numberProductsToChangeStocks.forEach(product -> {
//            productDao.updateStockTmpMinus(product.getProductId(), product.getQuantityToChange());
//        });

        //        orderItemsList.forEach(orderItem -> {
        //            int v1 = orderItem.getQuantity();
        //            int v2 = orderItem.getStateOnWarehouse();
        //            orderItem.getBasket().getBasketItems().forEach(basketItems -> {
        //                productDao.updateStockMinus(basketItems.getProduct().getId(), (new Long(v1 * v2)));
        //            });
        //        });
    }




//    public void changeStockWhenWarehouseUpdateOrderProgress(Long orderId, List<OrderItem> orderItemsList) {
//        List<NumberProductsToChangeStock> numberProductsToChangeStocks = productDao.numberProductsToChangeStock(orderId);
//        numberProductsToChangeStocks.forEach(product -> {
//            productDao.updateStockTmpMinus(product.getProductId(), product.getQuantityToChange());
//        });
//        orderItemsList.forEach(orderItem -> {
//            int v1 = orderItem.getQuantity();
//            orderItem.getBasket().getBasketItems().forEach(basketItems -> {
//                int v2 = basketItems.getQuantity();
//                logger.info(basketItems.getProduct().getProductName() + " Liczba " + (v1 * v2));
//                productDao.updateStockMinus(basketItems.getProduct().getId(), (new Long(v1 * v2)));
//            });
//        });
//    }


    public void assignOrdersToSpecifiedProduction(List<Integer> ordersIds, Long productionId) throws OrderStatusException {
        List<Order> orderListByIds = orderDao.findByOrderIds(ordersIds);
        for (Order order : orderListByIds) {
            if (order.getOrderStatus().getOrderStatusId() != ORDER_STATUS_ID_NOWE) {
                throw new OrderStatusException("Nie można zmienić  lub przydzielić produkcji do zamówienia o statusie innym niż NOWE");
            }
        }
        orderDao.assignOrdersToSpecifiedProduction(ordersIds, productionId);
    }

    public List<OrderDto> getOrderStats() {
        List<Order> orderList = orderDao.findAllWithoutDeleted();
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<DbFile> dbFileDtoList = dbFileDao.findAll();
        List<OrderItem> oredrItemsList = new ArrayList<>();
        orderList.forEach(order -> {
            Long fileIdTmp = null;
            orderDtoList.add(new OrderDto(order.getOrderId(), order.getOrderFvNumber(), order.getCustomer(), order.getOrderDate(), order.getAdditionalInformation(), order.getDeliveryDate(), order.getWeekOfYear(), order.getDeliveryType(), order.getOrderStatus(), order.getOrderTotalAmount(), fileIdTmp, order.getOrderItems(), order.getAdditionalSale(), order.getAddress()));
        });
        return orderDtoList;
    }

    public List<OrderDto> getOrderDaoByCustomer(Integer id) {
        List<Order> orderList = orderDao.findByCustomerId(id);
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<DbFile> dbFileDtoList = dbFileDao.findAll();
        List<OrderItem> oredrItemsList = new ArrayList<>();
        orderList.forEach(order -> {
            logger.error("EEEEEEEE" + order.getWeekOfYear());
            List<DbFile> result = new LinkedList<>();
            //result =  dbFileDtoList.stream().filter(data -> data.getOrderHistoryId() == 835).collect(Collectors.toList());
            result = dbFileDtoList.stream().filter(data -> order.getOrderId().equals(data.getOrderId())).collect(Collectors.toList());
            Long fileIdTmp = null;
            if (result.size() > 0) {
                fileIdTmp = result.get(0).getFileId();
            } else {
                fileIdTmp = 0L;
            }
            orderDtoList.add(new OrderDto(order.getOrderId(), order.getOrderFvNumber(), order.getCustomer(), order.getOrderDate(), order.getAdditionalInformation(), order.getDeliveryDate(), order.getWeekOfYear(), order.getDeliveryType(), order.getOrderStatus(), order.getOrderTotalAmount(), fileIdTmp, oredrItemsList, order.getAdditionalSale(), order.getProductionUser()));
        });
        return orderDtoList;
    }


    @Transactional
    public List<Order> getOrderListFromIdList(List<Long> orederIdList) {
        List<Order> ordersList = new ArrayList<>();
        orederIdList.forEach(orderId -> {
            Order orderToAdd = orderDao.findByOrderId(orderId);
            ordersList.add(orderToAdd);
        });
        return ordersList;
    }
    public OrderPageRequest getOrderDao(int page, int size, String text, String orderBy, int sortingDirection, List<Integer> orderStatusFilterArray, List<Integer> orderYearsFilterList) {
        Sort.Direction sortDirection = sortingDirection == -1 ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Order> orderList;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, orderBy));
        if (orderStatusFilterArray.isEmpty() && orderYearsFilterList.isEmpty()) {
            orderList = orderDao.findAll(OrderSpecyficationJPA.getOrderWithoutdeleted().and(OrderSpecyficationJPA.getOrderWithSearchFilter(text)), pageable);
        } else {
            if (!orderStatusFilterArray.isEmpty() && orderYearsFilterList.isEmpty()) {
                orderList = orderDao.findAll(OrderSpecyficationJPA.getOrderWithoutdeleted().and(OrderSpecyficationJPA.getOrderWithFilter(orderStatusFilterArray).and(OrderSpecyficationJPA.getOrderWithSearchFilter(text))), pageable);
            } else if (orderStatusFilterArray.isEmpty() && !orderYearsFilterList.isEmpty()) {
                orderList = orderDao.findAll(OrderSpecyficationJPA.getOrderWithoutdeleted().and(OrderSpecyficationJPA.getOrderWithOrderYearsFilter(orderYearsFilterList).and(OrderSpecyficationJPA.getOrderWithSearchFilter(text))), pageable);
            } else {
                orderList = orderDao.findAll(OrderSpecyficationJPA.getOrderWithoutdeleted().and(OrderSpecyficationJPA.getOrderWithOrderYearsFilter(orderYearsFilterList).and(OrderSpecyficationJPA.getOrderWithSearchFilter(text)).and(OrderSpecyficationJPA.getOrderWithFilter(orderStatusFilterArray))), pageable);
            }
        }
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<DbFile> dbFileDtoList = dbFileDao.findAll();
        //List<OrderItem> oredrItemsList = new ArrayList<>();
        orderList.forEach(order -> {
            List<DbFile> result = new LinkedList<>();
            //result =  dbFileDtoList.stream().filter(data -> data.getOrderHistoryId() == 835).collect(Collectors.toList());
            result = dbFileDtoList.stream().filter(data -> order.getOrderId().equals(data.getOrderId())).collect(Collectors.toList());
            Long fileIdTmp = null;
            if (result.size() > 0) {
                fileIdTmp = result.get(0).getFileId();
            } else {
                fileIdTmp = 0L;
            }
            orderDtoList.add(new OrderDto(order.getOrderId(), order.getOrderFvNumber(), order.getCustomer(), order.getOrderDate(), order.getAdditionalInformation(), order.getDeliveryDate(), order.getWeekOfYear(), order.getDeliveryType(), order.getOrderStatus(), order.getOrderTotalAmount(), fileIdTmp, order.getOrderItems(), order.getAdditionalSale(), order.getProductionUser()));
        });
        return new OrderPageRequest(orderDtoList, orderList.getTotalElements());
    }
    private void performOrderCustomerFromDB(Order order) {
        Customer customerToSave = order.getCustomer();
        Company company = order.getCustomer().getCompany();
        if (order.getAddress().getAddressId() == null) {
            Address savedAddress = addressDao.save(order.getAddress());
            order.setAddress(savedAddress);
        } else {
        }
        Optional<Customer> findCustomer = customCustomerDao.findExacCustomerByEntity(customerToSave); // sprawdza czy klient z ta firma jest w bazie
        if (findCustomer.isPresent()) {
            Customer savedCustomer = customerDao.save(customerToSave); //robi tylko za ewentualny update //  potrzebne tylko do zaktualizowania getAdditionalIforamtion
            order.setCustomer(savedCustomer);
            orderDao.saveAndFlush(order);
        } else {
            List<Customer> cust = customCustomerDao.findCustomerByCriteria(customerToSave); // sprawdza czy klient z tymi danymi poza (id) jest juz w bazie,(zapobieganie dupliaktą)
            if (cust.size() > 0) {
                customerToSave = cust.get(0);
                customerToSave.setAdditionalInformation(order.getCustomer().getAdditionalInformation()); // tylko to pole mozna zmienic
            } else {    // jesli nie ma tworzy nowego pracownika i zapisuje w bazie
                customerToSave.setCustomerId(null);
            }
            Customer savedCustomer = customerDao.saveAndFlush(customerToSave);
            order.setCustomer(savedCustomer);
            orderDao.saveAndFlush(order);
        }
    }

    private void performOrderWithNewCustomer(Order order) {
        if (order.getAddress().getAddressId() == null) {
            Address savedAddress = addressDao.save(order.getAddress());
            order.setAddress(savedAddress);
        } else {
        }
        Customer savedCustomer = customerDao.saveAndFlush(order.getCustomer());
        order.setCustomer(savedCustomer);
        orderDao.save(order);
    }


}



