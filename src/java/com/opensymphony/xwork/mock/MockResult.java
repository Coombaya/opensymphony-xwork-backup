/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.mock;

import com.opensymphony.xwork.Result;
import com.opensymphony.xwork.ActionInvocation;

/**
 * Mock for a {@link Result}.
 *
 * @author Mike
 * @author Rainer Hermanns
 */
public class MockResult implements Result {

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof MockResult)) {
            return false;
        }

        return true;
    }

    public void execute(ActionInvocation invocation) throws Exception {
        System.out.println("MockResult.execute");
    }

    public int hashCode() {
        return 10;
    }
}
