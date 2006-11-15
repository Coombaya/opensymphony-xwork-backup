/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork2.interceptor;

import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;
import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.ConfigurationProvider;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.inject.ContainerBuilder;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;


/**
 * PreResultListenerTest
 *
 * @author Jason Carreira
 *         Date: Nov 13, 2003 11:16:43 PM
 */
public class PreResultListenerTest extends XWorkTestCase {

    private int count = 1;


    public void testPreResultListenersAreCalled() throws Exception {
        ActionProxy proxy = container.getInstance(ActionProxyFactory.class).createActionProxy(
                configurationManager.getConfiguration(), "package", "action", new HashMap(), false, true);
        ActionInvocation invocation = proxy.getInvocation();
        Mock preResultListenerMock1 = new Mock(PreResultListener.class);
        preResultListenerMock1.expect("beforeResult", C.args(C.eq(invocation), C.eq(Action.SUCCESS)));
        invocation.addPreResultListener((PreResultListener) preResultListenerMock1.proxy());
        proxy.execute();
        preResultListenerMock1.verify();
    }

    public void testPreResultListenersAreCalledInOrder() throws Exception {
        ActionProxy proxy = container.getInstance(ActionProxyFactory.class).createActionProxy(
                configurationManager.getConfiguration(), "package", "action", new HashMap(), false, true);
        ActionInvocation invocation = proxy.getInvocation();
        CountPreResultListener listener1 = new CountPreResultListener();
        CountPreResultListener listener2 = new CountPreResultListener();
        invocation.addPreResultListener(listener1);
        invocation.addPreResultListener(listener2);
        proxy.execute();
        assertNotNull(listener1.getMyOrder());
        assertNotNull(listener2.getMyOrder());
        assertEquals(listener1.getMyOrder().intValue() + 1, listener2.getMyOrder().intValue());
    }

    protected void setUp() throws Exception {
        super.setUp();
        configurationManager.clearConfigurationProviders();
        configurationManager.addConfigurationProvider(new ConfigurationProvider() {
            Configuration configuration;
            public void destroy() {
            }
            
            public void init(Configuration config) {
                this.configuration = config;
            }

            public void loadPackages() {
                PackageConfig packageConfig = new PackageConfig("package");
                ActionConfig actionConfig = new ActionConfig(null, SimpleFooAction.class, null, null, null);
                actionConfig.setPackageName("package");
                packageConfig.addActionConfig("action", actionConfig);
                configuration.addPackageConfig("package", packageConfig);
            }

            /**
             * Tells whether the ConfigurationProvider should reload its configuration
             *
             * @return
             */
            public boolean needsReload() {
                return false;
            }

            public void register(ContainerBuilder builder, Properties props, List<Class<?>> ignoreFailureStaticInjection) throws ConfigurationException {
                // TODO Auto-generated method stub
                
            }
        });
       configurationManager.reload();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }


    private class CountPreResultListener implements PreResultListener {
        private Integer myOrder = null;

        public Integer getMyOrder() {
            return myOrder;
        }

        public void beforeResult(ActionInvocation invocation, String resultCode) {
            myOrder = new Integer(count++);
        }
    }
}
