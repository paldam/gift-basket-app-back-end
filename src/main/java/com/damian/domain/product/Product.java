package com.damian.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "products")
public class Product {

    private Integer id;
    private String capacity;
    private Integer price;
    private String productName;
    private String productCatalogName;
    private Integer stock;
    private Integer tmpStock;
    private Integer tmpOrdered;
    private Integer isArchival;
    private Set<Supplier> suppliers;
    private ProductSubType productSubType;
    private ProductSeason productSeason;
    private Date lastStockEditDate;
    private Date lastNumberOfOrderedEditDate;
    private byte[] productImageData;
    private Integer isProductImg;

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
    @Column(name = "product_catalog_name",nullable = true, length = 300)
    public String getProductCatalogName() {
        return productCatalogName;
    }

    public void setProductCatalogName(String productCatalogName) {
        this.productCatalogName = productCatalogName;
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
    @Column(name = "stock", columnDefinition = "INT DEFAULT 0")
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_suppliers", joinColumns = {@JoinColumn(name = "id")},  //
        inverseJoinColumns = {@JoinColumn(name = "supplier_id")})
    public Set<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<Supplier> suppliers) {
        this.suppliers = suppliers;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_season_id")
    public ProductSeason getProductSeason() {
        return productSeason;
    }

    public void setProductSeason(ProductSeason productSeason) {
        this.productSeason = productSeason;
    }

    @ManyToOne(fetch = FetchType.LAZY)
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

    @JsonIgnore
    @Basic
    @Column(name = "image", columnDefinition = "LONGBLOB")
    public byte[] getProductImageData() {
        return productImageData;
    }

    public void setProductImageData(byte[] productImageData) {
        this.productImageData = productImageData;
    }

    @Basic
    @Column(name = "is_product_img", length = 40, columnDefinition = "INT DEFAULT 0")
    public Integer getIsProductImg() {
        return isProductImg;
    }

    public void setIsProductImg(Integer isProductImg) {
        this.isProductImg = isProductImg;
    }
}
