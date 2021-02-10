package com.damian.domain.basket;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



@Entity
@Table(name = "baskets")
public class Basket {

    private Long basketId;
    private String basketName;
    private BasketSezon basketSezon;
    private BasketType basketType;
    private List<BasketItems> basketItems;
    private Integer basketTotalPrice;
    private Integer basketProductsPrice;
    private String season;
    private Integer stock;
    private Integer isAlcoholic;
    private Integer isAvailable;
    private  BasketImage basketImage;
    private Integer isBasketImg;
    private Date lastStockEditDate;
    private String imgNumber;

    public Basket() {
    }

    public Basket(BasketExt basketExt) {
        this.basketId = basketExt.getBasketId();
        this.basketName = basketExt.getBasketName();
        this.basketType = basketExt.getBasketType();
        this.basketItems = basketExt.getBasketItems();
        this.basketTotalPrice = basketExt.getBasketTotalPrice();
        this.season = basketExt.getSeason();
        this.isAlcoholic = basketExt.getIsAlcoholic();
        this.isAvailable = basketExt.getIsAvailable();
        this.basketProductsPrice = basketExt.getBasketProductsPrice();
    }

    @Basic
    @Column(name = "basket_name", nullable = false, length = 300)
    public String getBasketName() {
        return basketName;
    }

    public void setBasketName(String basketName) {
        this.basketName = basketName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_id", nullable = false)
    public Long getBasketId() {
        return basketId;
    }

    public void setBasketId(Long basketId) {
        this.basketId = basketId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_type")
    public BasketType getBasketType() {
        return basketType;
    }

    public void setBasketType(BasketType basketType) {
        this.basketType = basketType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_sezon")
    public BasketSezon getBasketSezon() {
        return basketSezon;
    }

    public void setBasketSezon(BasketSezon basketSezon) {
        this.basketSezon = basketSezon;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id", referencedColumnName = "basket_id")
    public List<BasketItems> getBasketItems() {
        return basketItems;
    }

    public void setBasketItems(List<BasketItems> basketItems) {
        this.basketItems = basketItems;
    }

    @Basic
    @Column(name = "stock", length = 40, columnDefinition = "INT DEFAULT 0")
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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
    @Column(name = "basket_products_price", length = 40)
    public Integer getBasketProductsPrice() {
        return basketProductsPrice;
    }

    public void setBasketProductsPrice(Integer basketProductsPrice) {
        this.basketProductsPrice = basketProductsPrice;
    }

    @Basic
    @Column(name = "basket_total_price", length = 40)
    public Integer getBasketTotalPrice() {
        return basketTotalPrice;
    }

    public void setBasketTotalPrice(Integer basketTotalPrice) {
        this.basketTotalPrice = basketTotalPrice;
    }

    @Basic
    @Column(name = "season", length = 200)
    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    @Basic
    @Column(name = "is_alcoholic", length = 40, columnDefinition = "INT DEFAULT 0")
    public Integer getIsAlcoholic() {
        return isAlcoholic;
    }

    public void setIsAlcoholic(Integer isAlcoholic) {
        this.isAlcoholic = isAlcoholic;
    }

    @Basic
    @Column(name = "is_available", length = 40, columnDefinition = "INT DEFAULT 1")
    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_image_id")
    public BasketImage getBasketImage() {
        return basketImage;
    }

    public void setBasketImage(BasketImage basketImage) {
        this.basketImage = basketImage;
    }

    @Column(name = "is_basket_img", length = 40, columnDefinition = "INT DEFAULT 0")
    public Integer getIsBasketImg() {
        return isBasketImg;
    }

    public void setIsBasketImg(Integer isBasketImg) {
        this.isBasketImg = isBasketImg;
    }


    @Basic
    @Column(name = "img_number", length = 200)
    public String getImgNumber() {
        return imgNumber;
    }

    public void setImgNumber(String imgNumber) {
        this.imgNumber = imgNumber;
    }


}
        
