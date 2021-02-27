package com.damian.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface ProductSubTypeDao extends JpaRepository<ProductSubType,Long> {
        List<ProductSubType> findAll();
        Optional<ProductSubType> findBySubTypeId(Integer id);

        @Query(value = "select pst from ProductSubType pst left join fetch  pst.productType pt order by pst.subTypeName ASC")
        List<ProductSubType> findallWithProductType();


}
