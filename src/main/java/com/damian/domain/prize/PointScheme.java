package com.damian.domain.prize;

import com.damian.domain.basket.BasketSezon;

import javax.persistence.*;

@Entity
@Table(name = "point_scheme")
public class PointScheme {
        private Long id;
    private BasketSezon basketSezon;
    private Integer points;
    private Integer step;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }




    @ManyToOne
    @JoinColumn(name = "basket_sezon")
    public BasketSezon getBasketSezon() {
        return basketSezon;
    }

    public void setBasketSezon(BasketSezon basketSezon) {
        this.basketSezon = basketSezon;
    }

    @Basic
    @Column(name = "points", length = 40, columnDefinition = "INT DEFAULT 0")
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Basic
    @Column(name = "step", length = 40, columnDefinition = "INT DEFAULT 0")
    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }
}
