package com.damian.domain.order;

import com.damian.domain.customer.*;
import com.damian.domain.user.User;
import com.damian.domain.user.User_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

public class OrderSpecyficationJPA {

    public static Specification<Order> getOrderWithOrderStatusFilter(List<Integer> orderStatus) {
        if (orderStatus.isEmpty()) {
            return getOrdersForSpecyficEmptyFilterList();
        } else {
            return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
                Join<Order, OrderStatus> orderStatusJoin = root.join(Order_.orderStatus);
                Expression<Integer> orderExpression = orderStatusJoin.get(OrderStatus_.orderStatusId);
                Predicate orderPredicate = orderExpression.in(orderStatus);
                return orderPredicate;
            };
        }
    }

    public static Specification<Order> getOrderWithProductionUserFilter(List<Integer> productionUserList) {
        if (productionUserList.isEmpty()) {
            return getOrdersForSpecyficEmptyFilterList();
        } else {
            return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
                Join<Order, User> orderStatusJoin = root.join(Order_.productionUser);
                Expression<Long> orderExpression = orderStatusJoin.get(User_.id);
                Predicate orderPredicate = orderExpression.in(productionUserList);
                return orderPredicate;
            };
        }
    }


    public static Specification<Order> getOrderWithWeeksFilter(List<Integer> orderWeeksUserFilterList) {
        if (orderWeeksUserFilterList.isEmpty()) {
            return getOrdersForSpecyficEmptyFilterList();
        } else {
            return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
                Expression<Integer> orderExpression = root.get(Order_.weekOfYear);
                Predicate orderPredicate = orderExpression.in(orderWeeksUserFilterList);
                return orderPredicate;
            };
        }
    }

    public static Specification<Order> getOrderWithProvincesFilter(List<String> provincesList) {
        if (provincesList.isEmpty()) {
            return getOrdersForSpecyficEmptyFilterList();
        } else {
            return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {


                Join<Order, Address> orderAddressJoin = root.join(Order_.address);
                Join<Address,Province> addressProvinceJoin = orderAddressJoin.join(Address_.province);
                Expression<String> orderExpression = addressProvinceJoin.get(Province_.name);
                Predicate orderPredicate = orderExpression.in(provincesList);
                return orderPredicate;
            };
        }
    }



    public static Specification<Order> getOrderWithOrderYearsFilter(List<Integer> orderYears) {
        if (orderYears.isEmpty()) {
            return getOrdersForSpecyficEmptyFilterList();
        } else {
            return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
                Expression<Integer> yearFromDate = criteriaBuilder.function("YEAR", Integer.class, root.get(Order_.orderDate));
                return yearFromDate.in(orderYears);
            };
        }
    }

    private static Specification<Order> getOrdersForSpecyficEmptyFilterList(){
        return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<Order, OrderStatus> orderStatusJoin = root.join(Order_.orderStatus);
            return criteriaBuilder.notEqual(orderStatusJoin.get(OrderStatus_.orderStatusId), 99);
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
