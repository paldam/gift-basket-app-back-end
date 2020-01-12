package com.damian.domain.product;

import javax.persistence.*;

@Entity
@Table(name = "products_sub_types")
public class ProductSubType {

    private Integer subTypeId;
    private String subTypeName;
    private ProductType productType;


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "sub_type_id", nullable = false)
    public Integer getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(Integer subTypeId) {
        this.subTypeId = subTypeId;
    }

    @Basic
    @Column(name = "sub_type_name", nullable = false, length = 40)
    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }


    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name="product_type_id")
    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
