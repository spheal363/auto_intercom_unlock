package xyz.sdoi;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CaptureImage {
    // OpenCVのネイティブライブラリをロード
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * カメラ撮影→画像保存→画像処理(モニタ部分の射影変換) まで行うメソッド
     */
    public static void Capture() {
        String imagePath = "/home/sdoi/captured.jpg";  // 保存パス
        String command = "libcamera-still -o " + imagePath + " --nopreview"; 

        try {
            // カメラ撮影をコマンド実行
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("画像を保存しました: " + imagePath);

            // 撮影した画像を読み込み・処理
            processImage(imagePath);

        } catch (IOException | InterruptedException e) {
            System.err.println("画像取得エラー: " + e.getMessage());
        }
    }

    /**
     * 画像を読み込み → 手動で指定したモニタの四隅を使って射影変換 → 保存
     */
    public static void processImage(String imagePath) {
        // 画像をOpenCVで読み込む（BGR形式）
        Mat image = Imgcodecs.imread(imagePath);

        if (image.empty()) {
            System.err.println("画像の読み込みに失敗しました: " + imagePath);
            return;
        }

        // 1) 手動でモニタの四隅を指定 (例: 左上, 右上, 左下, 右下)
        MatOfPoint2f ptsSrc = new MatOfPoint2f(
            new Point(524, 200),  // 左上
            new Point(1895, 87),  // 右上
            new Point(732, 1098), // 左下
            new Point(1883, 991)  // 右下
        );

        // 2) 出力先の4点 (例: 480x270 に変換)
        int W = 480, H = 270;
        MatOfPoint2f ptsDst = new MatOfPoint2f(
            new Point(0, 0),    // 変換先の左上
            new Point(W, 0),    // 変換先の右上
            new Point(0, H),    // 変換先の左下
            new Point(W, H)     // 変換先の右下
        );

        // 3) 射影変換行列を求める
        Mat transformationMatrix = Imgproc.getPerspectiveTransform(ptsSrc, ptsDst);

        // 4) warpPerspectiveで変換を適用
        Mat warped = new Mat();
        Imgproc.warpPerspective(image, warped, transformationMatrix, new Size(W, H));

        // 5) 変換後の画像を保存
        String outputPath = "/home/sdoi/captured.jpg";
        Imgcodecs.imwrite(outputPath, warped);
        System.out.println("モニタを射影変換した画像を保存: " + outputPath);
    }
}
