package xyz.sdoi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;

public class NgrokJsonUpdater {
    private static final String NGROK_API_URL = "http://127.0.0.1:4040/api/tunnels";
    private static final String JSON_FILE_PATH = "src/main/resources/IntercomNotify.json";
    private static final String IMAGE_PATH = "/image";

    /**
     * ngrokのURLを取得し、JSONファイルを更新
     */
    public static void updateJson() {
        try {
            String ngrokUrl = fetchNgrokUrl();
            if (ngrokUrl != null) {
                updateJsonFile(ngrokUrl);
                System.out.println("JSONファイルを更新しました: " + ngrokUrl);
            } else {
                System.err.println("ngrokのURLを取得できませんでした。");
            }
        } catch (Exception e) {
            System.err.println("NgrokJsonUpdater: エラー発生 - " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * ngrok API から公開URLを取得
     */
    private static String fetchNgrokUrl() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(NGROK_API_URL).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("ngrok APIエラー (HTTP " + responseCode + ")");
        }

        try (InputStream responseStream = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return extractNgrokUrl(response.toString());
        }
    }

    /**
     * JSONレスポンスから ngrok の公開URLを抽出
     */
    private static String extractNgrokUrl(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray tunnels = jsonObject.getJSONArray("tunnels");
        for (int i = 0; i < tunnels.length(); i++) {
            JSONObject tunnel = tunnels.getJSONObject(i);
            if ("https".equals(tunnel.getString("proto"))) {
                return tunnel.getString("public_url");
            }
        }
        return null;
    }

    /**
     * JSONファイルを更新
     */
    private static void updateJsonFile(String ngrokUrl) throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(jsonContent);

        JSONArray messages = jsonObject.getJSONArray("messages");
        for (int i = 0; i < messages.length(); i++) {
            JSONObject message = messages.getJSONObject(i);
            if ("image".equals(message.getString("type"))) {
                String newUrl = ngrokUrl + IMAGE_PATH;
                message.put("originalContentUrl", newUrl);
                message.put("previewImageUrl", newUrl);
            }
        }

        try (FileWriter file = new FileWriter(JSON_FILE_PATH)) {
            file.write(jsonObject.toString(4)); // インデント付きで保存
        }

        System.out.println("JSONファイルを正常に更新しました。");
    }
}
