/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.interceptor.component;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;


/**
 *
 *
 * @author $Author$
 * @version $Revision$
 */
public class DefaultComponentManagerTest extends TestCase {
    //~ Methods ////////////////////////////////////////////////////////////////

    public void testFoo() throws IOException, SAXException {
        ComponentConfiguration cc = new ComponentConfiguration();
        String configText = "<components>\n" + "<component>\n" + "<class>" + Foo.class.getName() + "</class>\n" + "<scope>foo</scope>\n" + "<enabler>" + FooAware.class.getName() + "</enabler>\n" + "</component>\n" + "<component>\n" + "<class>" + Bar.class.getName() + "</class>\n" + "<scope>bar</scope>\n" + "<enabler>" + BarAware.class.getName() + "</enabler>\n" + "</component>\n" + "</components>\n";

        cc.loadFromXml(new ByteArrayInputStream(configText.getBytes()));

        DefaultComponentManager dcmFoo = new DefaultComponentManager();
        DefaultComponentManager dcmBar = new DefaultComponentManager();
        dcmFoo.setFallback(dcmBar);

        cc.configure(dcmFoo, "foo");
        cc.configure(dcmBar, "bar");

        SomeComponent component = new SomeComponent();
        dcmFoo.initializeObject(component);

        assertTrue(component.initCalled);
        assertFalse(component.disposeCalled);
        assertNotNull(component.foo);
        assertNotNull(component.bar);
        assertEquals(component.bar, component.foo.bar);

        dcmFoo.dispose();
        assertTrue(component.disposeCalled);
    }
}
