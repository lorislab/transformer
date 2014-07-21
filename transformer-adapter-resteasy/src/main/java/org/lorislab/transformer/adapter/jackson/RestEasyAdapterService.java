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
package org.lorislab.transformer.adapter.jackson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.kohsuke.MetaInfServices;
import org.lorislab.transformer.api.service.AdapterService;

/**
 *
 * @author Andrej Petras
 */
@MetaInfServices
public class RestEasyAdapterService implements AdapterService {

    private static final Logger LOGGER = Logger.getLogger(RestEasyAdapterService.class.getName());

    private static final ResteasyProviderFactory RPF = ResteasyProviderFactory.getInstance();
    private static final MediaType MEDIA_TYPE = MediaType.APPLICATION_JSON_TYPE;
    private static final String CHARSET_NAME = "UTF-8";

    @Override
    public <T> T readValue(String value, Class<T> clazz) {
        T result = null;
        InputStream stream = null;
        try {
            LOGGER.log(Level.FINEST, "Read the value from:{0}, type:{1}", new Object[]{value, clazz});
            MessageBodyReader<T> mr = RPF.getMessageBodyReader(clazz, clazz, null, MEDIA_TYPE);
            stream = new ByteArrayInputStream(value.getBytes(CHARSET_NAME));
            result = mr.readFrom(clazz, clazz, null, MEDIA_TYPE, null, stream);
            LOGGER.log(Level.FINEST, "Result:{0}", result);
        } catch (IOException | WebApplicationException ex) {
            LOGGER.log(Level.SEVERE, "Error reading the value from string " + value, ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "Error closing the output stream", ex);
                }
            }
        }
        return result;
    }

    @Override
    public String writeValue(Object value, Class clazz) {
        String result = null;
        ByteArrayOutputStream os = null;
        try {
            LOGGER.log(Level.FINEST, "Write the value from:{0}, type:{1}", new Object[]{value, clazz});
            MessageBodyWriter mw = RPF.getMessageBodyWriter(clazz, clazz, null, MEDIA_TYPE);
            os = new ByteArrayOutputStream();
            mw.writeTo(value, clazz, clazz, null, MEDIA_TYPE, null, os);
            result = new String(os.toByteArray(), CHARSET_NAME);
            LOGGER.log(Level.FINEST, "Result:{0}", result);
        } catch (IOException | WebApplicationException ex) {
            LOGGER.log(Level.SEVERE, "Error writing the value to string " + value, ex);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "Error closing the output stream", ex);
                }
            }
        }
        return result;
    }

}
