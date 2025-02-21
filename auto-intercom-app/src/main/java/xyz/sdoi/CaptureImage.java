package xyz.sdoi;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CaptureImage {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void Capture() {
        String imagePath = "/home/sdoi/captured.jpg"; // 保存パス
        String command = "libcamera-still -o " + imagePath + " --nopreview"; 

        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("画像を保存しました: " + imagePath);

            // 画像処理: モニタ部分の検出と射影変換
            processImage(imagePath);

        } catch (IOException | InterruptedException e) {
            System.err.println("画像取得エラー: " + e.getMessage());
        }
    }

    public static void processImage(String imagePath) {
        Mat image = Imgcodecs.imread(imagePath); // 画像を読み込む

        if (image.empty()) {
            System.err.println("画像の読み込みに失敗しました: " + imagePath);
            return;
        }

        Mat edges = new Mat();
        Imgproc.GaussianBlur(image, image, new Size(5, 5), 0);
        Imgproc.Canny(image, edges, 100, 200);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f largestRect = null;
        double maxArea = 0;

        for (MatOfPoint contour : contours) {
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
            double perimeter = Imgproc.arcLength(contour2f, true);
            Imgproc.approxPolyDP(contour2f, approxCurve, 0.02 * perimeter, true);

            if (approxCurve.total() == 4) {
                double area = Imgproc.contourArea(approxCurve);
                if (area > maxArea) {
                    maxArea = area;
                    largestRect = approxCurve;
                }
            }
        }

        if (largestRect != null) {
            Point[] rectPoints = largestRect.toArray();
            Point[] targetPoints = {
                new Point(0, 0),
                new Point(400, 0),
                new Point(400, 300),
                new Point(0, 300)
            };
            MatOfPoint2f targetMat = new MatOfPoint2f(targetPoints);

            Mat transformationMatrix = Imgproc.getPerspectiveTransform(largestRect, targetMat);
            Mat correctedImage = new Mat();
            Imgproc.warpPerspective(image, correctedImage, transformationMatrix, new Size(400, 300));

            String processedImagePath = "/home/sdoi/captured.jpg";
            Imgcodecs.imwrite(processedImagePath, correctedImage);
            System.out.println("補正した画像を保存しました: " + processedImagePath);
        } else {
            System.err.println("モニタ部分の検出に失敗しました。");
        }
    }
}
