package com.damian.domain.product;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Damian on 05.09.2017.
 */
@Entity
@Table(name = "products")
public class Product {

    private Integer id;
    private String capacity;
    private Integer price;
    private String productName;
    private Integer stock;
    private Integer tmpStock;
    private Integer tmpOrdered;
    private String deliver;
    private Integer isArchival;
    private Set<Supplier> suppliers;
    private Supplier supplier;
    //private ProductType productType;
    private ProductSubType productSubType;
    private Date lastStockEditDate;
    private Date lastNumberOfOrderedEditDate;

    @Basic
    @Column(name = "is_archival")
    public Integer getIsArchival() {
        return isArchival;
    }

    public void setIsArchival(Integer isArchival) {
        this.isArchival = isArchival;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "capacity")
    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    @Basic
    @Column(name = "price")
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    @Column(name = "product_name", nullable = false, length = 300)
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Basic
    @Column(name = "stock")
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Basic
    @Column(name = "tmp_stock")
    public Integer getTmpStock() {
        return tmpStock;
    }

    public void setTmpStock(Integer tmpStock) {
        this.tmpStock = tmpStock;
    }

    @Basic
    @Column(name = "tmp_ordered", columnDefinition = "INT DEFAULT 0")
    public Integer getTmpOrdered() {
        return tmpOrdered;
    }

    public void setTmpOrdered(Integer tmpOrdered) {
        this.tmpOrdered = tmpOrdered;
    }

//    @Basic
//    @Column(name = "deliver")
//    public String getDeliver() {
//        return deliver;
//    }
//
//    public void setDeliver(String deliver) {
//        this.deliver = deliver;
//    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_suppliers",
        joinColumns = {@JoinColumn(name = "id")},  //
        inverseJoinColumns = {@JoinColumn(name = "supplier_id")})

    public Set<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<Supplier> suppliers) {
        this.suppliers = suppliers;
    }




    @ManyToOne
    @JoinColumn(name = "product_sub_type_id")
    public ProductSubType getProductSubType() {
        return productSubType;
    }

    public void setProductSubType(ProductSubType productSubType) {
        this.productSubType = productSubType;
    }

    @Basic
    @Column(name = "last_stock_edit_date")
    public Date getLastStockEditDate() {
        return lastStockEditDate;
    }

    public void setLastStockEditDate(Date lastStockEditDate) {
        this.lastStockEditDate = lastStockEditDate;
    }

    @Basic
    @Column(name = "last_number_of_ordered_edit_date")
    public Date getLastNumberOfOrderedEditDate() {
        return lastNumberOfOrderedEditDate;
    }

    public void setLastNumberOfOrderedEditDate(Date lastNumberOfOrderedEditDate) {
        this.lastNumberOfOrderedEditDate = lastNumberOfOrderedEditDate;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", capacity='" + capacity + '\'' + ", price=" + price + ", productName='" + productName + '\'' + ", stock=" + stock + ", tmpStock=" + tmpStock + ", tmpOrdered=" + tmpOrdered + ", deliver='" + deliver + '\'' + ", isArchival=" + isArchival + ", suppliers=" + suppliers + ", productSubType=" + productSubType + ", lastStockEditDate=" + lastStockEditDate + ", lastNumberOfOrderedEditDate=" + lastNumberOfOrderedEditDate + '}';
    }
}
