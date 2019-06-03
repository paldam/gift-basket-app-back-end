
package com.damian.domain.order;

        import com.damian.domain.basket.Basket;
        import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
        import org.hibernate.envers.Audited;

        import javax.persistence.*;

        import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Audited
@Table(name = "order_items")
public class OrderItem {
    private Integer orderItemId;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Basket basket;
    private Integer quantity;
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
    @Column(name = "production_state", nullable = false,columnDefinition = "INT DEFAULT 0")
    public Integer getStateOnProduction() {
        return stateOnProduction;
    }

    public void setStateOnProduction(Integer stateOnProduction) {
        this.stateOnProduction = stateOnProduction;
    }


    @Basic
    @Column(name = "warehouse_state", nullable = false,columnDefinition = "INT DEFAULT 0")
    public Integer getStateOnWarehouse() {
        return stateOnWarehouse;
    }

    public void setStateOnWarehouse(Integer stateOnWarehouse) {
        this.stateOnWarehouse = stateOnWarehouse;
    }

    @Basic
    @Column(name = "logistics_state", nullable = false,columnDefinition = "INT DEFAULT 0")
    public Integer getStateOnLogistics() {
        return stateOnLogistics;
    }

    public void setStateOnLogistics(Integer stateOnLogistics) {
        this.stateOnLogistics = stateOnLogistics;
    }
}
