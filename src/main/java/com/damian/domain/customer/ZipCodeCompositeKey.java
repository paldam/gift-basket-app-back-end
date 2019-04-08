package com.damian.domain.customer;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class ZipCodeCompositeKey  implements Serializable {

    @Basic
    @Column(name = "code",length = 6 )
    private String code;


    @Basic
    @Column(name = "city",length = 100)
    private String city;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
