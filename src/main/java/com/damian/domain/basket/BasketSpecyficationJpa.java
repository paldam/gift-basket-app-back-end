package com.damian.domain.basket;

import com.damian.domain.customer.Company;
import com.damian.domain.customer.Company_;
import com.damian.domain.customer.Customer;
import com.damian.domain.customer.Customer_;
import com.damian.domain.order.Order;
import com.damian.domain.order.OrderStatus;
import com.damian.domain.order.OrderStatus_;
import com.damian.domain.order.Order_;
import com.damian.domain.product.Product;
import com.damian.domain.product.ProductSubType;
import com.damian.domain.product.Product_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

public class BasketSpecyficationJpa {

    public static Specification<Basket> getBasketsWithSearchFilter(String likeText) {
        return new Specification<Basket>() {
            @Override
            public Predicate toPredicate(Root<Basket> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate basketNameLike = criteriaBuilder.like((root.get(Basket_.basketName)), "%" + likeText + "%");
                return basketNameLike;
            }
        };
    }

    public static Specification<Basket> getOnlyDeleted() {
        return new Specification<Basket>() {
            @Override
            public Predicate toPredicate(Root<Basket> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join<Basket, BasketType> basketTypeJoinJoin = root.join(Basket_.basketType);
                Predicate equalPredicate = criteriaBuilder.equal(basketTypeJoinJoin.get(BasketType_.basketTypeId), 99);
                return equalPredicate;
            }
        };
    }

    public static Specification<Basket> getWithoutDeleted() {
        return new Specification<Basket>() {
            @Override
            public Predicate toPredicate(Root<Basket> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join<Basket, BasketType> basketTypeJoinJoin = root.join(Basket_.basketType);
                Predicate equalPredicate = criteriaBuilder.notEqual(basketTypeJoinJoin.get(BasketType_.basketTypeId), 99);
                Predicate equalPredicate2 = criteriaBuilder.notEqual(basketTypeJoinJoin.get(BasketType_.basketTypeId), 999);
                Predicate WithoutDeletedPredicate = criteriaBuilder.and(equalPredicate, equalPredicate2);
                return WithoutDeletedPredicate;
            }
        };
    }

    public static Specification<Basket> getOrderWithSeasons(List<Integer> basketSeasons) {
        return new Specification<Basket>() {
            @Override
            public Predicate toPredicate(Root<Basket> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join<Basket, BasketSezon> basketSezonJoin = root.join(Basket_.basketSezon);
                Expression<Integer> basketExpression = basketSezonJoin.get(BasketSezon_.basketSezonId);
                Predicate basketSeasonPredicate = basketExpression.in(basketSeasons);
                return basketSeasonPredicate;
            }
        };
    }
}



