package com.damian.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Damian on 05.09.2017.
 */
@Entity
@Table(name = "products_types")
public class ProductType {
    private Integer typeId;
    private String typeName;
    private List<ProductSubType> subTypeList;


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "type_id", nullable = false)
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    @Basic
    @Column(name = "type_name", nullable = false, length = 40)
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

@JsonIgnore
    @OneToMany(mappedBy="productType")
    public List<ProductSubType> getSubTypeList() {
        return subTypeList;
    }

    public void setSubTypeList(List<ProductSubType> subTypeList) {
        this.subTypeList = subTypeList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductType that = (ProductType) o;

        if (typeId != null ? !typeId.equals(that.typeId) : that.typeId != null) return false;
        if (typeName != null ? !typeName.equals(that.typeName) : that.typeName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = typeId != null ? typeId.hashCode() : 0;
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductType{" + "typeId=" + typeId + ", typeName='" + typeName + '\'' + '}';
    }
}
