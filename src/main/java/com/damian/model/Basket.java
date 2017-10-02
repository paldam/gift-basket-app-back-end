package com.damian.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "baskets")
public class Basket {
    private Integer basketId;
    private String basketName;
    private BasketType basketType;
    private List<BasketItems> basketItems;


    @Basic
    @Column(name = "basket_name", nullable = false, length = 40)
    public String getBasketName() {
        return basketName;
    }

    public void setBasketName(String basketName) {
        this.basketName = basketName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_id", nullable = false)
    public Integer getBasketId() {
        return basketId;
    }

    public void setBasketId(Integer basketId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Basket basket = (Basket) o;

        if (getBasketName() != null ? !getBasketName().equals(basket.getBasketName()) : basket.getBasketName() != null)
            return false;
        if (getBasketId() != null ? !getBasketId().equals(basket.getBasketId()) : basket.getBasketId() != null)
            return false;
        return getBasketType() != null ? getBasketType().equals(basket.getBasketType()) : basket.getBasketType() == null;
    }

    @Override
    public int hashCode() {
        int result = getBasketName() != null ? getBasketName().hashCode() : 0;
        result = 31 * result + (getBasketId() != null ? getBasketId().hashCode() : 0);
        result = 31 * result + (getBasketType() != null ? getBasketType().hashCode() : 0);
        return result;
    }
}
