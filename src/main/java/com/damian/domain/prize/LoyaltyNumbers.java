package com.damian.domain.prize;

import javax.persistence.*;

@Entity
@Table(name = "loyalty_numbers")
public class LoyaltyNumbers {

    private Long id;
    private String number;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ids", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    @Column(name = "number", unique=true)
    public String getNumber() {
        return number;
    }

    public void setNumber(String id) {
        this.number = id;
    }


}
