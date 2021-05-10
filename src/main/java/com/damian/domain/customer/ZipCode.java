package com.damian.domain.customer;

import javax.persistence.*;

@Entity
@Table(name = "zip_codes")
public class ZipCode {

    @EmbeddedId
    private ZipCodeCompositeKey zipCode;
    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;
    private String provinceName;

    public ZipCodeCompositeKey getZipCode() {
        return zipCode;
    }

    public void setZipCode(ZipCodeCompositeKey zipCode) {
        this.zipCode = zipCode;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    @Basic
    @Column(name = "province", length = 300)
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}

