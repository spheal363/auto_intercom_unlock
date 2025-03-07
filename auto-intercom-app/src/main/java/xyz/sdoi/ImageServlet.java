package xyz.sdoi;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {
    private static final String IMAGE_PATH = "/home/sdoi/captured.jpg"; // 画像の保存パス
    private static final int BUFFER_SIZE = 1024; // バッファサイズ

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        File imageFile = new File(IMAGE_PATH);

        if (!imageFile.exists() || !imageFile.isFile()) {
            sendErrorResponse(response, "画像が見つかりません", HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        sendImageResponse(response, imageFile);
    }

    /**
     * 画像ファイルをレスポンスとして送信
     */
    private void sendImageResponse(HttpServletResponse response, File imageFile) throws IOException {
        response.setContentType("image/jpeg");
        response.setContentLength((int) imageFile.length());

        try (FileInputStream fis = new FileInputStream(imageFile);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * エラーメッセージをレスポンスとして送信
     */
    private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("text/plain; charset=UTF-8");
        response.getWriter().write(message);
    }
}
