package xyz.sdoi;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/RequestServlet")
public class RequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StringBuilder requestBody = new StringBuilder();
        String line;

        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        System.out.println("Received request: " + requestBody.toString());

        try {
            // JSONパース (Jackson を使用)
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(requestBody.toString(), Map.class);

            if (jsonMap.containsKey("events")) {
                for (Map<String, Object> event : (Iterable<Map<String, Object>>) jsonMap.get("events")) {
                    String type = (String) event.get("type");

                    if ("message".equals(type)) {
                        Map<String, Object> message = (Map<String, Object>) event.get("message");
                        String text = (String) message.get("text");

                        // "あける" の場合はサーボを動作
                        if ("あける".equals(text)) {
                            ServoMotorControl.IntercomPush();
                            System.out.println("サーボ駆動: 開けるコマンドを受信");
                        }
                    }
                }
            }

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.err.println("エラー: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
