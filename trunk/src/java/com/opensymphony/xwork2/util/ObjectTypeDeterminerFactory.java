/*
 * Copyright (c) 2002-2007 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork2.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Factory for getting an instance of {@link ObjectTypeDeterminer}.
 * <p/>
 * Will use <code>com.opensymphony.xwork2.util.GenericsObjectTypeDeterminer</code> if running on JDK5 or higher.
 * If not <code>com.opensymphony.xwork2.util.ObjectTypeDeterminer</code> is used.
 *
 * @author plightbo
 * @author Rainer Hermanns
 */
public class ObjectTypeDeterminerFactory {
    private static final Log LOG = LogFactory.getLog(ObjectTypeDeterminerFactory.class);

    private static ObjectTypeDeterminer instance = new DefaultObjectTypeDeterminer();

    static {
        try {
            Class c = ClassLoaderUtil.loadClass("com.opensymphony.xwork2.util.GenericsObjectTypeDeterminer",
                    ObjectTypeDeterminerFactory.class);

            LOG.info("Detected GenericsObjectTypeDeterminer, initializing it...");
            instance = (ObjectTypeDeterminer) c.newInstance();
        } catch (ClassNotFoundException e) {
            // this is fine, just fall back to the default object type determiner
        } catch (Exception e) {
            LOG.error("Exception when trying to create new GenericsObjectTypeDeterminer", e);
        }
    }

    /**
     * Sets a new instance of ObjectTypeDeterminer to be used.
     *
     * @param instance  instance of ObjectTypeDeterminer
     */
    public static void setInstance(ObjectTypeDeterminer instance) {
        ObjectTypeDeterminerFactory.instance = instance;
    }

    /**
     * Gets the instance of ObjectTypeDeterminer to be used.
     *
     * @return instance of ObjectTypeDeterminer
     */
    public static ObjectTypeDeterminer getInstance() {
        return instance;
    }

}
