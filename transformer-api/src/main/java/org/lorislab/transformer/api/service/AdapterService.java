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
package org.lorislab.transformer.api.service;

/**
 * The adapter service.
 *
 * @author Andrej Petras
 */
public interface AdapterService {

    /**
     * Reads the value from string.
     *
     * @param <T> the instance type.
     * @param value the string value.
     * @param clazz the class.
     * @return the corresponding value.
     */
    public <T> T readValue(String value, Class<T> clazz);

    /**
     * Writes the value to the string.
     *
     * @param value the value.
     * @param clazz the class
     * @return the corresponding string.
     */
    public String writeValue(Object value, Class clazz);
}
