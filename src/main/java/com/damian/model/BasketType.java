package com.damian.model;

import javax.persistence.*;

@Entity
@Table(name = "basket_types")
public class BasketType {
    private Integer basketTypeId;
    private String basketTypeName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_type_id", nullable = false)
    public Integer getBasketTypeId() {
        return basketTypeId;
    }

    public void setBasketTypeId(Integer basketTypeId) {
        this.basketTypeId = basketTypeId;
    }

    @Basic
    @Column(name = "basket_type_name", nullable = false, length = 100)
    public String getBasketTypeName() {
        return basketTypeName;
    }

    public void setBasketTypeName(String basketTypeName) {
        this.basketTypeName = basketTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasketType that = (BasketType) o;

        if (basketTypeId != null ? !basketTypeId.equals(that.basketTypeId) : that.basketTypeId != null) return false;
        if (basketTypeName != null ? !basketTypeName.equals(that.basketTypeName) : that.basketTypeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = basketTypeId != null ? basketTypeId.hashCode() : 0;
        result = 31 * result + (basketTypeName != null ? basketTypeName.hashCode() : 0);
        return result;
    }
}
