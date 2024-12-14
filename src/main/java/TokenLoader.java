import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TokenLoader {
    private final String filePath;

    public TokenLoader(String filePath) {
        this.filePath = filePath;
    }

    public String loadToken() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        }
        return properties.getProperty("token");
    }
}
