import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import com.pi4j.Pi4J;

public class ColorReader {
    public static void main(String[] args) {
        var pi4j = Pi4J.newAutoContext();

        I2CProvider i2cProvider = pi4j.provider("linuxfs-i2c");
        I2CConfig config = I2C.newConfigBuilder(pi4j)
                .id("s11059")
                .bus(1)
                .device(0x2A) // デバイスアドレス
                .build();

        I2C device = i2cProvider.create(config);

        try {
            // センサーの初期化とリセット
            initializeSensor(device);

            // RGBデータの読み取り
            int redValue = readColorValue(device, 0x03);
            int greenValue = readColorValue(device, 0x05);
            int blueValue = readColorValue(device, 0x07);

            System.out.println("Red: " + redValue);
            System.out.println("Green: " + greenValue);
            System.out.println("Blue: " + blueValue);

            // 色の判定
            String color = determineColor(redValue, greenValue, blueValue);
            System.out.println("判定結果: " + color);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pi4j.shutdown();
        }
    }

    // センサーを初期化するメソッド
    private static void initializeSensor(I2C device) throws Exception {
        device.writeRegister(0x00, (byte) 0x84); // 動作モード、ADCリセット
        Thread.sleep(10); // リセットのための短い待機時間
        device.writeRegister(0x00, (byte) 0x04); // 動作開始
        Thread.sleep(2184); // 積分時間の待機
    }

    // RGB値を読み取るためのヘルパーメソッド
    private static int readColorValue(I2C device, int register) throws Exception {
        byte[] buffer = new byte[2];
        device.readRegister(register, buffer);
        return ((buffer[0] & 0xFF) << 8) | (buffer[1] & 0xFF);
    }

    // 色を判定するためのロジック
    private static String determineColor(int red, int green, int blue) {
        int threshold = 100;

        if (red < threshold && green < threshold && blue < threshold) {
            return "黒";
        } else if (red >= green && red >= blue) {
            return "赤";
        } else if (green >= red && green >= blue) {
            return "緑";
        } else {
            return "青";
        }
    }
}
