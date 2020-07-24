package com.damian.domain.order;

import com.damian.domain.order.exceptions.OrderStatusException;
import com.damian.domain.product.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class OrderProgressService {

    public static final int ORDER_STATUS_ID_ZREALIZOWANE = 5;
    private OrderItemDao orderItemDao;
    private OrderDao orderDao;
    private ProductDao productDao;

    public OrderProgressService(OrderItemDao orderItemDao, OrderDao orderDao, ProductDao productDao) {
        this.orderItemDao = orderItemDao;
        this.orderDao = orderDao;
        this.productDao = productDao;
    }

    @Transactional
    public void changeOrderItemProgressOnSpecifiedPhase(
        Integer orderItemId, Long newStateValue, OrdersPreparePhase ordersPreparePhase) throws OrderStatusException {
        OrderItem currentOrderItemState = orderItemDao.findByOrderItemId(orderItemId);
        if (newStateValue > currentOrderItemState.getQuantity())
            throw new OrderStatusException("Bład, liczba nie może być większa od całkowitej liczby koszy");
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

    private void changeOrderItemProgressOnWarehouse(Integer orderItemId, OrderItem currentOrderItemState,
                                                    Long newStateValue) throws OrderStatusException {
        if (newStateValue < currentOrderItemState.getStateOnProduction()) {
            throw new OrderStatusException("Stan koszy ukończonych przez magazyn nie może być mniejszy od liczby " +
                "koszy przygotowanych przez produkcję ");
        }
        orderDao.changeSpecyfiedOrderItemProgressOnWarehouse(orderItemId, newStateValue);
    }

    private void changeOrderItemProgressOnProduction(Integer orderItemId, OrderItem currentOrderItemState,
                                                     Long newStateValue) throws OrderStatusException {
        if (newStateValue > currentOrderItemState.getStateOnWarehouse()) {
            throw new OrderStatusException("Stan koszy ukończonych nie może być większy od liczby koszy " +
                "przygotowanych przez magazyn ");
        }
        if (newStateValue < currentOrderItemState.getStateOnLogistics()) {
            throw new OrderStatusException("Stan koszy ukończonych przez produkcję nie może być mniejszy od liczby "
                + "koszy przygotowanych do wysyłki przez logistykę");
        }
        orderDao.changeSpecyfiedOrderItemProgressOnProduction(orderItemId, newStateValue);
    }

    private void changeOrderItemProgressOnLogistics(Integer orderItemId, OrderItem currentOrderItemState,
                                                    Long newStateValue) throws OrderStatusException {
        if (newStateValue > currentOrderItemState.getStateOnProduction()) {
            throw new OrderStatusException("Stan koszy ukończonych nie może być większy od liczby koszy " +
                "przygotowanych przez produkcje ");
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

    @Transactional
    public void changeOrderItemProgressOnSpecifiedPhaseByAddValue(
        Integer orderItemId, Long newStateValueToAdd, OrdersPreparePhase ordersPreparePhase) throws OrderStatusException{
        if (newStateValueToAdd < 0) throw new OrderStatusException("Wartość nie może być mniejsza od zera");
        OrderItem currentOrderItemState = orderItemDao.findByOrderItemId(orderItemId);
        Long newOrderStateTotal;
        if (ordersPreparePhase == OrdersPreparePhase.ON_WAREHOUSE) {
            newOrderStateTotal = currentOrderItemState.getStateOnWarehouse() + newStateValueToAdd;
            if (newOrderStateTotal > currentOrderItemState.getQuantity())
                throw new OrderStatusException("Bład, liczba nie może być większa od całkowitej liczby koszy ");
            if (newOrderStateTotal < currentOrderItemState.getStateOnProduction()) {
                throw new OrderStatusException("Stan koszy ukończonych przez magazyn nie może być mniejszy od liczby "
                    + "koszy przygotowanych przez produkcję ");
            }
            changeProductsStockWhenWarehouseUpdateOrderProgress(currentOrderItemState, newOrderStateTotal);
            orderDao.changeSpecifiedOrderItemProgressOnWarehouseByAddValue(orderItemId, newStateValueToAdd);
        } else if (ordersPreparePhase == OrdersPreparePhase.ON_PRODUCTION) {
            newOrderStateTotal = currentOrderItemState.getStateOnProduction() + newStateValueToAdd;
            if (newOrderStateTotal > currentOrderItemState.getQuantity())
                throw new OrderStatusException("Bład, liczba nie może być większa od całkowitejliczby koszy ");
            if (newOrderStateTotal > currentOrderItemState.getStateOnWarehouse())
                throw new OrderStatusException("Stan koszy ukończonych nie może być większy od liczby koszy " +
                    "przygotowanych przez magazyn ");
            if (newOrderStateTotal < currentOrderItemState.getStateOnLogistics())
                throw new OrderStatusException("Stan koszy ukończonych przez produkcję nie może być mniejszy od " +
                    "liczby koszy przygotowanych do wysyłki przez logistykę");
            orderDao.changeSpecifiedOrderItemProgressOnProductionByAddValue(orderItemId, newStateValueToAdd);
        } else if (ordersPreparePhase == OrdersPreparePhase.ON_LOGISTICS) {
            newOrderStateTotal = currentOrderItemState.getStateOnLogistics() + newStateValueToAdd;
            if (newOrderStateTotal > currentOrderItemState.getQuantity())
                throw new OrderStatusException("Bład, liczba nie może być większa od całkowitej liczby koszy ");
            if (newOrderStateTotal > currentOrderItemState.getStateOnProduction())
                throw new OrderStatusException("Stan koszy ukończonych nie może być większy od liczby koszy " +
                    "przygotowanych przez produkcje ");
            orderDao.changeSpecifiedOrderItemProgressOnLogisticsByAddValue(orderItemId, newStateValueToAdd);
        }
    }

    @Transactional
    public void changeOrderProgressByAdmin(Long id, List<OrderItem> orderItemsList) throws OrderStatusException {
        Order updatingOrder = orderDao.findByOrderId(id);
        if (updatingOrder.getOrderStatus().getOrderStatusId() == 1) {
            throw new OrderStatusException("Brak możliwości zmiany przy statusie zamowienia 'nowy' ");
        }
        Boolean completeOrderWatch = true;
        for (OrderItem oi : orderItemsList) {
            if (oi.getStateOnLogistics() > oi.getQuantity() || oi.getStateOnProduction() > oi.getQuantity() ||
                oi.getStateOnWarehouse() > oi.getQuantity()) {
                throw new OrderStatusException("Bład, liczba nie może być większa od całkowitej liczby koszy ");
            }
            if (oi.getStateOnLogistics() < 0 || oi.getStateOnProduction() < 0 || oi.getStateOnWarehouse() < 0) {
                throw new OrderStatusException("Wartość nie może być mniejsza od zera");
            }
            if (oi.getStateOnProduction() > oi.getStateOnWarehouse()) {
                throw new OrderStatusException("Stan koszy ukończonych nie może być większy od liczby koszy " +
                    "przygotowanych przez magazyn ");
            }
            if (oi.getStateOnLogistics() > oi.getStateOnProduction()) {
                throw new OrderStatusException("Stan koszy ukończonych nie może być większy od liczby koszy " +
                    "przygotowanych przez produkcje ");
            }
            if (oi.getStateOnLogistics() > oi.getStateOnWarehouse()) {
                throw new OrderStatusException("Stan koszy ukończonych nie może być większy od liczby koszy " +
                    "przygotowanych przez magazyn ");
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
    public void changeProductsStockWhenWarehouseUpdateOrderProgressByAdmin(Long orderId,
                                                                           List<OrderItem> orderItemsListToChange) {
        List<OrderItem> currentOrderItemsState = orderDao.findByOrderId(orderId).getOrderItems();
        for (int i = 0; i < currentOrderItemsState.size(); i = i + 1) {
            if (currentOrderItemsState.get(i).getQuantityFromSurplus() == 0) {
                Long v1 =
                    orderItemsListToChange.get(i).getStateOnWarehouse().longValue()
                        - currentOrderItemsState.get(i).getStateOnWarehouse();
                currentOrderItemsState.get(i).getBasket().getBasketItems().forEach(basketItems -> {
                    productDao.updateStockMinus(basketItems.getProduct().getId(), v1 * basketItems.getQuantity());
                    productDao.updateStockTmpMinus(basketItems.getProduct().getId(), v1 * basketItems.getQuantity());
                });
            }
        }
    }

    private void changeProductsStockWhenWarehouseUpdateOrderProgress(OrderItem currentOrderItemState,
                                                                     Long newWarehouseStateValue) {
        if (currentOrderItemState.getQuantityFromSurplus() == 0) {
            Long v1 = newWarehouseStateValue - currentOrderItemState.getStateOnWarehouse();
            currentOrderItemState.getBasket().getBasketItems().forEach(basketItems -> {
                productDao.updateStockMinus(basketItems.getProduct().getId(), v1 * basketItems.getQuantity());
                productDao.updateStockTmpMinus(basketItems.getProduct().getId(), v1 * basketItems.getQuantity());
            });
        }
    }
}
