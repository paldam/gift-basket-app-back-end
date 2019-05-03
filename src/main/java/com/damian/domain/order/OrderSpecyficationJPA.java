package com.damian.domain.order;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

public class OrderSpecyficationJPA {

    public static Specification<Order> getOrderWithFilter( List<Integer> orderStatus){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join<Order, OrderStatus> orderStatusJoin = root.join(Order_.orderStatus);

                Expression<Integer> orderExpression = orderStatusJoin.get(OrderStatus_.orderStatusId);
                Predicate orderPredicate = orderExpression.in(orderStatus);
               return orderPredicate;
            }
        };
    }


    public static Specification<Order> getOrderWithOrderYearsFilter( List<Integer> orderYears){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                Expression<Integer> yearFromDate = criteriaBuilder.function("YEAR",Integer.class,root.get(Order_.orderDate)); // function("YEAR")... is DB function
                Predicate  orderPredicate = yearFromDate.in(orderYears);
                return orderPredicate;
            }
        };
    }

    public static Specification<Order> getOrderWithoutdeleted(){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {


                Join<Order, OrderStatus> orderStatusJoin = root.join(Order_.orderStatus);
                Predicate equalPredicate = criteriaBuilder.notEqual(orderStatusJoin.get(OrderStatus_.orderStatusId), 99);
                return equalPredicate;
            }
        };
    }
}
