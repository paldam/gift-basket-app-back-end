package com.damian.domain.customer;

import javax.persistence.*;

@Entity
@Table(name = "company")
public class Company {

    private Long companyId;
    private String companyName;

    public Company() {
    }

    public Company(String companyName) {
        this.companyName = companyName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id",nullable = false)
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

    @Override
    public String toString() {
        return "Company{" +
            "companyId=" + companyId +
            ", companyName='" + companyName + '\'' +
            '}';
    }
}

