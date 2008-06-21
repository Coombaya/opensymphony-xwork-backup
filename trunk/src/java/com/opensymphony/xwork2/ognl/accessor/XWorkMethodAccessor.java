/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork2.ognl.accessor;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.util.reflection.ReflectionContextState;
import ognl.*;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Map;


/**
 * Allows methods to be executed under normal cirumstances, except when {@link ReflectionContextState#DENY_METHOD_EXECUTION}
 * is in the action context with a value of true.
 *
 * @author Patrick Lightbody
 * @author tmjee
 */
public class XWorkMethodAccessor extends ObjectMethodAccessor {
	
	private static final Logger LOG = LoggerFactory.getLogger(XWorkMethodAccessor.class);

    /**
     * @deprecated Use {@link ReflectionContextState#DENY_METHOD_EXECUTION} instead
     */
    @Deprecated public static final String DENY_METHOD_EXECUTION = ReflectionContextState.DENY_METHOD_EXECUTION;
    /**
     * @deprecated Use {@link ReflectionContextState#DENY_INDEXED_ACCESS_EXECUTION} instead
     */
    @Deprecated public static final String DENY_INDEXED_ACCESS_EXECUTION = ReflectionContextState.DENY_INDEXED_ACCESS_EXECUTION;


    @Override
    public Object callMethod(Map context, Object object, String string, Object[] objects) throws MethodFailedException {

        //Collection property accessing
        //this if statement ensures that ognl
        //statements of the form someBean.mySet('keyPropVal')
        //return the set element with value of the keyProp given
        
        if (objects.length==1 
                && context instanceof OgnlContext) {
            try {
              OgnlContext ogContext=(OgnlContext)context;
              if (OgnlRuntime.hasSetProperty(ogContext, object, string))  {
                  	PropertyDescriptor descriptor=OgnlRuntime.getPropertyDescriptor(object.getClass(), string);
                  	Class propertyType=descriptor.getPropertyType();
                  	if ((Collection.class).isAssignableFrom(propertyType)) {
                  	    //go directly through OgnlRuntime here
                  	    //so that property strings are not cleared
                  	    //i.e. OgnlUtil should be used initially, OgnlRuntime
                  	    //thereafter
                  	    
                  	    Object propVal=OgnlRuntime.getProperty(ogContext, object, string);
                  	    //use the Collection property accessor instead of the individual property accessor, because 
                  	    //in the case of Lists otherwise the index property could be used
                  	    PropertyAccessor accessor=OgnlRuntime.getPropertyAccessor(Collection.class);
                  	    ReflectionContextState.setGettingByKeyProperty(ogContext,true);
                  	    return accessor.getProperty(ogContext,propVal,objects[0]);
                  	}
              }
            }	catch (Exception oe) {
                //this exception should theoretically never happen
                //log it
            	LOG.error("An unexpected exception occurred", oe);
            }

        }

        //HACK - we pass indexed method access i.e. setXXX(A,B) pattern
        if (
                (objects.length == 2 && string.startsWith("set"))
                        ||
                        (objects.length == 1 && string.startsWith("get"))
                ) {
            Boolean exec = (Boolean) context.get(ReflectionContextState.DENY_INDEXED_ACCESS_EXECUTION);
            boolean e = ((exec == null) ? false : exec.booleanValue());
            if (!e) {
                return super.callMethod(context, object, string, objects);
            }
        }
        Boolean exec = (Boolean) context.get(ReflectionContextState.DENY_METHOD_EXECUTION);
        boolean e = ((exec == null) ? false : exec.booleanValue());

        if (!e) {
            return super.callMethod(context, object, string, objects);
        } else {
            return null;
        }
    }

    @Override
    public Object callStaticMethod(Map context, Class aClass, String string, Object[] objects) throws MethodFailedException {
        Boolean exec = (Boolean) context.get(ReflectionContextState.DENY_METHOD_EXECUTION);
        boolean e = ((exec == null) ? false : exec.booleanValue());

        if (!e) {
            return super.callStaticMethod(context, aClass, string, objects);
        } else {
            return null;
        }
    }
}
