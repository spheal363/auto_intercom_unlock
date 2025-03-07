package xyz.sdoi;

import java.io.IOException;

public class CaptureImage {
    private static final String IMAGE_PATH = "/home/sdoi/captured.jpg"; // 画像の保存パス
    private static final String CAPTURE_COMMAND = "libcamera-still -o " + IMAGE_PATH + " --nopreview --rotation 180";

    /**
     * 画像をキャプチャする
     */
    public static void capture() {
        try {
            executeCaptureCommand();
            System.out.println("画像を保存しました: " + IMAGE_PATH);
        } catch (IOException e) {
            System.err.println("CaptureImage: 画像取得エラー（IO例外）: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("CaptureImage: 画像取得処理が中断されました");
        }
    }

    /**
     * カメラ撮影コマンドを実行
     */
    private static void executeCaptureCommand() throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(CAPTURE_COMMAND);
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IOException("カメラ撮影コマンドが異常終了 (exit code: " + exitCode + ")");
        }
    }
}
