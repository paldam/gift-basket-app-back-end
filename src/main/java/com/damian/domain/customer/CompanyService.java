package com.damian.domain.customer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {

    private CompanyDao companyDao;

    public CompanyService(CompanyDao companyDao) {

        this.companyDao = companyDao;
    }


@Transactional

    public Company mergeCompany(List<Company> companyList, String newCompaniesName ){

       newCompaniesName = newCompaniesName.replace("\"", "");

        List<Long> companyToChangeIdList = new ArrayList<>();

        companyList.forEach(company -> {

            companyToChangeIdList.add(company.getCompanyId());
        });

        Long idRootCompany = companyList.get(0).getCompanyId();


        companyDao.updateCompanyName(newCompaniesName,idRootCompany);
        companyDao.updateCompany(idRootCompany,companyToChangeIdList);


        companyToChangeIdList.remove(0);

        companyDao.deleteCompanies(companyToChangeIdList);



      return  new Company(idRootCompany,newCompaniesName);


    }



}
