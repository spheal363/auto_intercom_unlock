import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class Notify {
    public static void main(String[] args) {
        try {
            // トークンを外部ファイルから読み込む
            String token = loadToken("config.properties");

            // LINEに通知するメッセージ
            String jsonMessage = """
                    {
                      "messages": [
                        {
                          "type": "text",
                          "text": "Hello, this is a test message from Java!"
                        }
                      ]
                    }""";

            sendLineBroadcastMessage(token, jsonMessage);
            System.out.println("LINEに通知を送信しました。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // トークンを読み込むメソッド
    private static String loadToken(String filePath) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        }
        return properties.getProperty("token");
    }

    public static void sendLineBroadcastMessage(String token, String jsonMessage) throws IOException {
        URL url = new URL("https://api.line.me/v2/bot/message/broadcast");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonMessage.getBytes("UTF-8"));
            os.flush();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream is = connection.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    String response = responseBuilder.toString();
                    System.out.println("Response: " + response);
                }
            } else {
                try (InputStream es = connection.getErrorStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(es))) {
                    StringBuilder errorResponseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        errorResponseBuilder.append(line);
                    }
                    String errorResponse = errorResponseBuilder.toString();
                    System.err.println("Error Response: " + errorResponse);
                }
            }
        } finally {
            connection.disconnect();
        }
    }
}
