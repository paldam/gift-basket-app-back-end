package com.damian.domain.order;

import com.damian.domain.customer.Company;
import com.damian.domain.customer.Company_;
import com.damian.domain.customer.Customer;
import com.damian.domain.customer.Customer_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

public class OrderSpecyficationJPA {

    public static Specification<Order> getOrderWithFilter(List<Integer> orderStatus) {
        return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<Order, OrderStatus> orderStatusJoin = root.join(Order_.orderStatus);
            Expression<Integer> orderExpression = orderStatusJoin.get(OrderStatus_.orderStatusId);
            Predicate orderPredicate = orderExpression.in(orderStatus);
            return orderPredicate;
        };
    }

    public static Specification<Order> getOrderWithOrderYearsFilter(List<Integer> orderYears) {
        return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
            Expression<Integer> yearFromDate = criteriaBuilder.function("YEAR", Integer.class,
                root.get(Order_.orderDate));
            return yearFromDate.in(orderYears);
        };
    }

    public static Specification<Order> getOrderWithoutdeleted() {
        return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<Order, OrderStatus> orderStatusJoin = root.join(Order_.orderStatus);
            return criteriaBuilder.notEqual(orderStatusJoin.get(OrderStatus_.orderStatusId), 99);
        };
    }

    public static Specification<Order> getOrderWithSearchFilter(String likeText) {
        return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<Order, Customer> orderCustomerJoin = root.join(Order_.customer);
            Join<Customer, Company> orderCustomerCompanyJoin = orderCustomerJoin.join(Customer_.company,
                JoinType.LEFT);
            Predicate orderFvLike = criteriaBuilder.like((root.get(Order_.orderFvNumber)), "%" + likeText + "%");
            Predicate orderAditionalInformationLike =
                criteriaBuilder.like((root.get(Order_.additionalInformation)), "%" + likeText + "%");
            Predicate orderCustomerOrganizationNameLike =
                criteriaBuilder.like((orderCustomerCompanyJoin.get(Company_.companyName)), "%" + likeText + "%");
            Predicate orderCustomerNameLike = criteriaBuilder.like((orderCustomerJoin.get(Customer_.name)),
                "%" + likeText + "%");
            return criteriaBuilder.or(orderFvLike, orderAditionalInformationLike,
                orderCustomerNameLike, orderCustomerOrganizationNameLike);
        };
    }
}
