package com.damian.boundry.rest;

import com.damian.domain.audit.OrderAuditedRevisionEntity;
import com.damian.domain.basket.BasketDao;
import com.damian.domain.notification.NotificationService;
import com.damian.domain.order.*;
import com.damian.domain.order.exceptions.OrderStatusException;
import com.damian.domain.product.ProductToOrderDto;
import com.damian.dto.NumberOfBasketOrderedByDate;
import com.damian.dto.OrderDto;
import com.damian.dto.OrderItemsDto;
import com.damian.util.PdfOrderProductCustom;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import javax.persistence.EntityManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

@Transactional
@CrossOrigin
@RestController
public class OrderController {

    @Autowired
    private EntityManagerFactory factory;
    private OrderDao orderDao;
    private OrderService orderService;
    private OrderStatusDao orderStatusDao;
    private NotificationService notificationService;
    private OrderProgressService orderProgressService;
    private OrderItemDao orderItemDao;
    private BasketDao basketDao;

    OrderController(BasketDao basketDao, OrderItemDao orderItemDao,NotificationService notificationService, OrderDao orderDao, OrderService orderService,
                    OrderStatusDao orderStatusDao,OrderProgressService orderProgressService) {
        this.basketDao = basketDao;
        this.orderItemDao= orderItemDao;
        this.orderDao = orderDao;
        this.orderService = orderService;
        this.orderStatusDao = orderStatusDao;
        this.notificationService = notificationService;
        this.orderProgressService = orderProgressService;
    }

    @GetMapping(value = "/order/{id}")
    ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return Optional.ofNullable(orderDao.findByOrderId(id))
            .map(order -> ResponseEntity.ok().body(order))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/orders")
    ResponseEntity<List<Order>> getOrders() {
        List<Order> ordersList = orderDao.findAllWithoutDeleted();
        return new ResponseEntity<>(ordersList, HttpStatus.OK);
    }

    @GetMapping("/orders/full_product_availability")
    ResponseEntity<List<Order>> getOrdersWithFullProductAvailability() {
        List<Order> ordersList = orderService.findOrderWithFullProductAvailability();
        return new ResponseEntity<>(ordersList, HttpStatus.OK);
    }

    @GetMapping(value = "/orderhistory/{id}")
    ResponseEntity<List<Order>> getOrderHistory(@PathVariable Long id) {
        AuditReader auditReader = AuditReaderFactory.get(factory.createEntityManager());
        AuditQuery query = auditReader.createQuery().forEntitiesModifiedAtRevision(Order.class, id);
        List<Order> orderTmp = (List<Order>) query.getResultList();
        return new ResponseEntity<>(orderTmp, HttpStatus.OK);
    }

    @GetMapping(value = "/order_history_prev_rev/{id}")
    ResponseEntity<List<Order>> getPreviousVersionOfOrderHistory(@PathVariable Long id) {
        AuditReader auditReader = AuditReaderFactory.get(factory.createEntityManager());
        Optional<BigInteger> revNumber = orderDao.getRevisionNumberOFfPreviousOrderState(id);
        BigInteger orderRevisionToGet;
        orderRevisionToGet = revNumber.orElseGet(() -> BigInteger.valueOf(id));
        AuditQuery query = auditReader
            .createQuery()
            .forEntitiesModifiedAtRevision(Order.class, orderRevisionToGet.longValue());
        List<Order> orderTmp = (List<Order>) query.getResultList();
        return new ResponseEntity<>(orderTmp, HttpStatus.OK);
    }

    @GetMapping(value = "/orderitemshistory/{id}")
    ResponseEntity<List<OrderItem>> getOrderItemsHistory(@PathVariable Long id) {
        AuditReader auditReader = AuditReaderFactory.get(factory.createEntityManager());
        Optional<BigInteger> revNumber = orderDao.getRevisionNumberOFfPreviousOrderState(id);
        BigInteger orderRevisionToGet;
        orderRevisionToGet = revNumber.orElseGet(() -> BigInteger.valueOf(id));
        AuditQuery query = auditReader.createQuery().forEntitiesModifiedAtRevision(OrderItem.class,
            orderRevisionToGet.longValue());
        List<OrderItem> orderTmp = (List<OrderItem>) query.getResultList();
        return new ResponseEntity<>(orderTmp, HttpStatus.OK);
    }

    @GetMapping("/order/audit/{id}")
    ResponseEntity<List<OrderAuditedRevisionEntity>> getOrderAudit(@PathVariable Integer id) {
        List<Object[]> orderHistoryListTmp = orderDao.getOrderHistoryById(id);
        List<OrderAuditedRevisionEntity> orderAuditedRevisionEntitiesList = new ArrayList<>();
        orderHistoryListTmp.forEach(objects -> orderAuditedRevisionEntitiesList
            .add(new OrderAuditedRevisionEntity(
                ((BigInteger) objects[0]).longValue(),
                ((BigInteger) objects[1]).longValue(),
                ((BigInteger) objects[3]).longValue(),
                (String) objects[2])));
        return new ResponseEntity<>(orderAuditedRevisionEntitiesList, HttpStatus.OK);
    }

    @GetMapping("/orders/production")
    ResponseEntity<List<OrderDto>> getOrdersForProduction() {
        try {
            List<OrderDto> orderDtoList = orderService.getOrderDtoForCurrentProductionUser();
            return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
        } catch (AccessDeniedException accessDeniedException) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/orders/daterange")
    ResponseEntity<List<Order>> getOrdersByDateRange(
        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<Order> ordersList = orderDao.findOrdersByDateRange(startDate, endDate);
        return new ResponseEntity<>(ordersList, HttpStatus.OK);
    }

    @GetMapping("/order_status")
    ResponseEntity<List<OrderStatus>> getOrderStatus() {
        List<OrderStatus> ordersStatusList = orderStatusDao.findAllBy();
        return new ResponseEntity<>(ordersStatusList, HttpStatus.OK);
    }

    @GetMapping("/orders/products_to_order/daterange")
    ResponseEntity<List<ProductToOrderDto>> getProductsToOrder(
        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<ProductToOrderDto> productToOrderList = orderDao.findProductToOrder(startDate,endDate);
        return new ResponseEntity<>(productToOrderList, HttpStatus.OK);
    }

    @GetMapping("/orders/products_to_order_without_deleted_by_order_date/daterange")
    ResponseEntity<List<ProductToOrderDto>> getProductsToOrderWithoutDeletedByOrderDate(
        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = setEndOfDay(endDate);
        List<ProductToOrderDto> productToOrderList = orderDao.findProductToOrderWithoutDeletedOrderByOrderDate(startDate, endDate);
        return new ResponseEntity<>(productToOrderList, HttpStatus.OK);
    }

    @GetMapping("/order/statistic/orderdaterange")
    ResponseEntity<List<Order>> getOrdersByBasket(
        @RequestParam(value = "basketId") Long basketId,
        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = setEndOfDay(endDate);
        List<Order> orderList = orderDao.findAllOrderByBasketIdAndOrderDate(basketId, startDate, endDate);
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @GetMapping("/baskets/statistic/daterange")
    ResponseEntity<List<NumberOfBasketOrderedByDate>> getNumberOfBasketOrdered(
        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = setEndOfDay(endDate);
        List<NumberOfBasketOrderedByDate> basketList = orderDao.getNumberOfBasketOrdered(startDate, endDate);
        return new ResponseEntity<>(basketList, HttpStatus.OK);
    }

    @GetMapping("/baskets/statistic/orderdaterange")
    ResponseEntity<List<NumberOfBasketOrderedByDate>> getNumberOfBasketOrderedFilteredByOrderDate(
        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        endDate = setEndOfDay(endDate);
        List<NumberOfBasketOrderedByDate> basketList =
            orderDao.getNumberOfBasketOrderedFilteredByOrderDate(startDate, endDate);
        return new ResponseEntity<>(basketList, HttpStatus.OK);
    }

    @GetMapping("/orderdao")
    ResponseEntity<OrderPageRequest> getOrderDao(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size") int size,
        @RequestParam(value = "searchtext", required = false) String text,
        @RequestParam(value = "orderBy", required = false) String orderBy,
        @RequestParam(value = "sortingDirection", required = false, defaultValue = "1") int sortingDirection,
        @RequestParam(value = "orderStatusFilterList", required = false) List<Integer> orderStatusFilterList,
        @RequestParam(value = "orderYearsFilterList", required = false) List<Integer> orderYearsFilterList,
        @RequestParam(value = "orderProductionUserFilterList", required = false) List<Integer> orderProductionUserFilterList,
        @RequestParam(value = "orderWeeksFilterList", required = false) List<Integer> orderWeeksFilterList,
        @RequestParam(value = "provinces", required = false) List<String> provinces,
        @RequestParam(value = "deliveryTypeFilterList", required = false) List<Integer> deliveryTypeList

        ) {

        if (orderStatusFilterList == null) {
            orderStatusFilterList = new ArrayList<>();
        }
        if (orderYearsFilterList == null) {
            orderYearsFilterList = new ArrayList<>();
        }
        if (orderProductionUserFilterList == null) {
            orderProductionUserFilterList = new ArrayList<>();
        }
        if (orderWeeksFilterList == null) {
            orderWeeksFilterList = new ArrayList<>();
        }

        if (deliveryTypeList == null) {
            deliveryTypeList = new ArrayList<>();
        }


        OrderPageRequest orderDtoList = orderService.getOrderDao(page, size, text, orderBy, sortingDirection,
            orderStatusFilterList, orderYearsFilterList,orderProductionUserFilterList,orderWeeksFilterList,provinces,deliveryTypeList);
        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    @GetMapping("/ordercount")
    ResponseEntity<Long> getOrderCount() {
        long numberOfRows = orderDao.getCountOfAllOrdersWithoutDeleted();
        return new ResponseEntity<>(numberOfRows, HttpStatus.OK);
    }

    @GetMapping("/ordersyears")
    ResponseEntity<int[]> getOrderYears() {
        int[] years = orderDao.getOrdersYears();
        return new ResponseEntity<>(years, HttpStatus.OK);
    }

    @GetMapping("/orderstats")
    ResponseEntity<List<OrderDto>> getOrderStats() {
        List<OrderDto> orderDtoList = orderService.getOrderStats();
        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    @GetMapping("/order/customer/{id}")
    ResponseEntity<List<OrderDto>> getOrdersByCustomer(@PathVariable Integer id) {
        List<OrderDto> ordersList = orderService.getOrderDaoByCustomer(id);
        return new ResponseEntity<>(ordersList, HttpStatus.OK);
    }

    @PostMapping( value = "/order/assign_production",produces = "text/plain;charset=UTF-8")
    ResponseEntity assignOrdersToSpecifiedProduction(
        @RequestParam(value = "ordersIds") List<Integer> ordersIds,
        @RequestParam(value = "productionId") Long productionId) {
        try {
            orderService.assignOrdersToSpecifiedProduction(ordersIds, productionId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OrderStatusException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @GetMapping(value = "/order/compute_loytaly_points", produces = "text/plain;charset=UTF-8")
    ResponseEntity computeLoyaltyProgramPoints() {
        orderService.computeLoyaltyProgramPoints();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/orders")
    ResponseEntity createOrUpdateOrder(@RequestBody Order order){
        try {
            orderService.createOrUpdateOrder(order,null);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (OrderStatusException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @PostMapping("/order/cancel")
    ResponseEntity cancelOrder(@RequestBody Order orderToCancel) {
        orderService.cancelOrder(orderToCancel);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/order/copy/{originOrderIdCopy}")
    ResponseEntity copyOrderFromExistingOne(@RequestBody Order order,
                                            @PathVariable Long originOrderIdCopy){
        Order originOrder = orderDao.findByOrderId(originOrderIdCopy);
       // notificationService.saveNotifications(order, originOrder);
        try {
            orderService.createOrderFromCopy(order);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (OrderStatusException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @PostMapping(value = "/order/status/{id}/{statusId}", produces = "text/plain;charset=UTF-8")
    ResponseEntity changeOrderStatus(@PathVariable Long id, @PathVariable Integer statusId){
        Order updatingOrder = orderDao.findByOrderId(id);
        //OrderStatus updatingOrderNewStatus = new OrderStatus();
        //updatingOrderNewStatus.setOrderStatusId(statusId);
        //updatingOrder.setOrderStatus(updatingOrderNewStatus);
        try {
            orderService.createOrUpdateOrder(updatingOrder,statusId);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (OrderStatusException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @PostMapping(value = "/order/paymentstatus/{id}/{status}", produces = "text/plain;charset=UTF-8")
    ResponseEntity changePaymentStatus(@PathVariable Long id, @PathVariable Integer status){

        orderDao.changePaymentStatus(id,status);
            return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @PostMapping(value = "/order/progress/{id}", produces = "text/plain;charset=UTF-8")
    ResponseEntity changeOrderProgress(@PathVariable Long id, @RequestBody List<OrderItem> orderItems){
        try {
            orderProgressService.changeOrderProgressByAdmin(id, orderItems);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (OrderStatusException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @GetMapping(value = "/order/orderitem/progress/warehouse/{orderItemId}/{newStateValueOnWarehouse}",
        produces = "text/plain;charset=UTF-8")
    ResponseEntity changeSpecifiedOrderItemProgressOnWarehouseByNewValue(@PathVariable Integer orderItemId,
                                                                         @PathVariable Long newStateValueOnWarehouse){
        try {
            orderProgressService.changeOrderItemProgressOnSpecifiedPhase(
                orderItemId,newStateValueOnWarehouse,OrdersPreparePhase.ON_WAREHOUSE);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (OrderStatusException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @GetMapping(value = "/order/orderitem/progress/production/{orderItemId}/{newStateValueOnProduction}",
        produces = "text/plain;charset=UTF-8")
    ResponseEntity changeSpecifiedOrderItemProgressOnProductionByNewValue(@PathVariable Integer orderItemId,
                                                                          @PathVariable Long newStateValueOnProduction){
        try {
            orderProgressService.changeOrderItemProgressOnSpecifiedPhase(
                orderItemId,newStateValueOnProduction,OrdersPreparePhase.ON_PRODUCTION);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (OrderStatusException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @GetMapping(value = "/order/orderitem/progress/logistics/{orderItemId}/{newStateValueOnLogistics}",
        produces = "text/plain;charset=UTF-8")
    ResponseEntity changeSpecifiedOrderItemProgressOnLogisticsByNewValue(@PathVariable Integer orderItemId,
                                                                         @PathVariable Long newStateValueOnLogistics){
        try {
            orderProgressService.changeOrderItemProgressOnSpecifiedPhase(
                orderItemId, newStateValueOnLogistics,OrdersPreparePhase.ON_LOGISTICS);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (OrderStatusException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @GetMapping(value = "/order/orderitem/progress/warehouse/addvalue/{orderItemId}/{newStateValueToAddOnWarehouse}",
        produces = "text/plain;charset=UTF-8")
    ResponseEntity changeSpecifiedOrderItemProgressOnWarehouseByAddValue(
        @PathVariable Integer orderItemId,
        @PathVariable Long newStateValueToAddOnWarehouse) {
        try {
            orderProgressService.changeOrderItemProgressOnSpecifiedPhaseByAddValue(orderItemId,newStateValueToAddOnWarehouse,
                OrdersPreparePhase.ON_WAREHOUSE);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (OrderStatusException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @GetMapping(value = "/order/orderitem/progress/production/addvalue/{orderItemId}/{newStateValueToAddOnProduction}",
        produces = "text/plain;charset=UTF-8")
    ResponseEntity changeSpecifiedOrderItemProgressOnProductionByAddValue(
        @PathVariable Integer orderItemId,
        @PathVariable Long newStateValueToAddOnProduction) {
        try {
            orderProgressService.changeOrderItemProgressOnSpecifiedPhaseByAddValue(orderItemId,newStateValueToAddOnProduction,
                OrdersPreparePhase.ON_PRODUCTION);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (OrderStatusException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @GetMapping(value = "/order/orderitem/progress/logistics/addvalue/{orderItemId}/{newStateValueToAddOnLogistics}",
        produces = "text/plain;charset=UTF-8")
    ResponseEntity changeSpecifiedOrderItemProgressOnLogisticsByAddValue(
        @PathVariable Integer orderItemId,
        @PathVariable Long newStateValueToAddOnLogistics) {
        try {
            orderProgressService.changeOrderItemProgressOnSpecifiedPhaseByAddValue(
                orderItemId,newStateValueToAddOnLogistics,OrdersPreparePhase.ON_LOGISTICS);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (OrderStatusException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @DeleteMapping("/order/{id}")
    ResponseEntity deleteOrderPermanent(@PathVariable Long id) {
        orderDao.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(
        value = "/order/pdf/aaa/{orderId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getBasketProductsPdf(@RequestBody List<OrderItemsDto> orderItemsDto ,
                                                                    @PathVariable Long orderId) throws IOException {
        Order order = orderDao.findByOrderId(orderId);


        orderItemsDto.forEach(orderItemsDto1 -> {
            orderItemsDto1.setBasket(basketDao.findById(orderItemsDto1.getBasket().getBasketId()).get());
        });


        ByteArrayInputStream bis = PdfOrderProductCustom.generateBasketsProductsCustomListPdf(orderItemsDto,order);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=produkty_zamowienia.pdf");
        new InputStreamResource(bis);
        return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(bis));
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(112000000); //12MB
        return multipartResolver;
    }

    private Date setEndOfDay(Date date){
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return date;
    }
}
