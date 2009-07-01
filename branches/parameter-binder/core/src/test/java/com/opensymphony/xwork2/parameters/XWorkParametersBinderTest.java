/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.opensymphony.xwork2.parameters;

import com.opensymphony.xwork2.SimpleAction;
import com.opensymphony.xwork2.XWorkTestCase;

import java.util.HashMap;
import java.util.Map;

import ognl.OgnlException;

public class XWorkParametersBinderTest extends XWorkTestCase {
    private XWorkParametersBinder binder;

    public void testSimple() throws ParseException, OgnlException {
        String expr = "name";
        SimpleAction action = new SimpleAction();

        assertNull(action.getName());

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getName());
    }

    public void testPropertyAsIndex() throws ParseException, OgnlException {
        String expr = "['name']";
        SimpleAction action = new SimpleAction();

        assertNull(action.getName());

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getName());
    }

    public void testPropertyAsIndexEmptyString() throws ParseException, OgnlException {
        String expr = "['']";
        SimpleAction action = new SimpleAction();

        assertNull(action.getName());

        Map<String, Object> context = new HashMap<String, Object>();
        try {
            binder.setProperty(context, action, expr, "Lex Luthor");
            fail("should have failed");
        } catch (Exception e) {
            //ok
        }
    }

    public void testNested() throws ParseException, OgnlException {
        String expr = "bean.name";
        SimpleAction action = new SimpleAction();

        assertNotNull(action.getBean());
        assertNull(action.getBean().getName());

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getBean().getName());
    }

    public void testNestedNull() throws ParseException, OgnlException {
        String expr = "bean.name";
        SimpleAction action = new SimpleAction();
        action.setBean(null);

        assertNull(action.getBean());

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getBean().getName());
    }

    //Maps

    public void testSimpleMap() throws ParseException, OgnlException {
        String expr = "theExistingMap['Name']";
        SimpleAction action = new SimpleAction();

        assertNull(action.getTheExistingMap().get("Name"));

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getTheExistingMap().get("Name"));
    }

    public void testSimpleMapNull() throws ParseException, OgnlException {
        String expr = "someMap['Name']";
        SimpleAction action = new SimpleAction();
        action.setExistingMap(null);

        assertNull(action.getSomeMap());

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getSomeMap().get("Name"));
    }

     public void testSimplePropertyOnObjectInMap() throws ParseException, OgnlException {
        String expr = "otherMap['my_hero'].name";
        SimpleAction action = new SimpleAction();

        assertNull(action.getOtherMap().get("my_hero"));

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getOtherMap().get("my_hero").getName());
    }

    public void testSimplePropertyOnObjectInMapNull() throws ParseException, OgnlException {
        String expr = "otherMap['my_hero'].name";
        SimpleAction action = new SimpleAction();
        action.setOtherMap(null);

        assertNull(action.getOtherMap());

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getOtherMap().get("my_hero").getName());
    }

    public void testSimplePropertyOnObjectInList() throws ParseException, OgnlException {
        String expr = "otherList[0].name";
        SimpleAction action = new SimpleAction();

        assertEquals(0, action.getOtherList().size());

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getOtherList().get(0).getName());
    }

    public void testSimplePropertyOnObjectInListNull() throws ParseException, OgnlException {
        String expr = "otherList[0].name";
        SimpleAction action = new SimpleAction();
        action.setOtherList(null);

        assertNull(action.getOtherList());

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getOtherList().get(0).getName());
    }

    //Lists
    public void testSimpleList() throws ParseException, OgnlException {
        String expr = "someList[0]";
        SimpleAction action = new SimpleAction();

        assertEquals(0, action.getSomeList().size());

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getSomeList().get(0));
    }

    public void testSimpleListNull() throws ParseException, OgnlException {
        String expr = "someList[0]";
        SimpleAction action = new SimpleAction();
        action.setSomeList(null);

        assertNull(action.getSomeList());

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getSomeList().get(0));
    }

    public void testSimpleListNullRandomIndex() throws ParseException, OgnlException {
        String expr = "someList[15]";
        SimpleAction action = new SimpleAction();
        action.setSomeList(null);

        assertNull(action.getSomeList());

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getSomeList().get(15));
    }

    public void testNestedNull2() throws ParseException, OgnlException {
        String expr = "nestedAction.bean.name";
        SimpleAction action = new SimpleAction();
        action.setNestedAction(null);

        assertNull(action.getNestedAction());

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getNestedAction().getBean().getName());
    }

    public void testNestedNull3() throws ParseException, OgnlException {
        String expr = "nestedAction.nestedAction.bean.name";
        SimpleAction action = new SimpleAction();
        action.setNestedAction(null);

        assertNull(action.getNestedAction());

        Map<String, Object> context = new HashMap<String, Object>();
        binder.setProperty(context, action, expr, "Lex Luthor");

        assertEquals("Lex Luthor", action.getNestedAction().getBean().getName());
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.binder = container.getInstance(XWorkParametersBinder.class);
    }
}
