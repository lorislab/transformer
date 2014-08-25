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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.junit.Test;
import org.lorislab.transformer.api.factory.ServiceFactory;
import org.lorislab.transformer.api.service.AdapterService;

/**
 * The GSON adapter service test.
 *
 * @author Andrej Petras
 */
public class GsonAdapterServiceTest {

    /**
     * The basic write test.
     */
    @Test
    public void writeTest() {
        AdapterService service = ServiceFactory.getAdapterService();
        write(service, "1");
        write(service, 100);
        write(service, 234.5d);
        write(service, Boolean.TRUE);
        Set<String> set = new HashSet<>(Arrays.asList("12", "234", "fdhsjhdfskj"));
        write(service, set);
        write(service, Locale.CANADA);
    }

    /**
     * Writes output.
     *
     * @param service the service instance.
     * @param value the value.
     */
    private void write(AdapterService service, Object value) {
        String tmp = service.writeValue(value, value.getClass());
        System.out.println(value.getClass().getName() + " : " + tmp);
    }

    /**
     * The basic read test.
     */
    @Test
    public void readTest() {
        AdapterService service = ServiceFactory.getAdapterService();

        String value = "\"1\"";
        read(service, value, String.class);

        value = "100";
        read(service, value, Integer.class);

        value = "true";
        read(service, value, Boolean.class);
    }

    /**
     * The read from string method.
     *
     * @param <T> the output type.
     * @param service the service.
     * @param value the value.
     * @param clazz the output class.
     */
    private <T> void read(AdapterService service, String value, Class<T> clazz) {
        T tmp = service.readValue(value, clazz);
        System.out.println(clazz.getName() + " : " + tmp);
    }
}
