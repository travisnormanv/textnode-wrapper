package org.trvsdv.textnode.wrapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Properties reader.
 * @author travisdev
 */
class Properties {
    public static String getProperty(String prop) {
        InputStream inputStream = Properties.class.getClassLoader().getResourceAsStream("textwrapper.properties");
        java.util.Properties properties = new java.util.Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(prop);
    }
}
