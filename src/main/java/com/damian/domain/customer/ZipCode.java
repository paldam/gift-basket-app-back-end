package com.damian.domain.customer;

import javax.persistence.*;

@Entity
@Table(name = "zip_codes")
public class ZipCode {

    @EmbeddedId
    private ZipCodeCompositeKey zipCode;
    private String province;

    public ZipCodeCompositeKey getZipCode() {
        return zipCode;
    }

    public void setZipCode(ZipCodeCompositeKey zipCode) {
        this.zipCode = zipCode;
    }

    @Basic
    @Column(name = "province", length = 300)
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }


}

