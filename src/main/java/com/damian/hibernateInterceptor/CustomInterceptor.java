package com.damian.hibernateInterceptor;

import com.damian.model.Order;
import com.damian.model.OrderEditAudit;
import com.damian.repository.OrderEditAuditDao;
import com.damian.security.SecurityUtils;
import com.damian.service.AddresService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomInterceptor extends EmptyInterceptor {


    private static final Logger logger = Logger.getLogger(AddresService.class);
    @Autowired
    private ApplicationContext appContext;



    @Override
    public boolean onFlushDirty(Object entity, Serializable id,
                                Object[] currentState, Object [] previousState,
                                String[] propertyNames, Type[] types) {
      

        if (entity instanceof Order) {

            for (int i = 0; i < currentState.length; i++) {
                if (ObjectUtils.equals(currentState[i], previousState[i]) ) {

                    logger.info("+++ " +propertyNames[i]+ " " + previousState[i] + " ---> " + currentState[i]  );
                }   else{
                    logger.info("---- " +propertyNames[i]+ " "+ previousState[i] + " ---> " + currentState[i] );
                }
            }


            OrderEditAuditDao  orderEditAuditDao = (OrderEditAuditDao )  appContext.getBean("orderEditAuditDao") ;
            OrderEditAudit orderEditAudit = new OrderEditAudit(SecurityUtils.getCurrentUserLogin(),((Order) entity).getOrderId());
            orderEditAuditDao.save(orderEditAudit) ;
            

        }
        return super.onFlushDirty(entity, id, currentState,
                previousState, propertyNames, types);
    }


}