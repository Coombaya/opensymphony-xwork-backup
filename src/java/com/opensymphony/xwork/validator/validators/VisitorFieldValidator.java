/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.validator.validators;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.validator.ActionValidatorManager;
import com.opensymphony.xwork.validator.DelegatingValidatorContext;
import com.opensymphony.xwork.validator.ValidationException;
import com.opensymphony.xwork.validator.ValidatorContext;

import java.util.Collection;
import java.util.Iterator;


/**
 * VisitorFieldValidator
 * @author Jason Carreira
 * Created Aug 2, 2003 10:27:48 PM
 */
public class VisitorFieldValidator extends FieldValidatorSupport {
    //~ Instance fields ////////////////////////////////////////////////////////

    private String context;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setContext(String context) {
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    public void validate(Object object) throws ValidationException {
        String fieldName = getFieldName();
        Object value = this.getFieldValue(fieldName, object);
        String visitorContext = (context == null) ? ActionContext.getContext().getName() : context;

        if (value == null) {
            return;
        }

        if (value instanceof Collection) {
            Collection coll = (Collection) value;

            for (Iterator iterator = coll.iterator(); iterator.hasNext();) {
                Object o = iterator.next();
                ValidatorContext validatorContext = new AppendingValidatorContext(getValidatorContext(), fieldName, getMessage(o));
                ActionValidatorManager.validate(o, visitorContext, validatorContext);
            }
        } else if (value instanceof Object[]) {
            Object[] array = (Object[]) value;

            for (int i = 0; i < array.length; i++) {
                Object o = array[i];
                ValidatorContext validatorContext = new AppendingValidatorContext(getValidatorContext(), fieldName, getMessage(o));
                ActionValidatorManager.validate(o, visitorContext, validatorContext);
            }
        } else {
            ValidatorContext validatorContext = new AppendingValidatorContext(getValidatorContext(), fieldName, getMessage(value));
            ActionValidatorManager.validate(value, visitorContext, validatorContext);
        }
    }

    //~ Inner Classes //////////////////////////////////////////////////////////

    private class AppendingValidatorContext extends DelegatingValidatorContext {
        String field;
        String message;

        public AppendingValidatorContext(Object object, String field, String message) {
            super(object);
            this.field = field;
            this.message = message;
        }

        public void addActionError(String anErrorMessage) {
            super.addFieldError(field, message + anErrorMessage);
        }

        public void addFieldError(String fieldName, String errorMessage) {
            super.addFieldError(field + "." + fieldName, message + errorMessage);
        }
    }
}
