package com.damian.domain.order;

import com.damian.domain.customer.*;
import com.damian.domain.user.User;
import com.damian.domain.user.User_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.Calendar;
import java.util.List;

public class OrderSpecyficationJPA {





    public static Specification<Order> getOrderWithOrderStatusFilter(List<Integer> orderStatus) {
            return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
                Join<Order, OrderStatus> orderStatusJoin = root.join(Order_.orderStatus,JoinType.LEFT);
                Expression<Integer> orderExpression = orderStatusJoin.get(OrderStatus_.orderStatusId);

                if(orderStatus.isEmpty()){
                    orderStatus.add(OrderStatusConst.NOWE);
                    orderStatus.add(OrderStatusConst.ZREALIZOWANE);
                    orderStatus.add(OrderStatusConst.W_TRAKCJE_REALIZACJI);
                }

                Predicate orderPredicate = orderExpression.in(orderStatus);
                return orderPredicate;
            };
    }

    public static Specification<Order> getOrderWithDeliveryTypeFilter(List<Integer> deliveryTypeList) {
        if (deliveryTypeList.isEmpty()) {
            return Specification.where(null);
        } else {
            return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
                Join<Order, DeliveryType> orderDeliveryTypJoin = root.join(Order_.deliveryType);
                Expression<Integer> orderExpression = orderDeliveryTypJoin.get(DeliveryType_.deliveryTypeId);
                Predicate orderPredicate = orderExpression.in(deliveryTypeList);
                return orderPredicate;
            };
        }
    }

    public static Specification<Order> getOrderWithProductionUserFilter(List<Integer> productionUserList) {
        if (productionUserList.isEmpty()) {
            return Specification.where(null);
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
            return Specification.where(null);
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
            return Specification.where(null);
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
        return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {

            if(!currentQueryIsCountRecords(criteriaQuery)){
                Fetch<Order, Customer> orderCustomerJoin = root.fetch(Order_.customer,JoinType.LEFT);
                orderCustomerJoin.fetch("company",JoinType.LEFT);
                root.fetch("orderStatus", JoinType.LEFT);
                root.fetch("orderItems", JoinType.LEFT).fetch("basket",JoinType.LEFT);
                root.fetch("productionUser", JoinType.LEFT);
                root.fetch("deliveryType", JoinType.LEFT);
                root.fetch("loyaltyUser", JoinType.LEFT);
                root.fetch("address",JoinType.LEFT);

            }





          //  root.fetch("customer", JoinType.LEFT);




            Expression<Integer> yearFromDate = criteriaBuilder.function("YEAR", Integer.class, root.get(Order_.orderDate));
            if (orderYears.isEmpty()) {
                orderYears.add(Calendar.getInstance().get(Calendar.YEAR) - 1);
            }
            return yearFromDate.in(orderYears);
        };
    }


    public static Specification<Order> getOrderWithSearchFilter(String likeText) {


            if (likeText.isEmpty()) {
                return Specification.where(null);
            } else {
                return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
                Join<Order, Customer> orderCustomerJoin = root.join(Order_.customer, JoinType.LEFT);
                Join<Customer, Company> orderCustomerCompanyJoin = orderCustomerJoin.join(Customer_.company, JoinType.LEFT);
                Predicate orderFvLike = criteriaBuilder.like((root.get(Order_.orderFvNumber)), "%" + likeText + "%");
                Predicate orderAditionalInformationLike = criteriaBuilder.like((root.get(Order_.additionalInformation)), "%" + likeText + "%");
                Predicate orderCustomerOrganizationNameLike = criteriaBuilder.like((orderCustomerCompanyJoin.get(Company_.companyName)), "%" + likeText + "%");
                Predicate orderCustomerNameLike = criteriaBuilder.like((orderCustomerJoin.get(Customer_.name)), "%" + likeText + "%");
                return criteriaBuilder.or(orderFvLike, orderAditionalInformationLike, orderCustomerNameLike, orderCustomerOrganizationNameLike);
            };
        }
    }



    private static boolean currentQueryIsCountRecords(CriteriaQuery<?> criteriaQuery) {
        return criteriaQuery.getResultType() == Long.class || criteriaQuery.getResultType() == long.class;
    }
}
