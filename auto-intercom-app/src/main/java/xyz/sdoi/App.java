package xyz.sdoi;

import java.io.IOException;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;

public class App {
    private static final int SENSOR_ADDRESS = 0x2A; // センサのI2Cアドレス

    public static void main(String[] args) throws InterruptedException {
        // Pi4Jのコンテキスト生成
        Context pi4j = Pi4J.newAutoContext();

        // I2C設定
        I2CConfig i2cConfig = I2C.newConfigBuilder(pi4j)
                .id("I2C1")
                .bus(1) // 使用するI2Cバス番号
                .device(SENSOR_ADDRESS)
                .build();

        // I2Cインスタンス生成
        I2C i2c = pi4j.create(i2cConfig);

        // カラーセンサリーダーの生成 (初期化含む)
        ColorSensorReader colorSensorReader = new ColorSensorReader(i2c);

        // 通知済フラグ
        boolean notificationSent = false;

        try {
            while (true) {
                int red   = colorSensorReader.readRed();
                int green = colorSensorReader.readGreen();
                int blue  = colorSensorReader.readBlue();

                // 緑の割合を計算
                int total = red + green + blue;
                double greenRatio = total > 0 ? (double) green / total : 0;

                if (total < 80 && greenRatio > 0.5 && !notificationSent) {
                    sendLineNotification();
                    ServoMotorControl.IntercomPush();

                    notificationSent = true;
                    System.out.println("緑値が相対的に高いため通知を送信しました: "
                            + "赤 " + red + " 緑 " + green + " 青 " + blue
                            + " 総和 " + total + " 緑割合 " + greenRatio);

                    Thread.sleep(10000); // 30秒スリープ
                    notificationSent = false; // 再度通知を許可
                } else {
                    System.out.println("赤: " + red + " 緑: " + green + " 青: " + blue
                            + " 総和: " + total + " 緑割合: " + greenRatio);
                }

                Thread.sleep(1000); // 1秒ごとに値を確認
            }
        } finally {
            pi4j.shutdown();
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
