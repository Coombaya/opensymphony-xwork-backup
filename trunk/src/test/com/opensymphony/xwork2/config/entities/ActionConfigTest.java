/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork2.config.entities;

import com.opensymphony.xwork2.util.location.LocationImpl;
import com.opensymphony.xwork2.XWorkTestCase;

import java.util.HashMap;


/**
 * ActionConfigTest
 */
public class ActionConfigTest extends XWorkTestCase {

    public void testToString() {
        ActionConfig cfg = new ActionConfig();
        cfg.setClassName("foo.Bar");
        cfg.setMethodName("execute");
        cfg.setLocation(new LocationImpl(null, "foo/xwork.xml", 10, 12));
        
        assertTrue("Wrong toString(): "+cfg.toString(), 
            "{ActionConfig foo.Bar.execute() - foo/xwork.xml:10:12}".equals(cfg.toString()));
    }
    
    public void testToStringWithNoMethod() {
        ActionConfig cfg = new ActionConfig();
        cfg.setClassName("foo.Bar");
        cfg.setLocation(new LocationImpl(null, "foo/xwork.xml", 10, 12));
        
        assertTrue("Wrong toString(): "+cfg.toString(), 
            "{ActionConfig foo.Bar - foo/xwork.xml:10:12}".equals(cfg.toString()));
    }
}