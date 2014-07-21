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
package org.lorislab.transformer.api;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lorislab.transformer.api.factory.ServiceFactory;

/**
 * The model utility class.
 *
 * @author Andrej Petras
 */
public final class Transformer {

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(Transformer.class.getName());

    /**
     * The default constructor.
     */
    private Transformer() {
        // empty cosntructor
    }
    
    /**
     * Transform the object to map of string.
     *
     * @param data the input object.
     * @return the corresponding map of field names and string values.
     */
    public static Map<String, String> transform(Object data) {
        Map<String, String> result = new HashMap<>();
        if (data != null) {
            Class clazz = data.getClass();
            Map<String, Field> fields = getFields(clazz);
            if (fields != null) {
                for (Field field : fields.values()) {

                    // get the value
                    Object value = null;
                    try {
                        boolean accessible = field.isAccessible();
                        try {
                            field.setAccessible(true);
                            value = field.get(data);
                        } finally {
                            field.setAccessible(accessible);
                        }
                    } catch (IllegalAccessException | IllegalArgumentException | SecurityException ex) {
                        LOGGER.log(Level.SEVERE, "Error read the value for the field {0}", field.getName());
                    }

                    // transform the value
                    String tmp = ServiceFactory.getAdapterService().writeValue(value, field.getType());
                    result.put(field.getName(), tmp);
                }
            }
        }
        return result;
    }

    /**
     * Transforms the map of strings to the instance of the class.
     * 
     * @param <T> the instance type.
     * @param data the map of strings.
     * @param clazz the class.
     * @return the corresponding instance.
     */
    public static <T> T transform(Map<String, String> data, Class<T> clazz) {
        T result = null;
        if (data != null && clazz != null) {

            // create instance
            try {
                result = clazz.newInstance();
            } catch (IllegalAccessException | InstantiationException ex) {
                LOGGER.log(Level.SEVERE, "Error create the instance for the " + clazz.getName(), ex);
            }

            if (result != null) {

                Map<String, Field> fields = getFields(clazz);
                for (Entry<String, String> entry : data.entrySet()) {

                    Field field = fields.get(entry.getKey());
                    if (field != null) {

                        // transform the value
                        Object tmp = ServiceFactory.getAdapterService().readValue(entry.getValue(), field.getType());

                        // set the value
                        boolean accessible = field.isAccessible();
                        try {
                            field.setAccessible(true);
                            field.set(result, tmp);
                        } catch (IllegalAccessException | IllegalArgumentException | SecurityException ex) {
                            LOGGER.log(Level.SEVERE, "Error set the value to the instance. Class {0} field {1}", new Object[]{clazz.getName(), field.getName()});
                        } finally {
                            field.setAccessible(accessible);
                        }

                    } else {
                        LOGGER.log(Level.FINE, "The field {0} is missing in the class {1}", new Object[]{entry.getKey(), clazz.getName()});
                    }
                }
            }
        }
        return result;
    }

    /**
     * Transforms the map of strings to the instance of the class.
     * 
     * @param <T> the instance type.
     * @param data the map of strings.
     * @param name the name of the class.
     * @return the corresponding instance.
     */    
    public static <T> T transform(Map<String, String> data, String name) {
        T result = null;
        if (name != null && data != null) {

            // load the class by name.
            Class<T> clazz = null;
            try {
                clazz = (Class<T>) Class.forName(name);
            } catch (ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Error load the class " + name, ex);
            }

            if (clazz != null) {
                result = transform(data, clazz);
            }
        }
        return result;
    }

    /**
     * Gets the list of fields for the class.
     *
     * @param clazz the class.
     * @return the corresponding list of fields.
     */
    private static Map<String, Field> getFields(Class clazz) {
        Map<String, Field> result = new HashMap<>();
        if (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        result.put(field.getName(), field);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Gets the list of fields for the class.
     *
     * @param clazz the class.
     * @return the corresponding list of fields.
     */
    public static Set<String> getFieldNames(Class clazz) {
        Set<String> result = new HashSet<>();
        if (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        result.add(field.getName());
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Creates object instance by class name.
     *
     * @param name the class name.
     * @return the corresponding object instance.
     */
    public static Object createInstance(String name) {
        Object result = null;
        try {
            Class clazz = Class.forName(name);
            result = clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return result;
    }    
}
