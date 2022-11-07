package com.ironsoftware;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigManager {

    private String appConfigPath = Paths.get("src", "test", "resources", "config.properties").toString();

    private Properties properties;

    public ConfigManager() {
        try (InputStream input = Files.newInputStream(Paths.get(appConfigPath))) {
            properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
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
}
