package com.damian.hibernateInterceptor;

import com.damian.domain.order.Order;
import com.damian.domain.audit.OrderEditAudit;
import com.damian.domain.order.OrderEditAuditDao;
import com.damian.security.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


import java.io.Serializable;

@Component
public class CustomInterceptor extends EmptyInterceptor {


    @Autowired
    private ApplicationContext appContext;
    private static final Logger logger = Logger.getLogger(CustomInterceptor.class);

    
    @Override
    public boolean onFlushDirty(Object entity, Serializable id,
                                Object[] currentState, Object [] previousState,
                                String[] propertyNames, Type[] types) {
      

        if (entity instanceof Order) {



            OrderEditAuditDao  orderEditAuditDao = (OrderEditAuditDao )  appContext.getBean("orderEditAuditDao") ;
            OrderEditAudit orderEditAudit = new OrderEditAudit(SecurityUtils.getCurrentUserLogin(),((Order) entity).getOrderId());
            orderEditAuditDao.save(orderEditAudit) ;

            logger.error(" W INTERCEPT użytkownik to " + SecurityUtils.getCurrentUserLogin());


        }






        return super.onFlushDirty(entity, id, currentState,
                previousState, propertyNames, types);
    }


    private boolean isOrderStatusChange(Object currState, Object prevState ){
        logger.info("Warunek jest"+ ObjectUtils.equals(currState , prevState));
        return ObjectUtils.equals(currState , prevState) ;

    }


}
