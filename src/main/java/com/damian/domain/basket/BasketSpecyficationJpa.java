package com.damian.domain.basket;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.List;

@Transactional(readOnly = true)
public class BasketSpecyficationJpa {

    public static Specification<Basket> getBasketsWithSearchFilter(String likeText) {
        return (Specification<Basket>) (root, criteriaQuery, criteriaBuilder) -> {
            if(!currentQueryIsCountRecords(criteriaQuery)){
                root.fetch("basketType");
                root.fetch("basketSezon");
            }

            Predicate basketNameLike = criteriaBuilder.like((root.get(Basket_.basketName)), "%" + likeText + "%");
            return basketNameLike;
        };
    }

    public static Specification<Basket> getWithoutDeleted(boolean onlyArchival) {
        if (onlyArchival) {
            return (Specification<Basket>) (root, criteriaQuery, criteriaBuilder) -> {
                Join<Basket, BasketType> basketTypeJoinJoin = root.join(Basket_.basketType);
                Predicate equalPredicate = criteriaBuilder.equal(basketTypeJoinJoin.get(BasketType_.basketTypeId), 99);
                return equalPredicate;
            };
        } else {
            return (Specification<Basket>) (root, criteriaQuery, criteriaBuilder) -> {
                Join<Basket, BasketType> basketTypeJoinJoin = root.join(Basket_.basketType);
                Predicate equalPredicate = criteriaBuilder.notEqual(basketTypeJoinJoin.get(BasketType_.basketTypeId), 99);
                Predicate equalPredicate2 = criteriaBuilder.notEqual(basketTypeJoinJoin.get(BasketType_.basketTypeId), 999);
                Predicate WithoutDeletedPredicate = criteriaBuilder.and(equalPredicate, equalPredicate2);
                return WithoutDeletedPredicate;
            };
        }
    }

    public static Specification<Basket> getOrderWithSeasons(List<Integer> basketSeasons) {
        if (basketSeasons.isEmpty()) {
            return Specification.where(null);
        } else {
            return (Specification<Basket>) (root, criteriaQuery, criteriaBuilder) -> {
                Join<Basket, BasketSezon> basketSezonJoin = root.join(Basket_.basketSezon);
                Expression<Integer> basketExpression = basketSezonJoin.get(BasketSezon_.basketSezonId);
                Predicate basketSeasonPredicate = basketExpression.in(basketSeasons);
                return basketSeasonPredicate;
            };
        }
    }

    private static boolean currentQueryIsCountRecords(CriteriaQuery<?> criteriaQuery) {
        return criteriaQuery.getResultType() == Long.class || criteriaQuery.getResultType() == long.class;
    }
}



