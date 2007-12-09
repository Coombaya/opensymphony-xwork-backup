/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork2.config.providers;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionChainResult;
import com.opensymphony.xwork2.ModelDrivenAction;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.SimpleAction;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.ConfigurationProvider;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.InterceptorConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.config.entities.InterceptorMapping;
import com.opensymphony.xwork2.inject.ContainerBuilder;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.ModelDrivenInterceptor;
import com.opensymphony.xwork2.interceptor.ParametersInterceptor;
import com.opensymphony.xwork2.interceptor.StaticParametersInterceptor;
import com.opensymphony.xwork2.mock.MockResult;
import com.opensymphony.xwork2.ognl.OgnlReflectionProvider;
import com.opensymphony.xwork2.util.location.LocatableProperties;
import com.opensymphony.xwork2.util.reflection.ReflectionProvider;
import com.opensymphony.xwork2.validator.ValidationInterceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * MockConfigurationProvider provides a simple configuration class without the need for xml files, etc. for simple testing.
 *
 * @author $author$
 * @version $Revision$
 */
public class MockConfigurationProvider implements ConfigurationProvider {

    public static final String FOO_ACTION_NAME = "foo";
    public static final String MODEL_DRIVEN_PARAM_TEST = "modelParamTest";
    public static final String MODEL_DRIVEN_PARAM_FILTER_TEST  = "modelParamFilterTest";
    public static final String PARAM_INTERCEPTOR_ACTION_NAME = "parametersInterceptorTest";
    public static final String VALIDATION_ACTION_NAME = "validationInterceptorTest";
    public static final String VALIDATION_ALIAS_NAME = "validationAlias";
    public static final String VALIDATION_SUBPROPERTY_NAME = "subproperty";
    private Configuration configuration;
    private Map<String,String> params;
    private ObjectFactory objectFactory;

    public MockConfigurationProvider() {}
    public MockConfigurationProvider(Map<String,String> params) {
        this.params = params;
    }

    /**
     * Allows the configuration to clean up any resources used
     */
    public void destroy() {
    }
    
    public void init(Configuration config) {
        this.configuration = config;
    }
    
    @Inject
    public void setObjectFactory(ObjectFactory fac) {
        this.objectFactory = fac;
    }

    public void loadPackages() {
        
        PackageConfig.Builder defaultPackageContext = new PackageConfig.Builder("defaultPackage");
        HashMap params = new HashMap();
        params.put("bar", "5");

        HashMap results = new HashMap();
        HashMap successParams = new HashMap();
        successParams.put("actionName", "bar");
        results.put("success", new ResultConfig.Builder("success", ActionChainResult.class.getName()).addParams(successParams).build());

        ActionConfig fooActionConfig = new ActionConfig.Builder("defaultPackage", FOO_ACTION_NAME, SimpleAction.class.getName())
            .addResultConfig(new ResultConfig.Builder(Action.ERROR, MockResult.class.getName()).build())
            .build();
        defaultPackageContext.addActionConfig(FOO_ACTION_NAME, fooActionConfig);

        results = new HashMap();
        successParams = new HashMap();
        successParams.put("actionName", "bar");
        results.put("success", new ResultConfig.Builder("success", ActionChainResult.class.getName()).addParams(successParams).build());

        List interceptors = new ArrayList();
        interceptors.add(new InterceptorMapping("params", new ParametersInterceptor()));

        ActionConfig paramInterceptorActionConfig = new ActionConfig.Builder("defaultPackage", PARAM_INTERCEPTOR_ACTION_NAME, SimpleAction.class.getName())
            .addResultConfig(new ResultConfig.Builder(Action.ERROR, MockResult.class.getName()).build())
            .addInterceptors(interceptors)
            .build();
        defaultPackageContext.addActionConfig(PARAM_INTERCEPTOR_ACTION_NAME, paramInterceptorActionConfig);

        interceptors = new ArrayList();
        interceptors.add(new InterceptorMapping("model", 
                objectFactory.buildInterceptor(new InterceptorConfig.Builder("model", ModelDrivenInterceptor.class.getName()).build(), new HashMap())));
        interceptors.add(new InterceptorMapping("params",
                objectFactory.buildInterceptor(new InterceptorConfig.Builder("model", ParametersInterceptor.class.getName()).build(), new HashMap())));

        ActionConfig modelParamActionConfig = new ActionConfig.Builder("defaultPackage", MODEL_DRIVEN_PARAM_TEST, ModelDrivenAction.class.getName())
            .addInterceptors(interceptors)
            .addResultConfig(new ResultConfig.Builder(Action.SUCCESS, MockResult.class.getName()).build())
            .build();
        defaultPackageContext.addActionConfig(MODEL_DRIVEN_PARAM_TEST, modelParamActionConfig);
        
        //List paramFilterInterceptor=new ArrayList();
        //paramFilterInterceptor.add(new ParameterFilterInterC)
        //ActionConfig modelParamFilterActionConfig = new ActionConfig(null, ModelDrivenAction.class, null, null, interceptors);
        

        results = new HashMap();
        successParams = new HashMap();
        successParams.put("actionName", "bar");
        results.put("success", new ResultConfig.Builder("success", ActionChainResult.class.getName()).addParams(successParams).build());
        results.put(Action.ERROR, new ResultConfig.Builder(Action.ERROR, MockResult.class.getName()).build());

        interceptors = new ArrayList();
        interceptors.add(new InterceptorMapping("static-params", 
                objectFactory.buildInterceptor(new InterceptorConfig.Builder("model", StaticParametersInterceptor.class.getName()).build(), new HashMap())));
        interceptors.add(new InterceptorMapping("model", 
                objectFactory.buildInterceptor(new InterceptorConfig.Builder("model", ModelDrivenInterceptor.class.getName()).build(), new HashMap())));
        interceptors.add(new InterceptorMapping("params", 
                objectFactory.buildInterceptor(new InterceptorConfig.Builder("model", ParametersInterceptor.class.getName()).build(), new HashMap())));
        interceptors.add(new InterceptorMapping("validation", 
                objectFactory.buildInterceptor(new InterceptorConfig.Builder("model", ValidationInterceptor.class.getName()).build(), new HashMap())));

        //Explicitly set an out-of-range date for DateRangeValidatorTest
        params = new HashMap();
        params.put("date", new java.util.Date(2002 - 1900, 11, 20));

        //Explicitly set an out-of-range double for DoubleRangeValidatorTest
        params.put("percentage", new Double(100.0123));

        ActionConfig validationActionConfig = new ActionConfig.Builder("defaultPackage", VALIDATION_ACTION_NAME, SimpleAction.class.getName())
            .addInterceptors(interceptors)
            .addParams(params)
            .addResultConfigs(results)
            .build();
        defaultPackageContext.addActionConfig(VALIDATION_ACTION_NAME, validationActionConfig);
        defaultPackageContext.addActionConfig(VALIDATION_ALIAS_NAME,
                new ActionConfig.Builder(validationActionConfig).name(VALIDATION_ALIAS_NAME).build());
        defaultPackageContext.addActionConfig(VALIDATION_SUBPROPERTY_NAME,
                new ActionConfig.Builder(validationActionConfig).name(VALIDATION_SUBPROPERTY_NAME).build());


        params = new HashMap();
        params.put("percentage", new Double(1.234567));
        ActionConfig percentageActionConfig = new ActionConfig.Builder("defaultPackage", "percentage", SimpleAction.class.getName())
                .addParams(params)
                .addResultConfigs(results)
                .addInterceptors(interceptors)
                .build();
        defaultPackageContext.addActionConfig(percentageActionConfig.getName(), percentageActionConfig);

        // We need this actionconfig to be the final destination for action chaining
        ActionConfig barActionConfig = new ActionConfig.Builder("defaultPackage", "bar", SimpleAction.class.getName())
                .addResultConfig(new ResultConfig.Builder(Action.ERROR, MockResult.class.getName()).build())
                .build();
        defaultPackageContext.addActionConfig(barActionConfig.getName(), barActionConfig);

        configuration.addPackageConfig("defaultPackage", defaultPackageContext.build());
    }

    /**
     * Tells whether the ConfigurationProvider should reload its configuration
     *
     * @return false
     */
    public boolean needsReload() {
        return false;
    }

    public void register(ContainerBuilder builder, LocatableProperties props) throws ConfigurationException {
        if (params != null) {
            for (String key : params.keySet()) {
                props.setProperty(key, params.get(key));
            }
        }
    }
}
