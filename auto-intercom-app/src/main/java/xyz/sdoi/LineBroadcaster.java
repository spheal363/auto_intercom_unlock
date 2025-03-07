package xyz.sdoi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LineBroadcaster {
    private static final String BROADCAST_URL = "https://api.line.me/v2/bot/message/broadcast";
    private static final String AUTH_PREFIX = "Bearer ";
    private static final String CONTENT_TYPE = "application/json";

    /**
     * LINEブロードキャストメッセージを送信
     *
     * @param token       LINEのアクセストークン
     * @param jsonMessage 送信するメッセージ（JSON形式）
     * @throws IOException 送信エラー時の例外
     */
    public void sendBroadcastMessage(String token, String jsonMessage) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = executePostRequest(token, jsonMessage);
            handleResponse(connection);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * HTTP POST リクエストを実行
     */
    private HttpURLConnection executePostRequest(String token, String jsonMessage) throws IOException {
        URL url = new URL(BROADCAST_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", AUTH_PREFIX + token);
        connection.setRequestProperty("Content-Type", CONTENT_TYPE);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonMessage.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        return connection;
    }

    /**
     * HTTPレスポンスを処理
     */
    private void handleResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            printStream(connection.getInputStream(), "LINE送信成功: ");
        } else {
            printStream(connection.getErrorStream(), "LINE送信失敗: ");
            throw new IOException("LINE APIエラー (HTTP " + responseCode + ")");
        }
    }

    /**
     * ストリームを読み取り、出力する
     */
    private void printStream(InputStream stream, String prefix) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            System.out.println(prefix + responseBuilder.toString());
        }
    }
}
