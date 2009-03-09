/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork2.test;

import com.opensymphony.xwork2.conversion.annotations.ConversionRule;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.util.KeyProperty;
import com.opensymphony.xwork2.validator.annotations.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Test bean.
 *
 * @author Mark Woon
 * @author Rainer Hermanns
 */
@Validation(
        validations = @Validations(
                expressions = {
                    @ExpressionValidator(expression = "email.startsWith('mark')", message = "Email does not start with mark"),
                    @ExpressionValidator(expression = "email2.startsWith('mark')", message = "Email2 does not start with mark")
                }
        )
)
public class AnnotationUser implements AnnotationUserMarker {

    private Collection collection;
    private List list;
    private Map map;
    private String email;
    private String email2;
    private String name;


    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Collection getCollection() {
        return collection;
    }

    @EmailValidator(shortCircuit = true, message = "Not a valid e-mail.")
    @FieldExpressionValidator(expression = "email.endsWith('mycompany.com')", message = "Email not from the right company.")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @EmailValidator(message = "Not a valid e-mail2.")
    @FieldExpressionValidator(expression = "email2.endsWith('mycompany.com')", message = "Email2 not from the right company.")
    public void setEmail2(String email) {
        email2 = email;
    }

    public String getEmail2() {
        return email2;
    }

    public void setList(List l) {
        list = l;
    }

    @KeyProperty( value = "name")
    @TypeConversion( converter = "java.lang.String", rule = ConversionRule.COLLECTION)
    public List getList() {
        return list;
    }

    @TypeConversion( converter = "java.lang.String", rule = ConversionRule.MAP)
    public void setMap(Map m) {
        map = m;
    }

    public Map getMap() {
        return map;
    }

    @RequiredFieldValidator(key = "name.key", message = "You must enter a value for name.")
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}