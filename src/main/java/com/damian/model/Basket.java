package com.damian.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "baskets")
public class Basket {
    private Long basketId;
    private String basketName;
    private BasketType basketType;
    private List<BasketItems> basketItems;
    private Integer basketTotalPrice;


    @Basic
    @Column(name = "basket_name", nullable = false, length = 100)
    public String getBasketName() {
        return basketName;
    }

    public void setBasketName(String basketName) {
        this.basketName = basketName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_id", nullable = false)
    public Long getBasketId() {
        return basketId;
    }

    public void setBasketId(Long basketId) {
        this.basketId = basketId;
    }


    @ManyToOne
    @JoinColumn(name = "basket_type")
    public BasketType getBasketType() {
        return basketType;
    }

    public void setBasketType(BasketType basketType) {
        this.basketType = basketType;
    }

    @OneToMany(cascade = CascadeType.ALL,fetch =FetchType.EAGER)
    @JoinColumn(name="basket_id", referencedColumnName="basket_id")
    public List<BasketItems> getBasketItems() {
        return basketItems;
    }

    public void setBasketItems(List<BasketItems> basketItems) {
        this.basketItems = basketItems;
    }

    @Basic
    @Column(name = "basket_total_price", length = 40)
    public Integer getBasketTotalPrice() {
        return basketTotalPrice;
    }

    public void setBasketTotalPrice(Integer basketTotalPrice) {
        this.basketTotalPrice = basketTotalPrice;
    }
}
