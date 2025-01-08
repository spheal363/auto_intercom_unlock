package xyz.sdoi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TokenLoader {
    private static final String TOKEN_FILE_PATH = "src/main/resources/config.properties";

    public static String loadToken() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(TOKEN_FILE_PATH)) {
            properties.load(input);
        }
        return properties.getProperty("token");
    }
}