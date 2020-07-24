package com.damian.domain.order;

import com.damian.domain.basket.Basket;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.envers.Audited;
import javax.persistence.*;
import java.util.Objects;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Audited
@Table(name = "order_items")
public class OrderItem {

    private Integer orderItemId;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Basket basket;
    private Integer quantity;
    private Integer quantityFromSurplus;
    private Integer stateOnProduction;
    private Integer stateOnWarehouse;
    private Integer stateOnLogistics;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id", nullable = false)
    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    @ManyToOne
    @JoinColumn(name = "basket_id")
    @Audited(targetAuditMode = NOT_AUDITED)
    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    @Basic
    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "quantity_from_surplus", columnDefinition = "INT DEFAULT 0")
    public Integer getQuantityFromSurplus() {
        return quantityFromSurplus;
    }

    public void setQuantityFromSurplus(Integer quantityFromSurplus) {
        this.quantityFromSurplus = quantityFromSurplus;
    }

    @Basic
    @Column(name = "production_state", columnDefinition = "INT DEFAULT 0")
    public Integer getStateOnProduction() {
        return stateOnProduction;
    }

    public void setStateOnProduction(Integer stateOnProduction) {
        this.stateOnProduction = stateOnProduction;
    }

    @Basic
    @Column(name = "warehouse_state", columnDefinition = "INT DEFAULT 0")
    public Integer getStateOnWarehouse() {
        return stateOnWarehouse;
    }

    public void setStateOnWarehouse(Integer stateOnWarehouse) {
        this.stateOnWarehouse = stateOnWarehouse;
    }

    @Basic
    @Column(name = "logistics_state", columnDefinition = "INT DEFAULT 0")
    public Integer getStateOnLogistics() {
        return stateOnLogistics;
    }

    public void setStateOnLogistics(Integer stateOnLogistics) {
        this.stateOnLogistics = stateOnLogistics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(orderItemId, orderItem.orderItemId) && Objects.equals(basket, orderItem.basket) && Objects.equals(quantity, orderItem.quantity) && Objects.equals(quantityFromSurplus, orderItem.quantityFromSurplus) && Objects.equals(stateOnProduction, orderItem.stateOnProduction) && Objects.equals(stateOnWarehouse, orderItem.stateOnWarehouse) && Objects.equals(stateOnLogistics, orderItem.stateOnLogistics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemId, basket, quantity, quantityFromSurplus, stateOnProduction, stateOnWarehouse,
            stateOnLogistics);
    }

    @Override
    public String toString() {
        return "OrderItem{" + "orderItemId=" + orderItemId + ", basket=" + basket + ", quantity=" + quantity + ", " +
            "quantityFromSurplus=" + quantityFromSurplus + ", stateOnProduction=" + stateOnProduction + ", " +
            "stateOnWarehouse=" + stateOnWarehouse + ", stateOnLogistics=" + stateOnLogistics + '}';
    }
}
