package com.damian.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "products_types")
public class ProductType {

    private Integer typeId;
    private String typeName;
    private List<ProductSubType> subTypeList;
    private Boolean isActiveForPdfExport;

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
    @OneToMany(mappedBy = "productType")
    public List<ProductSubType> getSubTypeList() {
        return subTypeList;
    }

    public void setSubTypeList(List<ProductSubType> subTypeList) {
        this.subTypeList = subTypeList;
    }

    @Basic
    @Column(name = "is_active_for_pdf_export", nullable = false, length = 40, columnDefinition = "true")
    public Boolean getActiveForPdfExport() {
        return isActiveForPdfExport;
    }

    public void setActiveForPdfExport(Boolean activeForPdfExport) {
        isActiveForPdfExport = activeForPdfExport;
    }
}


