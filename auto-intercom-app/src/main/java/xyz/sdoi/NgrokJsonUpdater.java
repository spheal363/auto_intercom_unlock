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
    private static final String JSON_FILE_PATH = "src/main/resources/IntercomNotify.json"; // JSONファイルのパス
    private static final String IMAGE_PATH = "/image"; // 画像のパス

    public static void updateJson() {
        try {
            String ngrokUrl = getNgrokUrl();
            if (ngrokUrl != null) {
                updateJsonFile(ngrokUrl);
                System.out.println("JSONファイルを更新しました: " + ngrokUrl);
            } else {
                System.out.println("ngrokのURLを取得できませんでした。");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getNgrokUrl() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(NGROK_API_URL).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            InputStream responseStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray tunnels = jsonObject.getJSONArray("tunnels");
            for (int i = 0; i < tunnels.length(); i++) {
                JSONObject tunnel = tunnels.getJSONObject(i);
                if (tunnel.getString("proto").equals("https")) {
                    return tunnel.getString("public_url");
                }
            }
        } catch (Exception e) {
            System.err.println("エラー: ngrokのURLを取得できませんでした。");
            e.printStackTrace();
        }
        return null;
    }

    private static void updateJsonFile(String ngrokUrl) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)), StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(jsonContent);

            JSONArray messages = jsonObject.getJSONArray("messages");
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                if (message.getString("type").equals("image")) {
                    String newUrl = ngrokUrl + IMAGE_PATH;
                    message.put("originalContentUrl", newUrl);
                    message.put("previewImageUrl", newUrl);
                }
            }

            JSONObject templateObject = new JSONObject();
            templateObject.put("type", "confirm");
            templateObject.put("text", "（テスト）誰かがインターホンを押したよ。ドア開ける?");
            JSONArray actions = new JSONArray();
            
            JSONObject action1 = new JSONObject();
            action1.put("type", "message");
            action1.put("label", "あける");
            action1.put("text", "あける");
            actions.put(action1);
            
            JSONObject action2 = new JSONObject();
            action2.put("type", "message");
            action2.put("label", "あけない");
            action2.put("text", "あけない");
            actions.put(action2);
            
            templateObject.put("actions", actions);
            
            JSONObject templateMessage = new JSONObject();
            templateMessage.put("type", "template");
            templateMessage.put("altText", "（テスト）誰かがインターホンを押したみたい");
            templateMessage.put("template", templateObject);
            
            messages.put(1, templateMessage);

            try (FileWriter file = new FileWriter(JSON_FILE_PATH)) {
                file.write(jsonObject.toString(4)); // インデント付きで保存
            }

            System.out.println("JSONファイルを正常に更新しました。");

        } catch (Exception e) {
            System.err.println("JSONファイルの更新に失敗しました。");
            e.printStackTrace();
        }
    }
}
