package com.ironsoftware.ironpdf;

import com.ironsoftware.ironpdf.internal.staticapi.ConfigLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

//public class TestConfigLoader extends ConfigLoader {
//
//    public TestConfigLoader() {
//        properties = new Properties();
//        Path testResource = Paths.get("src", "test", "resources", "config.properties");
//        if (testResource.toFile().exists()) {
//            try (InputStream input = Files.newInputStream(Paths.get(appConfigPath))) {
//                properties.load(input);
//            } catch (IOException ex) {
//                logger.debug("cannot get main resources path", ex);
//            }
//        }
//    }
//}
