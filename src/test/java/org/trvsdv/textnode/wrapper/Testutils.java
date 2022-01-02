package org.trvsdv.textnode.wrapper;

import java.io.IOException;
import java.io.InputStream;

class Testutils {

    public static String getResourceAsString(String path) throws IOException {
        InputStream inputStream = Testutils.class.getClassLoader().getResourceAsStream(path);
        StringBuilder builder = new StringBuilder();
        for (int ch; (ch = inputStream.read()) != -1;) {
            builder.append((char) ch);
        }
        return builder.toString();
    }
}
