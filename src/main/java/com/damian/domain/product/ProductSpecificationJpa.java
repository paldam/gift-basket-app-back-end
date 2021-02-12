package com.damian.domain.product;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.swing.text.StyledEditorKit;
import java.util.List;

public class ProductSpecificationJpa {

    public static Specification<Product> getProductWithSearchFilter(String likeText) {

        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> {
            Predicate productNameLike = criteriaBuilder.like((root.get(Product_.productName)), "%" + likeText + "%");
            return productNameLike;
        };
    }

    public static Specification<Product> getOnlyAvailable(Boolean onlyAvailable) {
        if (!onlyAvailable) {
            return Specification.where(null);
        } else {
            return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> {
                Predicate productAv = criteriaBuilder.greaterThan((root.get(Product_.stock)), 0);
                return productAv;
            };
        }
    }

    public static Specification<Product> getProductWithSpecType(List<Integer> productTypeIds) {
        if (productTypeIds.isEmpty()) {
            return Specification.where(null);
        } else {
            return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> {
                Join<Product, ProductSubType> productProductSubTypeJoin = root.join(Product_.productSubType,JoinType.LEFT);
                Expression<Integer> productExpression = productProductSubTypeJoin.get(ProductSubType_.subTypeId);
                Predicate productSubTypePredicate = productExpression.in(productTypeIds);
                return productSubTypePredicate;
            };
        }
    }

    public static Specification<Product> getProductWithSpecSupplier(List<Integer> supplierIds) {
        if (supplierIds.isEmpty()) {
            return Specification.where(null);
        } else {
            return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> {
                Join<Product, Supplier> productSupplierJoin = root.join(Product_.suppliers,JoinType.LEFT);
                Expression<Integer> productExpression = productSupplierJoin.get(Supplier_.supplierId);
                return productExpression.in(supplierIds);
            };
        }
    }

}
