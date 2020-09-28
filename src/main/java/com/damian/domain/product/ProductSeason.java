package com.damian.domain.product;

import javax.persistence.*;

@Entity
@Table(name = "products_season")
public class ProductSeason {

    private Integer productSeasonId;
    private String productSeasonName;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_season_id", nullable = false)
    public Integer getProductSeasonId() {
        return productSeasonId;
    }

    public void setProductSeasonId(Integer productSeasonId) {
        this.productSeasonId = productSeasonId;
    }

    @Basic
    @Column(name = "product_season_name", nullable = false, length = 40)
    public String getProductSeasonName() {
        return productSeasonName;
    }

    public void setProductSeasonName(String productSeasonName) {
        this.productSeasonName = productSeasonName;
    }
}
