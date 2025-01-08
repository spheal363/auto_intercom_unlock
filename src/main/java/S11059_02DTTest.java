import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import com.pi4j.Pi4J;

public class S11059_02DTTest {

    // I2Cアドレスやレジスタの定義
    private static final int DEVICE_ADDRESS = 0x2A;
    private static final int CONTROL_REG = 0x00;

    // センサーから取得した生の値(16bit, 0〜65535)
    private static int red = 0;
    private static int green = 0;
    private static int blue = 0;
    private static int IR = 0;

    // 補正用(色判別用)
    private static int r = 0;
    private static int g = 0;
    private static int b = 0;

    // 判別閾値(元コードの値をそのまま移行)
    private static final int r_1 = 4;
    private static final int r_2 = 8;
    private static final int r_3 = 20;
    private static final int g_1 = 4;
    private static final int g_2 = 8;
    private static final int g_3 = 20;
    private static final int b_1 = 3;
    private static final int b_2 = 6;
    private static final int b_3 = 9;
    private static final int b_4 = 20;

    private static int color = 0;

    // 色グループ(3次元配列)
    // colorGroup[r][g][b] -> 36種類(0〜35)
    private static final int[][][] colorGroup = {
        {
            {0, 1, 2, 3},
            {4, 5, 6, 7},
            {8, 9, 10, 11}
        },
        {
            {12, 13, 14, 15},
            {16, 17, 18, 19},
            {20, 21, 22, 23}
        },
        {
            {24, 25, 26, 27},
            {28, 29, 30, 31},
            {32, 33, 34, 35}
        }
    };

    /**
     * コンストラクタや初期化処理のイメージ
     * 実際には使用するI2Cライブラリの初期化コードなどを入れることが多い
     */
    public S11059_02DTTest() {
        // I2C初期化処理など(ライブラリに応じて書き換え)
        // 例: i2c = I2CFactory.getInstance(...);
        //     i2cDevice = i2c.getDevice(DEVICE_ADDRESS);
        System.out.println("初期化完了(仮)");
    }

    /**
     * メイン処理相当
     * Arduinoの loop() のように定期的に呼び出すイメージ
     */
    public void run() {
        while (true) {
            getRGB();  // センサーからRGB+IRを取得
            System.out.print("Raw(R,G,B,IR) = ");
            System.out.printf("%d, %d, %d, %d%n", red, green, blue, IR);

            // IRで割って正規化(元コードと同じ計算)
            // ただしIRが0になるケースへの例外対策は本来必要
            red   = (IR != 0) ? red   / IR : 0;
            green = (IR != 0) ? green / IR : 0;
            blue  = (IR != 0) ? blue  / IR : 0;

            getColor();  // RGB値に基づいて色IDを判定

            System.out.print("Normalized(R,G,B) = ");
            System.out.printf("%d, %d, %d, ColorID = %d%n", red, green, blue, color);

            try {
                // 適当に500ms待つ
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break; // ループを抜ける
            }
        }
    }

    /**
     * センサーからRGB+IRを取得する処理
     * Arduinoの Wire ライブラリを利用した部分をJava向けに概念的に書き換え
     */
    private void getRGB() {
        // I2C書き込み＆読み込みの流れは以下を参考に
        //   1) 制御レジスタに制御コマンドを書き込み
        //   2) 適切な時間だけ待機
        //   3) 必要なデータレジスタからRGB+IRをまとめて読み込み

        // ========== 書き込み例 ==========
        // i2cDevice.write(CONTROL_REG, (byte)0x83);  // ADC reset, wakeup, low gain...
        // i2cDevice.write(CONTROL_REG, (byte)0x03);  // ADC start, ...

        // ========== 実際の待機時間 ==========
        // 元コードでは delay(180*4) -> 約720ms
        // Thread.sleep(720) 等で対応

        // ========== 読み込み例 ==========
        // byte[] buffer = new byte[8];
        // i2cDevice.read(0x03, buffer, 0, 8);
        // その後バッファを2バイト単位で組み立てて16bit値を生成

        // ここではダミー値をセット(実装サンプル)
        try {
            // (1) 制御コマンド書き込み(ダミー)
            // i2cDevice.write(CONTROL_REG, (byte)0x83);
            // i2cDevice.write(CONTROL_REG, (byte)0x03);

            // (2) ADCの計測時間待ち
            Thread.sleep(720);

            // (3) デバイスから8バイト読み込む(ダミー値を仮定)
            // 実際にはi2cDevice.read(...)で読み出す
            // ここでは仮にセンサーが計測したっぽい値を適当にセット
            red   = 2000;
            green = 3000;
            blue  = 1500;
            IR    = 1000;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 取得したRGB値を判別用の区分(r,g,b)に変換し、colorIDを取得
     */
    private void getColor() {
        // red判定
        if (red <= r_1) {
            r = 0;
        } else if (red <= r_2) {
            r = 1;
        } else if (red <= r_3) {
            r = 2;
        } else {
            // このあたり、元コードだと r_3より大きい時が抜けているが
            // おそらく想定外 or それ以上は同一区分(2)扱い...?
            r = 2;
        }

        // green判定
        if (green <= g_1) {
            g = 0;
        } else if (green <= g_2) {
            g = 1;
        } else if (green <= g_3) {
            g = 2;
        } else {
            g = 2;
        }

        // blue判定
        if (blue <= b_1) {
            b = 0;
        } else if (blue <= b_2) {
            b = 1;
        } else if (blue <= b_3) {
            b = 2;
        } else if (blue <= b_4) {
            b = 3;
        } else {
            b = 3;
        }

        // 色グループ配列を使って色IDに変換
        color = colorGroup[r][g][b];
    }

    /**
     * 実行入口 (サンプル)
     */
    public static void main(String[] args) {
        S11059_02DTTest sensorTest = new S11059_02DTTest();
        sensorTest.run();
    }
}
