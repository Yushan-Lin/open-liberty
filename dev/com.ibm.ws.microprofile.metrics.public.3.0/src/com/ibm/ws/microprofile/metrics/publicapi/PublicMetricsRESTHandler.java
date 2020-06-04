/*******************************************************************************
 * Copyright (c) 2018, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.microprofile.metrics.publicapi;

import java.io.IOException;
import java.util.Map;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import com.ibm.websphere.ras.Tr;
import com.ibm.websphere.ras.TraceComponent;
import com.ibm.ws.microprofile.metrics.Constants;
import com.ibm.ws.microprofile.metrics30.impl.SharedMetricRegistries;
import com.ibm.ws.microprofile.metrics30.BaseMetricsHandler30;
import com.ibm.wsspi.rest.handler.RESTHandler;

import io.smallrye.metrics.MetricsRequestHandler;
import io.smallrye.metrics.setup.JmxRegistrar;

@Component(service = { RESTHandler.class }, configurationPolicy = ConfigurationPolicy.IGNORE, immediate = true, property = { "service.vendor=IBM",
                                                                                                                             RESTHandler.PROPERTY_REST_HANDLER_CONTEXT_ROOT + "="
                                                                                                                                                   + Constants.PATH_METRICS,
                                                                                                                             RESTHandler.PROPERTY_REST_HANDLER_ROOT + "=" + Constants.PATH_ROOT,
                                                                                                                             RESTHandler.PROPERTY_REST_HANDLER_ROOT + "=" + Constants.PATH_SUB,
                                                                                                                             RESTHandler.PROPERTY_REST_HANDLER_ROOT + "=" + Constants.PATH_SUB_ATTRIBUTE })
public class PublicMetricsRESTHandler extends BaseMetricsHandler30 {
    private static final TraceComponent tc = Tr.register(PublicMetricsRESTHandler.class);

    @Activate
    protected void activate(ComponentContext context, Map<String, Object> properties) throws IOException {
    	if (jmxRegistrar == null) {
    		jmxRegistrar = new JmxRegistrar();
    		jmxRegistrar.init();
    	}
    	if (mrHandler == null) {
    		mrHandler = new MetricsRequestHandler();
    	}
        for (String registry : Constants.REGISTRY_NAMES_LIST) {
            sharedMetricRegistry.getOrCreate(registry);
        }
    }

    @Reference
    public void setSharedMetricRegistries(SharedMetricRegistries sharedMetricRegistry) {
        this.sharedMetricRegistry = sharedMetricRegistry;
    }

    @Deactivate
    protected void deactivate(ComponentContext context, int reason) {}
}
