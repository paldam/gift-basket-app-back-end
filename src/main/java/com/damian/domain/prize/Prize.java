package com.damian.domain.prize;

import com.damian.domain.product.Supplier;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "prizes")
public class Prize {

    private Long id;
    private String name;
    private String description;
    private Integer pkt;
    private Integer stock;
    private Boolean isAvailable;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Basic
    @Column(name = "description",length = 8000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "pkt")
    public Integer getPkt() {
        return pkt;
    }

    public void setPkt(Integer pkt) {
        this.pkt = pkt;
    }


    @Basic
    @Column(name = "isAvailable")
    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }


    @Basic
    @Column(name = "stock")
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Prize{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + ", pkt=" + pkt + ", stock=" + stock + ", isAvailable=" + isAvailable + '}';
    }
}
