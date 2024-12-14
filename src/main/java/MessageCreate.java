import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MessageCreate {
    public static String LINEMessageCreate(String messageType) throws IOException {
        String filePath = "";
        if ("IntercomNotify".equals(messageType)) {
            filePath = "resources/IntercomNotify.json";
        }
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}