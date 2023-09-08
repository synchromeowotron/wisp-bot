package org.mogwai.wisp.security;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TokenSecurity {
    private static final String CONFIG_FILE = "/token.properties";
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream inputStream = TokenSecurity.class.getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new RuntimeException("config.properties file not found in resources.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading config.properties: " + e.getMessage(), e);
        }
    }

    public static String getBotToken() {
        return properties.getProperty("discord.bot.token");
    }
}
