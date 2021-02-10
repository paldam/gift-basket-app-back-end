package com.damian.domain.basket;

import javax.persistence.*;

@Entity
@Table(name = "basket_images")
public class BasketImage {


    private Integer BasketImageId;
    private byte[] basketImage;

    public BasketImage() {
    }

    public BasketImage( byte[] basketImage) {
        this.basketImage = basketImage;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_image_id", nullable = false)
    public Integer getBasketImageId() {
        return BasketImageId;
    }

    public void setBasketImageId(Integer basketImageId) {
        BasketImageId = basketImageId;
    }

    @Column(name = "basket_image", columnDefinition = "LONGBLOB")
    public byte[] getBasketImage() {
        return basketImage;
    }

    public void setBasketImage(byte[] basketImage) {
        this.basketImage = basketImage;
    }
}
