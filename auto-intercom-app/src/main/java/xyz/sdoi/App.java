package xyz.sdoi;

import java.io.IOException;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App {
    private static final int SENSOR_ADDRESS = 0x2A; // センサのI2Cアドレス

    // Pi4Jのコンテキスト（アプリ全体で共有）
    private static Context pi4j;
    private static I2C i2c;
    private static ColorSensorReader colorSensorReader;
    
    // 通知済みフラグ
    private static boolean notificationSent = false;

    /**
     * アプリの初期化
     * - Pi4JのContext生成
     * - I2Cのセットアップ
     * - カラーセンサリーダーの準備
     */
    public static void init() throws Exception {
        pi4j = Pi4J.newAutoContext();

        I2CConfig i2cConfig = I2C.newConfigBuilder(pi4j)
                .id("I2C1")
                .bus(1)
                .device(SENSOR_ADDRESS)
                .build();

        i2c = pi4j.create(i2cConfig);
        colorSensorReader = new ColorSensorReader(i2c);

        // ngrokのJSON更新
        NgrokJsonUpdater.updateJson();

        System.out.println("App: 初期化完了");
    }

    /**
     * センサーデータを取得し、通知判定を行う
     */
    private static void analyzeSensorData() throws InterruptedException {
        int red = colorSensorReader.readRed();
        int green = colorSensorReader.readGreen();
        int blue = colorSensorReader.readBlue();

        int total = red + green + blue;
        double greenRatio = total > 0 ? (double) green / total : 0;

        if (green > 15 && !notificationSent) {
            sendLineNotification();
            notificationSent = true;
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M月 dd HH:mm:ss");
            String timestamp = LocalDateTime.now().format(formatter);

            System.out.printf("%s 通知送信: 赤 %d 緑 %d 青 %d 総和 %d 緑割合 %.2f%n",
                                timestamp, red, green, blue, total, greenRatio);

            CaptureImage.capture();
            Thread.sleep(10000); // 10秒待機
            notificationSent = false; // 再度通知を許可
        } else {
            // System.out.printf("データログ: 赤 %d 緑 %d 青 %d 総和 %d 緑割合 %.2f%n",
            //                   red, green, blue, total, greenRatio);
        }
    }

    /**
     * センサーを監視し続けるループ処理
     */
    public static void mainLoop() {
        try {
            while (true) {
                analyzeSensorData();
                Thread.sleep(100); // 0.1秒ごとにセンサー値を確認
            }
        } catch (InterruptedException e) {
            System.out.println("App: ループ処理が中断されました");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("App: 致命的エラーが発生: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * アプリ終了処理
     */
    public static void shutdown() {
        if (pi4j != null) {
            pi4j.shutdown();
            System.out.println("App: Pi4J shutdown完了");
        }
    }

    /**
     * アプリケーションのエントリーポイント
     */
    public static void main(String[] args) {
        try {
            init();
            mainLoop();
        } catch (Exception e) {
            System.err.println("App: 実行時エラーが発生: " + e.getMessage());
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
            String token = TokenLoader.loadToken();
            String jsonMessage = MessageCreate.lineMessageCreate("IntercomNotify");

            new LineBroadcaster().sendBroadcastMessage(token, jsonMessage);
        } catch (IOException e) {
            System.err.println("LINE通知送信エラー: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("予期せぬエラー: " + e.getMessage());
        }
    }
}
