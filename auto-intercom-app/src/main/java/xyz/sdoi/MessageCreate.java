package xyz.sdoi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MessageCreate {
    public static String LINEMessageCreate(String messageType) throws IOException {
        String fileName = "";
        if ("IntercomNotify".equals(messageType)) {
            fileName = "/IntercomNotify.json";
        }
        try (InputStream in = MessageCreate.class.getResourceAsStream(fileName)) {
            if (in == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}