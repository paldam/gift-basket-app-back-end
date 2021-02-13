package com.damian.domain.product;

import java.util.Date;
import java.util.List;

public interface ProductToOrderDto {

    public Integer getId();
    public  String getProductName();
    public  Integer getStock();
    public  Integer getTmpOrdered();
    public  String getCapacity();
    public Date getLastNumberOfOrderedEditDate();
    public List<SupplierDto> getSuppliers();
   public ProductSubTypeDto getProductSubType();
    public  Integer getValueForDeliver();
    public  Long getSuma();


    interface SupplierDto{

        public Integer getSupplierId();
        public String getsupplierName();
    }

    interface  ProductSubTypeDto{
        public Integer getSubTypeId();
        public String getSubTypeName();
        public ProductTypeDto getProductType();
    }

    interface  ProductTypeDto{
        public Integer getTypeId();
        public String getTypeName();
    }



}
