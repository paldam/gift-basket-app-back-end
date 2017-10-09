package com.damian.model;

import javax.persistence.*;

@Entity
@Table(name = "delivery_type")
public class DeliveryType {

    private Integer deliveryTypeId;
    private String deliveryTypeName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_type_id")
    public Integer getDeliveryTypeId() {
        return deliveryTypeId;
    }

    public void setDeliveryTypeId(Integer deliveryTypeId) {
        this.deliveryTypeId = deliveryTypeId;
    }

    @Basic
    @Column(name ="delivery_type_name" )
    public String getDeliveryTypeName() {
        return deliveryTypeName;
    }

    public void setDeliveryTypeName(String deliveryTypeName) {
        this.deliveryTypeName = deliveryTypeName;
    }

    @Override
    public String toString() {
        return "DeliveryType{" +
                "deliveryTypeId=" + deliveryTypeId +
                ", deliveryTypeName='" + deliveryTypeName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeliveryType that = (DeliveryType) o;

        if (getDeliveryTypeId() != null ? !getDeliveryTypeId().equals(that.getDeliveryTypeId()) : that.getDeliveryTypeId() != null)
            return false;
        return getDeliveryTypeName() != null ? getDeliveryTypeName().equals(that.getDeliveryTypeName()) : that.getDeliveryTypeName() == null;
    }

    @Override
    public int hashCode() {
        int result = getDeliveryTypeId() != null ? getDeliveryTypeId().hashCode() : 0;
        result = 31 * result + (getDeliveryTypeName() != null ? getDeliveryTypeName().hashCode() : 0);
        return result;
    }
}
