package com.damian.domain.order;

import com.damian.domain.customer.Company;
import com.damian.domain.customer.Company_;
import com.damian.domain.customer.Customer;
import com.damian.domain.customer.Customer_;
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

    public static Specification<Order> getOrderWithSearchFilter(String likeText){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {


                Join<Order, Customer> orderCustomerJoin = root.join(Order_.customer);
                Join<Customer, Company> orderCustomerCompanyJoin = orderCustomerJoin.join(Customer_.company);

                Predicate orderFvLike = criteriaBuilder.like((root.get(Order_.orderFvNumber)), "%"+ likeText +"%");
                Predicate orderAditionalInformationLike = criteriaBuilder.like((root.get(Order_.additionalInformation)), "%"+ likeText +"%");
                Predicate orderCustomerOrganizationNameLike = criteriaBuilder.like(( orderCustomerCompanyJoin.get(Company_.companyName)), "%"+ likeText +"%");
                Predicate orderCustomerNameLike = criteriaBuilder.like((orderCustomerJoin.get(Customer_.name)), "%"+ likeText +"%");
                Predicate likePredicate = criteriaBuilder.or(orderFvLike,orderAditionalInformationLike,orderCustomerOrganizationNameLike,orderCustomerNameLike);

                return likePredicate;

            }
        };
    }
}
