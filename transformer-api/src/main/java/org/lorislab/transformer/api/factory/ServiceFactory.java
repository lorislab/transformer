/*
 * Copyright 2014 Andrej Petras.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lorislab.transformer.api.factory;

import java.util.Iterator;
import java.util.ServiceLoader;
import org.lorislab.transformer.api.service.AdapterService;

/**
 * The service factory.
 *
 * @author Andrej Petras
 */
public final class ServiceFactory {

    /**
     * The adapter service instance.
     */
    private static AdapterService ATTR_ADAPTER_SERVICE;

    /**
     * Loads the service instance.
     */
    static {
        ServiceLoader<AdapterService> loader2 = ServiceLoader.load(AdapterService.class);
        Iterator<AdapterService> iter2 = loader2.iterator();
        if (iter2.hasNext()) {
            ATTR_ADAPTER_SERVICE = iter2.next();
        }
    }

    /**
     * The default constructor.
     */
    private ServiceFactory() {
        // empty constructor
    }

    /**
     * Gets the adapter service instance.
     *
     * @return the adapter service instance.
     */
    public static AdapterService getAdapterService() {
        return ATTR_ADAPTER_SERVICE;
    }
}
