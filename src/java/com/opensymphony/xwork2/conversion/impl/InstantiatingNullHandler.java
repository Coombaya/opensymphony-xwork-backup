/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork2.conversion.impl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.conversion.NullHandler;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.util.reflection.ReflectionContextState;
import com.opensymphony.xwork2.util.reflection.ReflectionProvider;


/**
 * <!-- START SNIPPET: javadoc -->
 *
 * Provided that the key {@link ReflectionContextState#CREATE_NULL_OBJECTS} is in the action context with a value of true (this key is set
 * only during the execution of the {@link com.opensymphony.xwork2.interceptor.ParametersInterceptor}), OGNL expressions
 * that have caused a NullPointerException will be temporarily stopped for evaluation while the system automatically
 * tries to solve the null references by automatically creating the object.
 *
 * <p/> The following rules are used when handling null references:
 *
 * <ul>
 *
 * <li>If the property is declared <i>exactly</i> as a {@link Collection} or {@link List}, then an ArrayList shall be
 * returned and assigned to the null references.</li>
 *
 * <li>If the property is declared as a {@link Map}, then a HashMap will be returned and assigned to the null
 * references.</li>
 *
 * <li>If the null property is a simple bean with a no-arg constructor, it will simply be created using the {@link
 * ObjectFactory#buildBean(java.lang.Class, java.util.Map)} method.</li>
 *
 * </ul>
 *
 * <!-- END SNIPPET: javadoc -->
 *
 * <!-- START SNIPPET: example -->
 *
 * For example, if a form element has a text field named <b>person.name</b> and the expression <i>person</i> evaluates
 * to null, then this class will be invoked. Because the <i>person</i> expression evaluates to a <i>Person</i> class, a
 * new Person is created and assigned to the null reference. Finally, the name is set on that object and the overall
 * effect is that the system automatically created a Person object for you, set it by calling setUsers() and then
 * finally called getUsers().setName() as you would typically expect.
 *
 * <!-- END SNIPPET: example>
 *
 * @author Matt Ho
 * @author Patrick Lightbody
 */
public class InstantiatingNullHandler implements NullHandler {

    /**
     * @deprecated Use {@link ReflectionContextState#CREATE_NULL_OBJECTS} instead
     */
    public static final String CREATE_NULL_OBJECTS = ReflectionContextState.CREATE_NULL_OBJECTS;
    private static final Logger LOG = LoggerFactory.getLogger(InstantiatingNullHandler.class);
    private ReflectionProvider reflectionProvider;
    private ObjectFactory objectFactory;
    
    @Inject
    public void setReflectionProvider(ReflectionProvider prov) {
        this.reflectionProvider = prov;
    }
    
    @Inject
    public void setObjectFactory(ObjectFactory fac) {
        this.objectFactory = fac;
    }
    
    public Object nullMethodResult(Map context, Object target, String methodName, Object[] args) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Entering nullMethodResult ");
        }

        return null;
    }

    public Object nullPropertyValue(Map context, Object target, Object property) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Entering nullPropertyValue [target="+target+", property="+property+"]");
        }

        boolean c = ReflectionContextState.isCreatingNullObjects(context);

        if (!c) {
            return null;
        }

        if ((target == null) || (property == null)) {
            return null;
        }

        try {
            String propName = property.toString();
            Object realTarget = reflectionProvider.getRealTarget(propName, context, target);
            Class clazz = null;

            if (realTarget != null) {
                PropertyDescriptor pd = reflectionProvider.getPropertyDescriptor(realTarget.getClass(), propName);
                if (pd == null) {
                    return null;
                }

                clazz = pd.getPropertyType();
            }

            if (clazz == null) {
                // can't do much here!
                return null;
            }

            Object param = createObject(clazz, realTarget, propName, context);

            reflectionProvider.setValue(propName, context, realTarget, param);

            return param;
        } catch (Exception e) {
            LOG.error("Could not create and/or set value back on to object", e);
        }

        return null;
    }

    private Object createObject(Class clazz, Object target, String property, Map context) throws Exception {
        if (Collection.class.isAssignableFrom(clazz)) {
            return new ArrayList();
        } else if (clazz == Map.class) {
            return new HashMap();
        }

        return objectFactory.buildBean(clazz, context);
    }
}