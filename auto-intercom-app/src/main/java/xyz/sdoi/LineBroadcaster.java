package xyz.sdoi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LineBroadcaster {

    private static final String BROADCAST_URL = "https://api.line.me/v2/bot/message/broadcast";

    public void sendBroadcastMessage(String token, String jsonMessage) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BROADCAST_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonMessage.getBytes("UTF-8"));
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                printResponse(connection.getInputStream());
            } else {
                printErrorResponse(connection.getErrorStream());
            }

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void printResponse(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            String response = responseBuilder.toString();
            System.out.println("Response: " + response);
        }
    }

    private void printErrorResponse(InputStream es) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(es))) {
            StringBuilder errorResponseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                errorResponseBuilder.append(line);
            }
            String errorResponse = errorResponseBuilder.toString();
            System.err.println("Error Response: " + errorResponse);
        }
    }
}
