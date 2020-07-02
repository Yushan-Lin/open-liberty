/*******************************************************************************
 * Copyright (c) 2012, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.managedbeans.fat.mb.web;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Basic EJB Interceptor that appends to a call stack.
 **/
public class MBInterceptor {
    private static final String CLASS_NAME = MBInterceptor.class.getName();
    private static final String SIMPLE_NAME = MBInterceptor.class.getSimpleName();
    private static final Logger svLogger = Logger.getLogger(CLASS_NAME);

    @PostConstruct
    void postConstruct(InvocationContext inv) {
        svLogger.info("> " + SIMPLE_NAME + ".postConstruct()");

        InterceptorAccess bean = (InterceptorAccess) inv.getTarget();
        List<String> postConstructStack = bean.getPostConstructStack();
        postConstructStack.add(SIMPLE_NAME + ".postConstruct");

        try {
            inv.proceed();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RuntimeException("unexpected Exception", e);
        }

        svLogger.info("< " + SIMPLE_NAME + ".postConstruct()");
    }

    @AroundInvoke
    Object aroundInvoke(InvocationContext inv) throws Exception {
        svLogger.info("> " + SIMPLE_NAME + ".aroundInvoke()");

        Object[] parms = inv.getParameters();

        if (parms != null && parms.length > 0 && parms[0] instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<String> callStack = (List<String>) parms[0];

            callStack.add(SIMPLE_NAME + ".aroundInvoke");
        }

        Object rv = inv.proceed();

        svLogger.info("< " + SIMPLE_NAME + ".aroundInvoke()");
        return rv;
    }
}
