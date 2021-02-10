package com.damian.domain.order;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

public class OrderFilterPredicate {

    @PersistenceContext
    static EntityManager entityManager;
    static private CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    static  CriteriaQuery<Order> query = cb.createQuery(Order.class);
    static Root<Order> root = query.from(Order.class);


    public static  Predicate orderStatusPredicate(List<Integer> orderStatus) {


        Join<Order, OrderStatus> orderStatusJoin = root.join(Order_.orderStatus);
        Expression<Integer> orderExpression = orderStatusJoin.get(OrderStatus_.orderStatusId);
        Predicate orderPredicate = orderExpression.in(orderStatus);

        return orderPredicate;

    }


}
