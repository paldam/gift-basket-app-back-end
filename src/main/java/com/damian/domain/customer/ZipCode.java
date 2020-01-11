package com.damian.domain.customer;

import javax.persistence.*;

@Entity
@Table(name = "zip_codes")
public class ZipCode {

    @EmbeddedId
    private ZipCodeCompositeKey zipCode;

    public ZipCodeCompositeKey getZipCode() {
        return zipCode;
    }

    public void setZipCode(ZipCodeCompositeKey zipCode) {
        this.zipCode = zipCode;
    }
}

