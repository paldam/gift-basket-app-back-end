package com.damian.domain.basket;

import javax.persistence.*;

@Entity
@Table(name = "basket_sezon")
public class BasketSezon {

    private Integer basketSezonId;
    private String basketSezonName;
    private Boolean isActive;

    public BasketSezon() {
    }

    public BasketSezon(Integer basketSezonId) {
        this.basketSezonId = basketSezonId;
    }

    public BasketSezon(String basketSezonName) {
        this.basketSezonName = basketSezonName;
    }

    public BasketSezon(Integer basketSezonId, String basketSezonName) {
        this.basketSezonId = basketSezonId;
        this.basketSezonName = basketSezonName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_sezon_id", nullable = false)
    public Integer getBasketSezonId() {
        return basketSezonId;
    }

    public void setBasketSezonId(Integer basketSezonId) {
        this.basketSezonId = basketSezonId;
    }

    @Basic
    @Column(name = "basket_sezon_name", nullable = false, length = 100)
    public String getBasketSezonName() {
        return basketSezonName;
    }

    public void setBasketSezonName(String basketSezonName) {
        this.basketSezonName = basketSezonName;
    }


    @Basic
    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
