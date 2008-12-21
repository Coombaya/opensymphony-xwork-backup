/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.config;


/**
 * Interface to be implemented by all forms of XWork configuration classes.
 *
 * @author $Author$
 * @version $Revision$
 */
public interface ConfigurationProvider {
    //~ Methods ////////////////////////////////////////////////////////////////

    public void destroy();

    /**
     * Initializes the configuration object.
     */
    public void init(Configuration configuration) throws ConfigurationException;

    /**
     * Tells whether the ConfigurationProvider should reload its configuration
     * @return
     */
    public boolean needsReload();
}