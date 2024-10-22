package com.ironsoftware.ironpdf.internal.staticapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigLoader {
    protected static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
//    protected String appConfigPath = Paths.get("src", "test", "resources", "config.properties").toString();

    protected Properties properties = new Properties();

    public ConfigLoader() {

        Path mainResource = Paths.get("src", "main", "resources", "config.properties");
        if (mainResource.toFile().exists()) {
            try (InputStream input = Files.newInputStream(mainResource)) {
                properties.load(input);
            } catch (IOException ex) {
                logger.debug("cannot get main resources path", ex);
            }
        }

        try (InputStream input = getClass().getResourceAsStream("config.properties")) {
            if (input != null)
                properties.load(input);
        } catch (IOException ex) {
            logger.debug("cannot get class resources path", ex);
        }

        Path testResource = Paths.get("src", "test", "resources", "config.properties");
        if (testResource.toFile().exists()) {
            try (InputStream input = Files.newInputStream(testResource)) {
                if (input != null)
                    properties.load(input);
            } catch (IOException ex) {
                logger.debug("cannot get main resources path", ex);
            }
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }

//    public static ConfigLoader ofMainConfig(){
//        ConfigLoader c = new ConfigLoader();
//
//        Path mainResource = Paths.get("src", "main", "resources", "config.properties");
//        if (mainResource.toFile().exists()) {
//            try (InputStream input = Files.newInputStream(Paths.get(mainResource))) {
//                c.properties.load(input);
//            } catch (IOException ex) {
//                logger.debug("cannot get main resources path", ex);
//            }
//        }
//
//        try (InputStream input = getClass().getResourceAsStream("config.properties")) {
//            c.properties.load(input);
//        } catch (IOException ex) {
//            logger.debug("cannot get class resources path", ex);
//        }
//
//        return  c;
//
//
//
//    }
//
//    public static ConfigLoader ofTestConfig(){
//            properties = new Properties();
//            Path testResource = Paths.get("src", "test", "resources", "config.properties");
//            if (testResource.toFile().exists()) {
//                try (InputStream input = Files.newInputStream(Paths.get(appConfigPath))) {
//                    properties.load(input);
//                } catch (IOException ex) {
//                    logger.debug("cannot get main resources path", ex);
//                }
//            }
//    }

}
