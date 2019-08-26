package com.heylichen.ivy;

import java.io.*;
import java.net.URL;

public class FileUtil {

    public static File toFile(String classpath) {
        URL url = toURL(classpath);
        return new File(url.getPath());
    }

    public static URL toURL(String classpath) {
        return FileUtil.class.getResource(classpath);
    }

    public static String readString(String classpath) throws IOException {
        InputStream in = FileUtil.class.getResourceAsStream(classpath);
        return readString(in);
    }

    public static String readString(InputStream in) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        }
    }
}
