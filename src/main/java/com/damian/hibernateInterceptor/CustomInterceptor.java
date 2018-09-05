package com.damian.hibernateInterceptor;

import com.damian.model.Order;
import com.damian.model.OrderEditAudit;
import com.damian.model.OrderStatus;
import com.damian.repository.OrderDao;
import com.damian.repository.OrderEditAuditDao;
import com.damian.repository.ProductDao;
import com.damian.security.SecurityUtils;
import com.damian.service.AddresService;
import com.damian.service.OrderService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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



        }






        return super.onFlushDirty(entity, id, currentState,
                previousState, propertyNames, types);
    }


    private boolean isOrderStatusChange(Object currState, Object prevState ){
        logger.info("Warunek jest"+ ObjectUtils.equals(currState , prevState));
        return ObjectUtils.equals(currState , prevState) ;

    }


}