package xyz.sdoi;

import com.pi4j.io.i2c.I2C;

public class ColorSensorReader {

    // センサのレジスタ定義
    private static final int REGISTER_CONTROL = 0x00; // コントロールレジスタ
    private static final int REGISTER_RED     = 0x03; // 赤データ上位バイト
    private static final int REGISTER_GREEN   = 0x05; // 緑データ上位バイト
    private static final int REGISTER_BLUE    = 0x07; // 青データ上位バイト

    // I2Cインスタンス
    private final I2C i2c;

    /**
     * コンストラクタでI2Cインスタンスを受け取り、初期化を行う
     *
     * @param i2c 既に生成されたPi4JのI2Cインスタンス
     */
    public ColorSensorReader(I2C i2c) {
        this.i2c = i2c;
        initializeSensor();
    }

    /**
     * センサの初期化
     * - ADCリセットとスリープ解除
     * - 動作モード設定
     * - 測定開始待機
     */
    private void initializeSensor() {
        // 1. ADCリセットとスリープ解除 (0x84)
        i2c.writeRegister(REGISTER_CONTROL, (byte) 0x84);

        // 2. 動作モード設定 (Lowゲイン, 固定時間モード, Tint=1.4ms)
        i2c.writeRegister(REGISTER_CONTROL, (byte) 0x09);

        // 3. センサが測定を開始するのを待つ (>5.6ms)
        try {
            Thread.sleep(10); // 10ms待機（安全マージンとして少し長め）
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 現在の赤の測定値を取得
     *
     * @return 赤データ(16bit)
     */
    public int readRed() {
        return readData(REGISTER_RED);
    }

    /**
     * 現在の緑の測定値を取得
     *
     * @return 緑データ(16bit)
     */
    public int readGreen() {
        return readData(REGISTER_GREEN);
    }

    /**
     * 現在の青の測定値を取得
     *
     * @return 青データ(16bit)
     */
    public int readBlue() {
        return readData(REGISTER_BLUE);
    }

    /**
     * 実際にセンサのレジスタからデータを読む共通処理
     *
     * @param register 上位バイトレジスタアドレス
     * @return 16bitで合成した値
     */
    private int readData(int register) {
        int highByte = i2c.readRegister(register);
        int lowByte  = i2c.readRegister(register + 1);
        return (highByte << 8) | lowByte;
    }
}