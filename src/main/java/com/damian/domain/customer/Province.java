package com.damian.domain.customer;

import javax.persistence.*;

@Entity
@Table(name = "province")
public class Province {

    private Long provinceId;
    private String name;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "province_id", nullable = false)
    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }


    @Basic
    @Column(name = "name", length = 350)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Province{" + "provinceId=" + provinceId + ", name='" + name + '\'' + '}';
    }
}

