package com.damian.domain.customer;

import javax.persistence.*;

@Entity
@Table(name = "company")
public class Company {

    private Long companyId;
    private String companyName;
    private Integer wasCombined;

    public Company() {
    }

    public Company(String companyName) {
        this.companyName = companyName;
    }

    public Company(Long companyId, String companyName) {
        this.companyId = companyId;
        this.companyName = companyName;
    }

    public Company(Long companyId, String companyName, Integer wasCombined) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.wasCombined = wasCombined;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id", nullable = false)
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Basic
    @Column(name = "company_name", length = 300)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Basic
    @Column(name = "was_combined", columnDefinition = "INT DEFAULT 0")
    public Integer getWasCombined() {
        return wasCombined;
    }

    public void setWasCombined(Integer wasCombined) {
        this.wasCombined = wasCombined;
    }
}

