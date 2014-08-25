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
package org.lorislab.transformer.adapter.gson;

import com.google.gson.Gson;
import org.kohsuke.MetaInfServices;
import org.lorislab.transformer.api.service.AdapterService;

/**
 * The GSON adapter service.
 * 
 * @author Andrej Petras
 */
@MetaInfServices
public class GsonAdapterService implements AdapterService {

    /**
     * The GSON instance.
     */
    private static final Gson INSTANCE = new Gson();
    
    /**
     * {@inheritDoc }
     */
    @Override
    public <T> T readValue(String value, Class<T> clazz) {
        T result = INSTANCE.fromJson(value, clazz);
        return result;
    }

    /**
     * {@inheritDoc }
     */    
    @Override
    public String writeValue(Object value, Class clazz) {
        String result = INSTANCE.toJson(value);
        return result;
    }

}
