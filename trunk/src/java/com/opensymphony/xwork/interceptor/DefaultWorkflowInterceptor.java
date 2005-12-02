/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.interceptor;

import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Validateable;
import com.opensymphony.xwork.ValidationAware;
import com.opensymphony.xwork.util.TextParseUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.Set;


/**
 * <!-- START SNIPPET: description -->
 *
 * An interceptor that does some basic validation workflow before allowing the interceptor chain to continue.
 *
 * <p/>This interceptor does nothing if the name of the method being invoked
 * is specified in the <b>excludeMethods</b> parameter. <b>excludeMethods</b>
 * accepts a comma-delimited list of method names. For example, requests to
 * <b>foo!input.action</b> and <b>foo!back.action</b> will be skipped by this
 * interceptor if you set the <b>excludeMethods</b> parameter to "input,
 * back".
 *
 * <p/>The order of execution in the workflow is:
 *
 * <ol>
 *
 * <li>If the action being executed implements {@link Validateable}, the action's {@link Validateable#validate()
 * validate} method is called.</li>
 *
 * <li>Next, if the action implements {@link ValidationAware}, the action's {@link ValidationAware#hasErrors()
 * hasErrors} method is called. If this method returns true, this interceptor stops the chain from continuing and
 * immediately returns {@link Action#INPUT}</li>
 *
 * </ol>
 *
 * <p/> Note: if the action doesn't implement either interface, this interceptor effectively does nothing. This
 * interceptor is often used with the <b>validation</b> interceptor. However, it does not have to be, especially if you
 * wish to write all your validation rules by hand in the validate() method rather than in XML files.
 *
 * <!-- END SNIPPET: description -->
 *
 * <p/> <u>Interceptor parameters:</u>
 *
 * <!-- START SNIPPET: parameters -->
 *
 * <ul>
 *
 * <li>None</li>
 *
 * </ul>
 *
 * <!-- END SNIPPET: parameters -->
 *
 * <p/> <u>Extending the interceptor:</u>
 *
 * <p/>
 *
 * <!-- START SNIPPET: extending -->
 *
 * There are no known extension points for this interceptor.
 *
 * <!-- END SNIPPET: extending -->
 *
 * <p/> <u>Example code:</u>
 *
 * <pre>
 * <!-- START SNIPPET: example -->
 * &lt;action name="someAction" class="com.examples.SomeAction"&gt;
 *     &lt;interceptor-ref name="params"/&gt;
 *     &lt;interceptor-ref name="validation"/&gt;
 *     &lt;interceptor-ref name="workflow"/&gt;
 *     &lt;result name="success"&gt;good_result.ftl&lt;/result&gt;
 * &lt;/action&gt;
 * <!-- END SNIPPET: example -->
 * </pre>
 *
 * @author Jason Carreira
 */
public class DefaultWorkflowInterceptor implements Interceptor {

    Log log = LogFactory.getLog(this.getClass());

    Set excludeMethods = Collections.EMPTY_SET;

    public void setExcludeMethods(String excludeMethods) {
        this.excludeMethods = TextParseUtil.commaDelimitedStringToSet(excludeMethods);
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        if (excludeMethods.contains(invocation.getProxy().getMethod())) {
            log.debug("Skipping workflow. Method found in exclude list.");
            return invocation.invoke();
        }

        Object action = invocation.getAction();

        if (action instanceof Validateable) {
            Validateable validateable = (Validateable) action;
            validateable.validate();
        }

        if (action instanceof ValidationAware) {
            ValidationAware validationAwareAction = (ValidationAware) action;

            if (validationAwareAction.hasErrors()) {
                return Action.INPUT;
            }
        }

        return invocation.invoke();
    }

    public void destroy() {}
    public void init() {}
}
