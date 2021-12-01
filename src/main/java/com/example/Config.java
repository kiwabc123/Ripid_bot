package com.example;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * File configuration support
 */
public class Config {

    private InputStream inputStream;

    /**
     * Get value from config
     * @param name
     * @return
     * @throws IOException
     */
    public String getPropValues(String name) throws IOException {

        try {
            Properties properties = new Properties();
            String fileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file " + fileName + " not found in the classpath");
            }
            return properties.getProperty(name);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        finally {
            inputStream.close();
        }
        return null;
    }
}
