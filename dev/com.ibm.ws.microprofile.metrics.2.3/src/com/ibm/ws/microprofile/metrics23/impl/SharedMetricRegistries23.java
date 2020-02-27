/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.microprofile.metrics23.impl;

import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.osgi.service.component.annotations.Component;

import com.ibm.ws.microprofile.metrics.impl.SharedMetricRegistries;

/**
 * A map of shared, named metric registries.
 */
@Component(service = SharedMetricRegistries.class, immediate = true)
public class SharedMetricRegistries23 extends SharedMetricRegistries {
    @Override
    public MetricRegistry getOrCreate(String name) {
        final MetricRegistry existing = SharedMetricRegistries.REGISTRIES.get(name);
        if (existing == null) {
            final MetricRegistry created = createNewMetricRegsitry(configResolver, name);
            final MetricRegistry raced = add(name, created);
            if (raced == null) {
                return created;
            }
            return raced;
        }
        return existing;
    }

//    @Override
    protected MetricRegistry createNewMetricRegsitry(ConfigProviderResolver configResolver, String name) {
        return new MetricRegistry23Impl(configResolver, name);
    }

}
