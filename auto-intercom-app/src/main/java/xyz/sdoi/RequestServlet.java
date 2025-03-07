package xyz.sdoi;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/RequestServlet")
public class RequestServlet extends HttpServlet {

    private static final String COMMAND_OPEN = "あける";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");

        String requestBody = parseRequestBody(request);
        if (requestBody.isEmpty()) {
            sendErrorResponse(response, "リクエストボディが空です", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Map<String, Object> jsonMap = objectMapper.readValue(requestBody, Map.class);
            processRequest(jsonMap);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"status\": \"success\"}");
        } catch (JsonMappingException e) {
            sendErrorResponse(response, "JSONフォーマットエラー: " + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            sendErrorResponse(response, "サーバーエラー: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * リクエストボディを読み取る
     */
    private String parseRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        return requestBody.toString();
    }

    /**
     * JSONリクエストを解析し、必要な処理を実行
     */
    private void processRequest(Map<String, Object> jsonMap) {
        if (jsonMap.containsKey("events")) {
            for (Map<String, Object> event : (Iterable<Map<String, Object>>) jsonMap.get("events")) {
                String type = (String) event.get("type");

                if ("message".equals(type)) {
                    Map<String, Object> message = (Map<String, Object>) event.get("message");
                    String text = (String) message.get("text");

                    if (COMMAND_OPEN.equals(text)) {
                        ServoMotorControl.intercomPush();
                        System.out.println("サーボ駆動: 開けるコマンドを受信");
                    }
                }
            }
        }
    }

    /**
     * エラーメッセージをレスポンスとして送信
     */
    private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
