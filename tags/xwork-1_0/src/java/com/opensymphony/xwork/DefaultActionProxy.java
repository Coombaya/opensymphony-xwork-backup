/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork;

import com.opensymphony.xwork.config.ConfigurationException;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.entities.ActionConfig;
import com.opensymphony.xwork.util.LocalizedTextUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Locale;
import java.util.Map;


/**
 * The DefaultActionProxy is an extra layer between XWork and the action so that different proxies are possible.
 *
 * An example of this would be a remote proxy, where the layer between XWork and the action might be RMI or SOAP.
 *
 * @author $Author$
 * @version $Revision$
 */
public class DefaultActionProxy implements ActionProxy {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final Log LOG = LogFactory.getLog(DefaultActionProxy.class);

    //~ Instance fields ////////////////////////////////////////////////////////

    protected ActionConfig config;
    protected ActionInvocation invocation;
    protected Map extraContext;
    protected String actionName;
    protected String namespace;
    protected boolean executeResult;

    //~ Constructors ///////////////////////////////////////////////////////////

    /**
    * This constructor is private so the builder methods (create*) should be used to create an DefaultActionProxy.
    *
    * The reason for the builder methods is so that you can use a subclass to create your own DefaultActionProxy instance
    * (like a RMIActionProxy).
    */
    protected DefaultActionProxy(String namespace, String actionName, Map extraContext, boolean executeResult) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating an DefaultActionProxy for namespace " + namespace + " and action name " + actionName);
        }

        this.actionName = actionName;
        this.namespace = namespace;
        this.executeResult = executeResult;
        this.extraContext = extraContext;
        config = ConfigurationManager.getConfiguration().getRuntimeConfiguration().getActionConfig(namespace, actionName);

        if (config == null) {
            String message;

            if ((namespace != null) && (namespace.trim().length() > 0)) {
                message = LocalizedTextUtil.findDefaultText(XWorkMessages.MISSING_PACKAGE_ACTION_EXCEPTION, Locale.getDefault(), new String[] {
                            namespace, actionName
                        });
            } else {
                message = LocalizedTextUtil.findDefaultText(XWorkMessages.MISSING_ACTION_EXCEPTION, Locale.getDefault(), new String[] {
                            actionName
                        });
            }

            throw new ConfigurationException(message);
        }

        prepare();
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public Action getAction() {
        return invocation.getAction();
    }

    public String getActionName() {
        return actionName;
    }

    public ActionConfig getConfig() {
        return config;
    }

    public void setExecuteResult(boolean executeResult) {
        this.executeResult = executeResult;
    }

    public boolean getExecuteResult() {
        return executeResult;
    }

    public ActionInvocation getInvocation() {
        return invocation;
    }

    public String getNamespace() {
        return namespace;
    }

    public String execute() throws Exception {
        ActionContext nestedContext = ActionContext.getContext();
        ActionContext.setContext(invocation.getInvocationContext());

        String retCode = null;

        try {
            retCode = invocation.invoke();
        } finally {
            ActionContext.setContext(nestedContext);
        }

        return retCode;
    }

    protected void prepare() throws Exception {
        invocation = ActionProxyFactory.getFactory().createActionInvocation(this, extraContext);
    }
}