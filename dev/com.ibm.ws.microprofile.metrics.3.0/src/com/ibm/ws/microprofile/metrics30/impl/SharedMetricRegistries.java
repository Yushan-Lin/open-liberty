/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.microprofile.metrics30.impl;

import org.eclipse.microprofile.metrics.MetricID;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricRegistry.Type;
import org.osgi.service.component.annotations.Component;

import io.smallrye.metrics.MetricRegistries;
import io.smallrye.metrics.MetricsRegistryImpl;

/**
 * A map of shared, named metric registries.
 */
@Component(service = SharedMetricRegistries.class, immediate = true)
public class SharedMetricRegistries {
//    protected static final ConcurrentMap<String, MetricRegistry> REGISTRIES = new ConcurrentHashMap<String, MetricRegistry>();

    public static void clear() {
        System.out.println("REMOVE ALL");
        MetricRegistries.dropAll();
    }

//don't see this being used
//    public static Set<String> names() {
//        return REGISTRIES.keySet();
//    }
    public static void remove(String key) {
        System.out.println("REMOVE!!");
        Type type = typeOf(key);
        MetricRegistries.drop(type);
    }

//    @Override
    public MetricRegistry add(String name, MetricRegistry registry) {
        Type type = typeOf(name);
        return MetricRegistries.get(type);
    }

//    @Override
    public MetricRegistry getOrCreate(String name) {
        Type type = typeOf(name);
        final MetricRegistry existing = MetricRegistries.get(type);
        if (existing == null) {
            final MetricRegistry created = createNewMetricRegsitry(type);
            final MetricRegistry raced = add(name, created);
            if (raced == null) {
                return created;
            }
            return raced;
        }
        return existing;
    }

//    @Override
//    @Reference(service = ConfigProviderResolver.class, cardinality = ReferenceCardinality.MANDATORY)
//    protected void setConfigProvider(ConfigProviderResolver configResolver) {
//        this.configResolver = configResolver;
//    }

//    @Override
    protected MetricRegistry createNewMetricRegsitry(Type type) {
//        return new MetricsRegistryImpl(configResolver);
        return new MetricsRegistryImpl(type);

    }

//    @Override
    public void associateMetricIDToApplication(MetricID metricID, String appName, MetricRegistry registry) {
        if (MetricsRegistryImpl.class.isInstance(registry)) {
            MetricsRegistryImpl metricRegistryImpl = (MetricsRegistryImpl) registry;
            metricRegistryImpl.addNameToApplicationMap(metricID, appName);

        }

    }

    protected static Type typeOf(String name) {
        if (name.equals("base")) {
            return Type.BASE;
        } else if (name.equals("vendor")) {
            return Type.VENDOR;
        } else if (name.equals("application")) {
            return Type.APPLICATION;
        } else {
            throw new IllegalArgumentException("Name of registry must be base vendor or application");
        }

    }

}
