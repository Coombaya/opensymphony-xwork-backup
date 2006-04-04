/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.config.entities;

import java.util.Map;
import java.util.LinkedHashMap;
import java.io.Serializable;

import com.opensymphony.xwork.util.location.Located;


/**
 * Configuration class for result types.
 * <p/>
 * In the xml configuration file this is defined as the <code>result-type</code> tag.
 *
 * @author Mike
 * @author Rainer Hermanns
 * @author Neo
 */
public class ResultTypeConfig extends Located implements Serializable {

    private Class clazz;
    private String name;

    private Map params;

    public ResultTypeConfig() {
    }

    public ResultTypeConfig(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }


    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addParam(String key, String value) {
        if (params == null) {
            params = new LinkedHashMap();
        }
        params.put(key, value);
    }

    public Map getParams() {
        return this.params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ResultTypeConfig that = (ResultTypeConfig) o;

        if (clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (params != null ? !params.equals(that.params) : that.params != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (clazz != null ? clazz.hashCode() : 0);
        result = 29 * result + (name != null ? name.hashCode() : 0);
        result = 29 * result + (params != null ? params.hashCode() : 0);
        return result;
    }
}
