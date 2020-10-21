package com.damian.domain.order;

import com.damian.boundry.rest.NotificationsController;
import com.damian.boundry.rest.OrderController;
import com.damian.domain.basket.Basket;
import com.damian.domain.basket.BasketDao;
import com.damian.domain.basket.BasketSezon;
import com.damian.domain.customer.*;
import com.damian.domain.order_file.DbFile;
import com.damian.domain.order_file.DbFileDao;
import com.damian.domain.prize.PointScheme;
import com.damian.domain.prize.PointsDao;
import com.damian.domain.product.ProductDao;
import com.damian.domain.user.User;
import com.damian.domain.user.UserRepository;
import com.damian.dto.OrderDto;
import com.damian.domain.order.exceptions.OrderStatusException;
import com.damian.security.SecurityUtils;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.stream.Collectors;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

@Service
public class OrderService {

    private static final int ORDER_STATUS_ID_NOWE = 1;
    private static final int ORDER_STATUS_ID_W_TRAKCIE_REALIZACJI = 6;
    private static final int ORDER_STATUS_ID_USUNIETE = 99;
    private final OrderDao orderDao;
    private final CustomerDao customerDao;
    private final AddressDao addressDao;
    private final ProductDao productDao;
    private final DbFileDao dbFileDao;
    private final CustomCustomerDao customCustomerDao;
    private final UserRepository userRepository;
    private final BasketDao basketDao;
    private final PointsDao pointsDao;
    private final ZipCodeDao zipCodeDao;

    OrderService(BasketDao basketDao, UserRepository userRepository, OrderDao orderDao, CustomCustomerDao customCustomerDao, CustomerDao customerDao, AddressDao addressDao, ProductDao productDao, DbFileDao dbFileDao, PointsDao pointsDao, ZipCodeDao zipCodeDao) {
        this.orderDao = orderDao;
        this.customerDao = customerDao;
        this.addressDao = addressDao;
        this.productDao = productDao;
        this.dbFileDao = dbFileDao;
        this.customCustomerDao = customCustomerDao;
        this.userRepository = userRepository;
        this.basketDao = basketDao;
        this.pointsDao = pointsDao;
        this.zipCodeDao = zipCodeDao;
    }

    @Transactional
    public void createOrUpdateOrder(Order order, Integer statusId) throws OrderStatusException {
        if (!Objects.isNull(order.getOrderId())) {
            //TODO
            if (order.getOrderStatus().getOrderStatusId() == ORDER_STATUS_ID_W_TRAKCIE_REALIZACJI) {
                Order currentOrderState = orderDao.findByOrderId(order.getOrderId());
                order.setOrderItems(currentOrderState.getOrderItems());
            }
            if (order.getOrderStatus().getOrderStatusId() == ORDER_STATUS_ID_NOWE) {
                order.getOrderItems().forEach(orderItem -> orderItem.setQuantityFromSurplus(0));
            }
            performChangeOrderStatusOperation(order, statusId);
        } else {
            order.getOrderItems().forEach(orderItem -> orderItem.setQuantityFromSurplus(0));
            order.setAllreadyComputedPoints(false);
        }
        if (order.getCustomer().getCustomerId() != null) {
            performOrderCustomerFromDB(order);
        } else {
            performOrderWithNewCustomer(order);
        }
        assignProvince(order.getAddress());
        // pushNotificationForNewOrder(order);
    }

    private void performChangeOrderStatusOperation(Order order, Integer statusId) throws OrderStatusException {
        Integer prevOrderStatus = order.getOrderStatus().getOrderStatusId();
        Integer newOrderStatus;
        if(statusId ==null){
            newOrderStatus = order.getOrderStatus().getOrderStatusId();
        }else{
            newOrderStatus = statusId;
            order.setOrderStatus(new OrderStatus(statusId));
        }

        if (prevOrderStatus == ORDER_STATUS_ID_NOWE && newOrderStatus == ORDER_STATUS_ID_W_TRAKCIE_REALIZACJI) {
            changeStockStatusNoweToWtrakcie(order);
        }
        if (prevOrderStatus == ORDER_STATUS_ID_W_TRAKCIE_REALIZACJI && newOrderStatus == ORDER_STATUS_ID_NOWE) {
            System.out.println(ANSI_YELLOW + "zmiana na nowe" + ANSI_RESET);
            Order currentStateOfOrder = orderDao.findByOrderId(order.getOrderId());
            List<OrderItem> orderItemsTmp = currentStateOfOrder.getOrderItems();
            for (OrderItem orderItem : orderItemsTmp) {
                if (orderItem.getStateOnProduction() > 0 || orderItem.getStateOnLogistics() > 0 || orderItem.getStateOnWarehouse() > 0) {
                    throw new OrderStatusException("brak możliwości zmiany statusu z 'w trakcie realizacji' na " + "'nowy'" + " z powodu rozpoczęcia realizacji");
                }
            }
            changeStockStatusWtrakcieToNowe(order);
        }
    }

    private void changeStockStatusWtrakcieToNowe(Order order) {
        order.getOrderItems().forEach(orderItem -> {
            int specificBasketQuantity = orderItem.getQuantity();
            orderItem.getBasket().getBasketItems().forEach(basketItems -> {
                int numberOfProductsInOneSpecifiedBasket = basketItems.getQuantity();
                productDao.updateStockTmpMinus(basketItems.getProduct().getId(), (long) (specificBasketQuantity * numberOfProductsInOneSpecifiedBasket));
            });
        });
    }

    @Transactional
    public void createOrderFromCopy(Order order) throws OrderStatusException {
        order.getOrderItems().forEach(orderItem -> orderItem.setQuantityFromSurplus(0));
        if (order.getCustomer().getCustomerId() != null) {
            performOrderCustomerFromDB(order);
        } else {
            performOrderWithNewCustomer(order);
        }
        // pushNotificationForCombinedOrder(order);
        // pushNotificationForNewOrder(order);
    }

    private void performOrderCustomerFromDB(Order order) {
        Customer customerToSave = order.getCustomer();
        if (order.getAddress().getAddressId() == null) {
            Address savedAddress = addressDao.save(order.getAddress());
            order.setAddress(savedAddress);
        }
        Optional<Customer> findCustomer = customCustomerDao.findExacCustomerByEntity(customerToSave);
        if (findCustomer.isPresent()) {
            Customer savedCustomer = customerDao.save(customerToSave);
            order.setCustomer(savedCustomer);
            orderDao.saveAndFlush(order);
        } else {
            List<Customer> cust = customCustomerDao.findCustomerByCriteria(customerToSave);
            if (cust.size() > 0) {
                customerToSave = cust.get(0);
                customerToSave.setAdditionalInformation(order.getCustomer().getAdditionalInformation());
            } else {
                customerToSave.setCustomerId(null);
            }
            Customer savedCustomer = customerDao.saveAndFlush(customerToSave);
            order.setCustomer(savedCustomer);
            orderDao.saveAndFlush(order);
        }
    }

    @Transactional
    public void cancelOrder(Order order) {
        Order currOrder = orderDao.findByOrderId(order.getOrderId());
        if (currOrder.getOrderStatus().getOrderStatusId() == ORDER_STATUS_ID_NOWE) {
            currOrder.setOrderStatus(new OrderStatus(ORDER_STATUS_ID_USUNIETE));
        } else if (currOrder.getOrderStatus().getOrderStatusId() == ORDER_STATUS_ID_W_TRAKCIE_REALIZACJI) {
            currOrder.setOrderStatus(new OrderStatus(ORDER_STATUS_ID_USUNIETE));
            order.getOrderItems().forEach(orderItem -> basketDao.addBasketToStock(orderItem.getBasket().getBasketId(), orderItem.getStateOnProduction()));
            order.getOrderItems().forEach(orderItem -> orderItem.getBasket().getBasketItems().forEach(basketItems -> {
                productDao.updateStock(basketItems.getProduct().getId(), orderItem.getStateOnWarehouse() * basketItems.getQuantity());
            }));
            currOrder.getOrderItems().forEach(orderItem -> {
                int specificBasketQuantity = orderItem.getQuantity();
                orderItem.getBasket().getBasketItems().forEach(basketItems -> {
                    int numberOfProductsInOneSpecifiedBasket = basketItems.getQuantity();
                    productDao.updateStockTmpMinus(basketItems.getProduct().getId(), (long) (specificBasketQuantity * numberOfProductsInOneSpecifiedBasket) - (orderItem.getStateOnWarehouse() * basketItems.getQuantity()));
                });
            });
        }
    }

    private void pushNotificationForCombinedOrder(Order order) {
        List<SseEmitter> sseEmitterListToRemove = new ArrayList<>();
        NotificationsController.emitters.forEach((SseEmitter emitter) -> emit(order, sseEmitterListToRemove, emitter));
        NotificationsController.emitters.removeAll(sseEmitterListToRemove);
    }

    private void pushNotificationForNewOrder(Order order) {
        List<SseEmitter> sseEmitterListToRemove = new ArrayList<>();
        NotificationsController.newOrderEmitters.forEach((SseEmitter newOrderEmitter) -> emit(order, sseEmitterListToRemove, newOrderEmitter));
        NotificationsController.newOrderEmitters.removeAll(sseEmitterListToRemove);
    }

    private void emit(Order order, List<SseEmitter> sseEmitterListToRemove, SseEmitter emitter) {
        try {
            String productionUserLogin;
            if (order.getProductionUser() == null) {
                productionUserLogin = "new";
            } else {
                productionUserLogin = order.getProductionUser().getLogin();
            }
            emitter.send(productionUserLogin, MediaType.APPLICATION_JSON);
        } catch (Exception e) {
            emitter.complete();
            sseEmitterListToRemove.add(emitter);
        }
    }

    @Transactional
    void changeStockStatusNoweToWtrakcie(Order order) {
        order.getOrderItems().forEach(orderItem -> {
            int specificBasketQuantity = orderItem.getQuantity();
            Basket specificBasket = basketDao.findByBasketId(orderItem.getBasket().getBasketId());
            System.out.println(specificBasketQuantity);
            if (specificBasketQuantity <= specificBasket.getStock()) {
                orderItem.setQuantityFromSurplus(specificBasketQuantity);
                orderItem.setStateOnWarehouse(specificBasketQuantity);
                orderItem.setStateOnProduction(specificBasketQuantity);
                basketDao.minusBasketToStock(orderItem.getBasket().getBasketId(), specificBasketQuantity);
            } else {
                orderItem.getBasket().getBasketItems().forEach(basketItems -> {
                    int numberOfProductsInOneSpecifiedBasket = basketItems.getQuantity();
                    productDao.updateStockTmpAdd(basketItems.getProduct().getId(), (specificBasketQuantity * numberOfProductsInOneSpecifiedBasket));
                });
            }
        });
    }

    public void assignOrdersToSpecifiedProduction(List<Integer> ordersIds, Long productionId) throws OrderStatusException {
        List<Order> orderListByIds = orderDao.findByOrderIds(ordersIds);
        for (Order order : orderListByIds) {
            if (!SecurityUtils.isCurrentUserInRole("admin")) {
                if (order.getOrderStatus().getOrderStatusId() != ORDER_STATUS_ID_NOWE) {
                    throw new OrderStatusException("Nie można zmienić  lub przydzielić produkcji do zamówienia o " + "statusie innym niż NOWE");
                }
            }
        }
        orderDao.assignOrdersToSpecifiedProduction(ordersIds, productionId);
    }

    public List<OrderDto> getOrderStats() {
        List<Order> orderList = orderDao.findAllWithoutDeleted();
        List<OrderDto> orderDtoList = new ArrayList<>();
        orderList.forEach(order -> orderDtoList.add(new OrderDto(order.getOrderId(), order.getOrderFvNumber(), order.getCustomer(), order.getOrderDate(), order.getAdditionalInformation(), order.getDeliveryDate(), order.getWeekOfYear(), order.getDeliveryType(), order.getOrderStatus(), order.getOrderTotalAmount(), null, order.getOrderItems(), order.getAdditionalSale(), order.getAddress())));
        return orderDtoList;
    }

    public List<OrderDto> getOrderDaoByCustomer(Integer id) {
        List<Order> orderList = orderDao.findByCustomerId(id);
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<DbFile> dbFileDtoList = dbFileDao.findAll();
        List<OrderItem> oredrItemsList = new ArrayList<>();
        orderList.forEach(order -> {
            List<DbFile> result;
            result = dbFileDtoList.stream().filter(data -> order.getOrderId().equals(data.getOrderId())).collect(Collectors.toList());
            Long fileIdTmp;
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

    public OrderPageRequest getOrderDao(int page, int size, String text, String orderBy, int sortingDirection, List<Integer> orderStatusFilterArray, List<Integer> orderYearsFilterList, List<Integer> orderProductionUserFilterList, List<Integer> orderWeeksUserFilterList, List<String> provinces) {
        Sort.Direction sortDirection = sortingDirection == -1 ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Order> orderList;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, orderBy));
        orderList = orderDao.findAll((OrderSpecyficationJPA.getOrderWithOrderYearsFilter(orderYearsFilterList).and(OrderSpecyficationJPA.getOrderWithSearchFilter(text)).and(OrderSpecyficationJPA.getOrderWithProductionUserFilter(orderProductionUserFilterList)).and(OrderSpecyficationJPA.getOrderWithWeeksFilter(orderWeeksUserFilterList)).and(OrderSpecyficationJPA.getOrderWithProvincesFilter(provinces)).and(OrderSpecyficationJPA.getOrderWithOrderStatusFilter(orderStatusFilterArray))), pageable);
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<DbFile> dbFileDtoList = dbFileDao.findAll();
        orderList.forEach(order -> {
            List<DbFile> result;
            result = dbFileDtoList.stream().filter(data -> order.getOrderId().equals(data.getOrderId())).collect(Collectors.toList());
            Long fileIdTmp;
            if (result.size() > 0) {
                fileIdTmp = result.get(0).getFileId();
            } else {
                fileIdTmp = 0L;
            }
            orderDtoList.add(new OrderDto(order.getOrderId(), order.getOrderFvNumber(), order.getCustomer(), order.getOrderDate(), order.getAdditionalInformation(), order.getDeliveryDate(), order.getWeekOfYear(), order.getDeliveryType(), order.getOrderStatus(), order.getOrderTotalAmount(), fileIdTmp, order.getOrderItems(), order.getAdditionalSale(), order.getProductionUser(), order.getLoyaltyUser(), order.getAllreadyComputedPoints()));
        });
        return new OrderPageRequest(orderDtoList, orderList.getTotalElements());
    }

    private void performOrderWithNewCustomer(Order order) {
        if (order.getAddress().getAddressId() == null) {
            Address savedAddress = addressDao.save(order.getAddress());
            order.setAddress(savedAddress);
        }
        Customer savedCustomer = customerDao.saveAndFlush(order.getCustomer());
        order.setCustomer(savedCustomer);
        orderDao.save(order);
    }

    public List<OrderDto> getOrderDtoForCurrentProductionUser() {
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (currentUser.isPresent()) {
            List<Order> ordersList = orderDao.getAllOrdersByProductionUserId(currentUser.get().getId());
            return getOrderDtoListFromOrderList(ordersList);
        } else {
            throw new AccessDeniedException("Access Denied");
        }
    }

    private void assignProvince(Address address) {
        if (address.getZipCode().isEmpty()) {
            address.setProvince(null);
        } else {
            List<ZipCode> zipTmp = zipCodeDao.findByZipCodeCode(address.getZipCode());
            if (zipTmp.isEmpty()) {
                address.setProvince(null);
            } else {
                address.setProvince(zipTmp.get(0).getProvince());
                addressDao.save(address);
            }
        }
    }

    private List<OrderDto> getOrderDtoListFromOrderList(List<Order> ordersList) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<DbFile> dbFileDtoList = dbFileDao.findAll();
        ordersList.forEach(order -> {
            List<DbFile> result;
            result = dbFileDtoList.stream().filter(data -> order.getOrderId().equals(data.getOrderId())).collect(Collectors.toList());
            Long fileIdTmp;
            if (result.size() > 0) {
                fileIdTmp = result.get(0).getFileId();
            } else {
                fileIdTmp = 0L;
            }
            orderDtoList.add(new OrderDto(order.getOrderId(), order.getOrderFvNumber(), order.getCustomer(), order.getOrderDate(), order.getAdditionalInformation(), order.getDeliveryDate(), order.getWeekOfYear(), order.getDeliveryType(), order.getOrderStatus(), order.getOrderTotalAmount(), fileIdTmp, order.getOrderItems(), order.getAdditionalSale(), order.getProductionUser()));
        });
        return orderDtoList;
    }

    public void computeLoyaltyProgramPoints() {
        List<PointScheme> pointScheme = pointsDao.findBy();
        List<Order> orderListToComputePoints = orderDao.findAllOrderForLoyaltyProgram();
        for (Order order : orderListToComputePoints) {
            Double pointsToAdd = 0.0;
            for (OrderItem oi : order.getOrderItems()) {
                BasketSezon bs = oi.getBasket().getBasketSezon();
                if (bs != null) {
                    PointScheme scheme = pointScheme.stream().filter(pointScheme1 -> bs.getBasketSezonId().equals(pointScheme1.getBasketSezon().getBasketSezonId())).findAny().orElse(null);
                    if (scheme != null) {
                        Double stepA = (oi.getBasket().getBasketTotalPrice().doubleValue() / 100) * oi.getQuantity();
                        Double stepB = (stepA / scheme.getStep().doubleValue()) * scheme.getPoints().doubleValue();
                        pointsToAdd += stepB;
                    }
                }
            }
            Integer tmpPoints = (int) Math.ceil(pointsToAdd);
            userRepository.updateUserPoints(order.getLoyaltyUser().getLogin(), tmpPoints);
            order.setAllreadyComputedPoints(true);
            orderDao.save(order);
        }
    }
}



