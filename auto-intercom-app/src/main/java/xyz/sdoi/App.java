package xyz.sdoi;

import java.io.IOException;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import org.opencv.core.Core;

public class App {
    private static final int SENSOR_ADDRESS = 0x2A; // センサのI2Cアドレス

    /**
     * Pi4JのContextをアプリ全体で使い回すため静的保持
     */
    private static Context pi4j;
    private static I2C i2c;
    private static ColorSensorReader colorSensorReader;

    // 通知済みフラグをクラスレベルで保持
    private static boolean notificationSent = false;

    /**
     * (1) アプリの初期化
     *     - Pi4JのContext生成
     *     - I2Cのセットアップ
     *     - カラーセンサリーダーの準備
     */
    public static void init() throws Exception {
        // Pi4Jのコンテキスト生成
        pi4j = Pi4J.newAutoContext();

        // I2C設定
        I2CConfig i2cConfig = I2C.newConfigBuilder(pi4j)
                .id("I2C1")
                .bus(1) // 使用するI2Cバス番号
                .device(SENSOR_ADDRESS)
                .build();

        // I2Cインスタンス生成
        i2c = pi4j.create(i2cConfig);

        // カラーセンサリーダーの生成 (初期化含む)
        colorSensorReader = new ColorSensorReader(i2c);

        // opencv
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        // json書き換え
        NgrokJsonUpdater.updateJson();
        System.out.println("App: init完了");
    }

    /**
     * (2) 常駐ループ処理
     *     - ColorSensorReaderでセンサー値を取得
     *     - 緑判定してLINE通知 + サーボ駆動
     */
    public static void mainLoop() throws InterruptedException {
        // 永遠に動かす場合
        // while(true) { ... } の中でセンサーを読んで通知
        // ただし、ServletContextListenerから呼ぶ場合は「停止方法」に注意してください。
        while (true) {
            int red   = colorSensorReader.readRed();
            int green = colorSensorReader.readGreen();
            int blue  = colorSensorReader.readBlue();

            // 緑の割合を計算
            int total = red + green + blue;
            double greenRatio = total > 0 ? (double) green / total : 0;

            if (total < 80 && greenRatio > 0.5 && !notificationSent) {
                sendLineNotification();

                notificationSent = true;
                System.out.println("緑値が相対的に高いため通知を送信しました: "
                        + "赤 " + red + " 緑 " + green + " 青 " + blue
                        + " 総和 " + total + " 緑割合 " + greenRatio);

                CaptureImage.Capture();

                Thread.sleep(10000); // 10秒スリープ
                notificationSent = false; // 再度通知を許可
            } else {
                System.out.println("赤: " + red + " 緑: " + green + " 青: " + blue
                        + " 総和: " + total + " 緑割合: " + greenRatio);
            }

            Thread.sleep(1000); // 1秒ごとに値を確認
        }
    }

    /**
     * (3) 終了処理
     *     - pi4jシャットダウンなど
     */
    public static void shutdown() {
        if (pi4j != null) {
            pi4j.shutdown();
            System.out.println("App: Pi4J shutdown完了");
        }
    }

    /**
     * ローカル開発/テスト用: 単独実行できるmainメソッド
     */
    public static void main(String[] args) {
        try {
            init();
            mainLoop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    /**
     * LINE通知を送信するメソッド
     */
    private static void sendLineNotification() {
        try {
            // トークン読み込み
            String token = TokenLoader.loadToken();

            // メッセージ作成
            String jsonMessage = MessageCreate.LINEMessageCreate("IntercomNotify");

            // LINE通知送信
            LineBroadcaster broadcaster = new LineBroadcaster();
            broadcaster.sendBroadcastMessage(token, jsonMessage);
        } catch (IOException e) {
            System.err.println("LINE通知送信中にエラーが発生: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("予期せぬエラーが発生: " + e.getMessage());
        }
    }
}