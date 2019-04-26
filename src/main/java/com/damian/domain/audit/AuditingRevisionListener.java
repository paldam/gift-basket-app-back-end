package com.damian.domain.audit;


import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditingRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        OrderAuditedRevisionEntity orderAuditedRevisionEntity = (OrderAuditedRevisionEntity) revisionEntity;
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        orderAuditedRevisionEntity.setUser(userName);
    }
}
