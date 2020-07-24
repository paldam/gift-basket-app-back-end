package com.damian.domain.basket;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private byte[] basketImageData;
    private Integer isBasketImg;
    private Date lastStockEditDate;

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

    @ManyToOne
    @JoinColumn(name = "basket_type")
    public BasketType getBasketType() {
        return basketType;
    }

    public void setBasketType(BasketType basketType) {
        this.basketType = basketType;
    }

    @ManyToOne
    @JoinColumn(name = "basket_sezon")
    public BasketSezon getBasketSezon() {
        return basketSezon;
    }

    public void setBasketSezon(BasketSezon basketSezon) {
        this.basketSezon = basketSezon;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    @JsonIgnore
    @Basic
    @Column(name = "data", columnDefinition = "LONGBLOB")
    public byte[] getBasketImageData() {
        return basketImageData;
    }

    public void setBasketImageData(byte[] basketImageData) {
        this.basketImageData = basketImageData;
    }

    @Basic
    @Column(name = "is_basket_img", length = 40, columnDefinition = "INT DEFAULT 0")
    public Integer getIsBasketImg() {
        return isBasketImg;
    }

    public void setIsBasketImg(Integer isBasketImg) {
        this.isBasketImg = isBasketImg;
    }

    @Override
    public String toString() {
        return "Basket{"
            + "basketId=" + basketId
            + ", basketName='" + basketName + '\''
            + ", basketSezon=" + basketSezon
            + ", basketType=" + basketType
            + ", basketItems=" + basketItems
            + ", basketTotalPrice=" + basketTotalPrice
            + ", season='" + season + '\''
            + ", stock=" + stock
            + ", isAlcoholic=" + isAlcoholic
            + ", isAvailable=" + isAvailable
            + ", basketImageData=" + Arrays.toString(basketImageData)
            + ", isBasketImg=" + isBasketImg
            + ", lastStockEditDate=" + lastStockEditDate
            + '}';
    }
}
        
