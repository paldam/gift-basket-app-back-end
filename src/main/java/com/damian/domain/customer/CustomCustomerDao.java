package com.damian.domain.customer;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomCustomerDao {

    @PersistenceContext
    EntityManager em;

    public List<Customer> findCustomerByCriteria(Customer probeCustomer) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> root = cq.from(Customer.class);
        Predicate comapnyLike = cb.equal(root.get(Customer_.company), probeCustomer.getCompany());
        Predicate nameLike = cb.equal(root.get(Customer_.name), probeCustomer.getName());
        Predicate emailLike = cb.equal(root.get(Customer_.email), probeCustomer.getEmail());
        Predicate phoneNumber = cb.equal(root.get(Customer_.phoneNumber), probeCustomer.getPhoneNumber());
        cq.where(comapnyLike, nameLike, emailLike, phoneNumber);
        TypedQuery<Customer> query = em.createQuery(cq);
        return query.getResultList();
    }

    public Optional<Customer> findExacCustomerByEntity(Customer probeCustomer) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> root = cq.from(Customer.class);
        Predicate entityEq = cb.equal(root, probeCustomer);
        Predicate comapnyLike = cb.equal(root.get(Customer_.company), probeCustomer.getCompany());
        cq.where(entityEq, comapnyLike);
        try {
            return Optional.of(em.createQuery(cq).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
