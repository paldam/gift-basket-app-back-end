package com.damian.domain.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CompanyDao extends JpaRepository<Company, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE company set company.company_name =?1, company.was_combined =1 " + "WHERE company" +
        ".company_id = ?2", nativeQuery = true)
    public void updateCompanyName(String newName, Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE customers set customers.company_id =?1 " + "WHERE customers.company_id in ?2",
        nativeQuery = true)
    public void updateCompany(Long id, List<Long> companyToChangeIdList);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM company WHERE company_id IN ?1", nativeQuery = true)
    public void deleteCompanies(List<Long> comapnyToDelete);
}
