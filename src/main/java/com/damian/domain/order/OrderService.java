package com.damian.domain.order;

import com.damian.boundry.rest.OrderController;
import com.damian.domain.basket.Basket;
import com.damian.domain.basket.BasketDao;
import com.damian.domain.basket.BasketItems;
import com.damian.domain.basket.BasketSezon;
import com.damian.domain.customer.*;
import com.damian.domain.order_file.DbFile;
import com.damian.domain.order_file.DbFileDao;
import com.damian.domain.prize.PointScheme;
import com.damian.domain.prize.PointsDao;
import com.damian.domain.product.ProductDao;
import com.damian.domain.product.SupplierDao;
import com.damian.domain.user.User;
import com.damian.domain.user.UserRepository;
import com.damian.domain.user.UserService;
import com.damian.dto.NumberProductsToChangeStock;
import com.damian.dto.OrderDto;
import com.damian.domain.order.exceptions.OrderStatusException;
import com.damian.security.SecurityUtils;
import org.apache.log4j.Logger;
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
    private BasketDao basketDao;
    private PointsDao pointsDao;

    OrderService(BasketDao basketDao, UserRepository userRepository, OrderDao orderDao, OrderItemDao orderItemDao, CompanyDao companyDao, CustomCustomerDao customCustomerDao, CustomerDao customerDao, AddressDao addressDao, ProductDao productDao, DbFileDao dbFileDao, SupplierDao supplierDao, PointsDao pointsDao) {
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
        this.basketDao = basketDao;
        this.pointsDao = pointsDao;
    }

    //TODO refactor  DRY
    public void pushNotificationForCombinedOrder(Order order) {
        List<SseEmitter> sseEmitterListToRemove = new ArrayList<>();
        OrderController.emitters.forEach((SseEmitter emitter) -> {
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
        });
        OrderController.emitters.removeAll(sseEmitterListToRemove);
    }

    //TODO refactor  DRY
    public void pushNotificationForNewOrder(Order order) {
        List<SseEmitter> sseEmitterListToRemove = new ArrayList<>();
        OrderController.newOrderEmitters.forEach((SseEmitter newOrderEmitter) -> {
            System.out.println(ANSI_YELLOW + "new" + ANSI_RESET);
            try {
                String productionUserLogin;
                if (order.getProductionUser() == null) {
                    productionUserLogin = "new";
                } else {
                    productionUserLogin = order.getProductionUser().getLogin();
                }
                newOrderEmitter.send(productionUserLogin, MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                newOrderEmitter.complete();
                sseEmitterListToRemove.add(newOrderEmitter);
            }
        });
        OrderController.newOrderEmitters.removeAll(sseEmitterListToRemove);
    }

    @Transactional
    public void createOrderFromCopy(Order order) throws OrderStatusException {
        order.getOrderItems().forEach(orderItem -> {
            orderItem.setQuantityFromSurplus(0);
        });
        if (order.getCustomer().getCustomerId() != null) {
            performOrderCustomerFromDB(order);
        } else {
            performOrderWithNewCustomer(order);
        }
        pushNotificationForCombinedOrder(order);
        pushNotificationForNewOrder(order);
    }

    @Transactional
    public void cancelOrder(Order order) {
        //todo
        Order currOrder = orderDao.findByOrderId(order.getOrderId());
        if (currOrder.getOrderStatus().getOrderStatusId() == 1) {
            currOrder.setOrderStatus(new OrderStatus(99));
        } else if (currOrder.getOrderStatus().getOrderStatusId() == 6) {
            currOrder.setOrderStatus(new OrderStatus(99));
            order.getOrderItems().forEach(orderItem -> {
                basketDao.addBasketToStock(orderItem.getBasket().getBasketId(), orderItem.getStateOnProduction());
            });
            order.getOrderItems().forEach(orderItem -> {
                orderItem.getBasket().getBasketItems().forEach(basketItems -> {
                    productDao.updateStock(basketItems.getProduct().getId(), orderItem.getStateOnWarehouse() * basketItems.getQuantity());
                });
            });
            currOrder.getOrderItems().forEach(orderItem -> {
                int specificBasketQuantity = orderItem.getQuantity();
                orderItem.getBasket().getBasketItems().forEach(basketItems -> {
                    int numberOfProductsInOneSpecifiedBasket = basketItems.getQuantity();
                    productDao.updateStockTmpMinus(basketItems.getProduct().getId(), Long.valueOf(specificBasketQuantity * numberOfProductsInOneSpecifiedBasket) - (orderItem.getStateOnWarehouse() * basketItems.getQuantity()));
                });
            });
        }
    }

    //todo
    @Transactional
    public void createOrUpdateOrder(Order order) throws OrderStatusException {
        if (!Objects.isNull(order.getOrderId())) {
            //TODO
            if (order.getOrderStatus().getOrderStatusId() == 6) {
                Order currentOrderState = orderDao.findByOrderId(order.getOrderId());
                order.setOrderItems(currentOrderState.getOrderItems());
            }
            if (order.getOrderStatus().getOrderStatusId() == 1) {
                order.getOrderItems().forEach(orderItem -> {
                    orderItem.setQuantityFromSurplus(0);
                });
            }
            performChangeOrderStatusOperation(order);
        } else {
            order.getOrderItems().forEach(orderItem -> {
                orderItem.setQuantityFromSurplus(0);
            });
            order.setAllreadyComputedPoints(false);
        }
        if (order.getCustomer().getCustomerId() != null) {
            performOrderCustomerFromDB(order);
        } else {
            performOrderWithNewCustomer(order);
        }
        pushNotificationForNewOrder(order);
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

    @Transactional
    public void changeStockStatusNoweToWtrakcie(Order order) {
        order.getOrderItems().forEach(orderItem -> {  // ustala czy dla konkretnego kosza z zmaówienia jest tyle koszy na stanie , jesli
            // tak to automatyczniedodaje progres M i P na tą ilosc
            int specificBasketQuantity = orderItem.getQuantity();
            Basket specificBasket = basketDao.findByBasketId(orderItem.getBasket().getBasketId());
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

    public void changeStockStatusWtrakcieToNowe(Order order) {
        List<OrderItem> tmp = order.getOrderItems();
        List<NumberProductsToChangeStock> numberProductsToChangeStocks = new ArrayList<>();
        order.getOrderItems().forEach(orderItem -> {
            int specificBasketQuantity = orderItem.getQuantity();
            Basket specificBasket = basketDao.findByBasketId(orderItem.getBasket().getBasketId());
            orderItem.getBasket().getBasketItems().forEach(basketItems -> {
                int numberOfProductsInOneSpecifiedBasket = basketItems.getQuantity();
                productDao.updateStockTmpMinus(basketItems.getProduct().getId(), Long.valueOf(specificBasketQuantity * numberOfProductsInOneSpecifiedBasket));
            });
        });
    }

    @Transactional
    public void changeOrderItemProgressOnSpecifiedPhase(Integer orderItemId, Long newStateValue, OrdersPreparePhase ordersPreparePhase) throws OrderStatusException {
        OrderItem currentOrderItemState = orderItemDao.findByOrderItemId(orderItemId);
        if (newStateValue > currentOrderItemState.getQuantity())
            throw new OrderStatusException("Brak możliwości zmiany, liczba nie może być większa od całkowitej liczby koszy ");
        if (newStateValue < 0) throw new OrderStatusException("Wartość nie może być mniejsza od zera");
        switch (ordersPreparePhase) {
            case ON_WAREHOUSE:
                changeOrderItemProgressOnWarehouse(orderItemId, currentOrderItemState, newStateValue);
                changeProductsStockWhenWarehouseUpdateOrderProgress(currentOrderItemState, newStateValue);
                break;
            case ON_PRODUCTION:
                changeOrderItemProgressOnProduction(orderItemId, currentOrderItemState, newStateValue);
                break;
            case ON_LOGISTICS:
                changeOrderItemProgressOnLogistics(orderItemId, currentOrderItemState, newStateValue);
                break;
        }
    }

    private void changeOrderItemProgressOnWarehouse(Integer orderItemId, OrderItem currentOrderItemState, Long newStateValue) throws OrderStatusException {
        if (newStateValue < currentOrderItemState.getStateOnProduction()) {
            throw new OrderStatusException("Stan koszy ukończonych przez magazyn nie może być mniejszy od liczby koszy przygotowanych przez produkcję ");
        }
        orderDao.changeSpecyfiedOrderItemProgressOnWarehouse(orderItemId, newStateValue);
    }

    private void changeOrderItemProgressOnProduction(Integer orderItemId, OrderItem currentOrderItemState, Long newStateValue) throws OrderStatusException {
        if (newStateValue > currentOrderItemState.getStateOnWarehouse()) {
            throw new OrderStatusException("Stan koszy ukończonych nie może być większy od liczby koszy przygotowanych przez magazyn ");
        }
        if (newStateValue < currentOrderItemState.getStateOnLogistics()) {
            throw new OrderStatusException("Stan koszy ukończonych przez produkcję nie może być mniejszy od liczby koszy przygotowanych do wysyłki przez logistykę");
        }
        orderDao.changeSpecyfiedOrderItemProgressOnProduction(orderItemId, newStateValue);
    }

    private void changeOrderItemProgressOnLogistics(Integer orderItemId, OrderItem currentOrderItemState, Long newStateValue) throws OrderStatusException {
        if (newStateValue > currentOrderItemState.getStateOnProduction()) {
            throw new OrderStatusException("Stan koszy ukończonych nie może być większy od liczby koszy przygotowanych przez produkcje ");
        }
        orderDao.changeSpecyfiedOrderItemProgressOnLogistics(orderItemId, newStateValue);
        Order orderTmp = orderDao.findOrderByOrderItemId(orderItemId.longValue());
        Boolean completeOrderWatch = true;
        for (OrderItem oi : orderTmp.getOrderItems()) {
            if (!oi.getQuantity().equals(oi.getStateOnLogistics())) {
                completeOrderWatch = false;
            }
        }
        if (completeOrderWatch) orderTmp.setOrderStatus(new OrderStatus(ORDER_STATUS_ID_ZREALIZOWANE));
        orderDao.save(orderTmp);
    }

    //TODO refact
    @Transactional
    public void changeOrderItemProgressOnSpecifiedPhaseByAddValue(Integer orderItemId, Long newStateValueToAdd, OrdersPreparePhase ordersPreparePhase) throws OrderStatusException {
        if (newStateValueToAdd < 0) throw new OrderStatusException("Wartość nie może być mniejsza od zera");
        OrderItem currentOrderItemState = orderItemDao.findByOrderItemId(orderItemId);
        Long newOrderStateTotal;
        if (ordersPreparePhase == OrdersPreparePhase.ON_WAREHOUSE) {
            newOrderStateTotal = currentOrderItemState.getStateOnWarehouse() + newStateValueToAdd;
            if (newOrderStateTotal > currentOrderItemState.getQuantity())
                throw new OrderStatusException("Brak możliwości zmiany, liczba nie może być większa od całkowitej liczby koszy ");
            if (newOrderStateTotal < currentOrderItemState.getStateOnProduction()) {
                throw new OrderStatusException("Stan koszy ukończonych przez magazyn nie może być mniejszy od liczby koszy przygotowanych przez produkcję ");
            }
            changeProductsStockWhenWarehouseUpdateOrderProgress(currentOrderItemState, newOrderStateTotal);
            orderDao.changeSpecifiedOrderItemProgressOnWarehouseByAddValue(orderItemId, newStateValueToAdd);
        } else if (ordersPreparePhase == OrdersPreparePhase.ON_PRODUCTION) {
            newOrderStateTotal = currentOrderItemState.getStateOnProduction() + newStateValueToAdd;
            if (newOrderStateTotal > currentOrderItemState.getQuantity())
                throw new OrderStatusException("Brak możliwości zmiany, liczba nie może być większa od całkowitej liczby koszy ");
            if (newOrderStateTotal > currentOrderItemState.getStateOnWarehouse())
                throw new OrderStatusException("Stan koszy ukończonych nie może być większy od liczby koszy przygotowanych przez magazyn ");
            if (newOrderStateTotal < currentOrderItemState.getStateOnLogistics())
                throw new OrderStatusException("Stan koszy ukończonych przez produkcję nie może być mniejszy od liczby koszy przygotowanych do wysyłki przez logistykę");
            orderDao.changeSpecifiedOrderItemProgressOnProductionByAddValue(orderItemId, newStateValueToAdd);
        } else if (ordersPreparePhase == OrdersPreparePhase.ON_LOGISTICS) {
            newOrderStateTotal = currentOrderItemState.getStateOnLogistics() + newStateValueToAdd;
            if (newOrderStateTotal > currentOrderItemState.getQuantity())
                throw new OrderStatusException("Brak możliwości zmiany, liczba nie może być większa od całkowitej liczby koszy ");
            if (newOrderStateTotal > currentOrderItemState.getStateOnProduction())
                throw new OrderStatusException("Stan koszy ukończonych nie może być większy od liczby koszy przygotowanych przez produkcje ");
            orderDao.changeSpecifiedOrderItemProgressOnLogisticsByAddValue(orderItemId, newStateValueToAdd);
        }
    }

    @Transactional
    public void changeOrderProgressByAdmin(Long id, List<OrderItem> orderItemsList) throws OrderStatusException {  // BY admin
        Order updatingOrder = orderDao.findByOrderId(id);
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
            if (oi.getStateOnLogistics() > oi.getStateOnWarehouse()) {
                throw new OrderStatusException("Stan koszy ukończonych nie może być większy od liczby koszy przygotowanych przez magazyn ");
            }
            if (!oi.getQuantity().equals(oi.getStateOnLogistics())) {
                completeOrderWatch = false;
            }
        }
        if (completeOrderWatch) updatingOrder.setOrderStatus(new OrderStatus(ORDER_STATUS_ID_ZREALIZOWANE));
        changeProductsStockWhenWarehouseUpdateOrderProgressByAdmin(id, orderItemsList);
        updatingOrder.setOrderItems(orderItemsList);
        orderDao.save(updatingOrder);
    }

    @Transactional
    public void changeProductsStockWhenWarehouseUpdateOrderProgressByAdmin(Long orderId, List<OrderItem> orderItemsListToChange) {
        List<OrderItem> currentOrderItemsState = orderDao.findByOrderId(orderId).getOrderItems();
        for (int i = 0; i < currentOrderItemsState.size(); i = i + 1) {
            if (currentOrderItemsState.get(i).getQuantityFromSurplus() == 0) {
                Long v1 = orderItemsListToChange.get(i).getStateOnWarehouse().longValue() - currentOrderItemsState.get(i).getStateOnWarehouse();
                currentOrderItemsState.get(i).getBasket().getBasketItems().forEach(basketItems -> {
                    productDao.updateStockMinus(basketItems.getProduct().getId(), v1 * basketItems.getQuantity());
                    productDao.updateStockTmpMinus(basketItems.getProduct().getId(), v1 * basketItems.getQuantity());
                });
            }
        }
    }

    private void changeProductsStockWhenWarehouseUpdateOrderProgress(OrderItem currentOrderItemState, Long newWarehouseStateValue) {
        if (currentOrderItemState.getQuantityFromSurplus() == 0) {
            Long v1 = newWarehouseStateValue - currentOrderItemState.getStateOnWarehouse();  //possible negative value
            currentOrderItemState.getBasket().getBasketItems().forEach(basketItems -> {
                productDao.updateStockMinus(basketItems.getProduct().getId(), v1 * basketItems.getQuantity());
                productDao.updateStockTmpMinus(basketItems.getProduct().getId(), v1 * basketItems.getQuantity());
            });
        }
    }

    public void assignOrdersToSpecifiedProduction(List<Integer> ordersIds, Long productionId) throws OrderStatusException {
        List<Order> orderListByIds = orderDao.findByOrderIds(ordersIds);
        for (Order order : orderListByIds) {
            if (!SecurityUtils.isCurrentUserInRole("admin")) {
                if (order.getOrderStatus().getOrderStatusId() != ORDER_STATUS_ID_NOWE) {
                    throw new OrderStatusException("Nie można zmienić  lub przydzielić produkcji do zamówienia o statusie innym niż NOWE");
                }
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
            orderDtoList.add(new OrderDto(order.getOrderId(), order.getOrderFvNumber(), order.getCustomer(), order.getOrderDate(), order.getAdditionalInformation(), order.getDeliveryDate(), order.getWeekOfYear(), order.getDeliveryType(), order.getOrderStatus(), order.getOrderTotalAmount(), fileIdTmp, order.getOrderItems(), order.getAdditionalSale(), order.getProductionUser(), order.getLoyaltyUser(), order.getAllreadyComputedPoints()));
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

    public List<OrderDto> getOrderDtoForCurrentProductionUser() {
        Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (currentUser.isPresent()) {
            List<Order> ordersList = orderDao.getAllOrdersByProductionUserId(currentUser.get().getId());
            return getOrderDtoListFromOrderList(ordersList);
        } else {
            throw new AccessDeniedException("Access Denied");
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



