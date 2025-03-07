package xyz.sdoi;

import com.pi4j.io.i2c.I2C;

public class ColorSensorReader {

    // センサのレジスタ定義（定数として管理）
    private static final int REGISTER_CONTROL = 0x00; // コントロールレジスタ
    private static final int REGISTER_RED     = 0x03; // 赤データ上位バイト
    private static final int REGISTER_GREEN   = 0x05; // 緑データ上位バイト
    private static final int REGISTER_BLUE    = 0x07; // 青データ上位バイト

    // センサの初期設定値
    private static final byte RESET_AND_WAKEUP = (byte) 0x84;
    private static final byte MODE_LOW_GAIN = (byte) 0x09;
    private static final int SENSOR_INIT_DELAY_MS = 10; // 安全マージンを含めた待機時間

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
     * センサの初期化処理
     */
    private void initializeSensor() {
        try {
            resetAndWakeUpSensor();
            setSensorMode();
            waitForSensorInitialization();
        } catch (Exception e) {
            System.err.println("ColorSensorReader: センサ初期化エラー - " + e.getMessage());
        }
    }

    /**
     * センサをリセットしてスリープ解除
     */
    private void resetAndWakeUpSensor() {
        i2c.writeRegister(REGISTER_CONTROL, RESET_AND_WAKEUP);
    }

    /**
     * センサの動作モードを設定
     */
    private void setSensorMode() {
        i2c.writeRegister(REGISTER_CONTROL, MODE_LOW_GAIN);
    }

    /**
     * センサが測定を開始するのを待つ
     */
    private void waitForSensorInitialization() {
        try {
            Thread.sleep(SENSOR_INIT_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("ColorSensorReader: センサ待機中に割り込み発生");
        }
    }

    /**
     * 現在の赤の測定値を取得
     * @return 赤データ(16bit)
     */
    public int readRed() {
        return readData(REGISTER_RED);
    }

    /**
     * 現在の緑の測定値を取得
     * @return 緑データ(16bit)
     */
    public int readGreen() {
        return readData(REGISTER_GREEN);
    }

    /**
     * 現在の青の測定値を取得
     * @return 青データ(16bit)
     */
    public int readBlue() {
        return readData(REGISTER_BLUE);
    }

    /**
     * センサのレジスタからデータを読み取る
     *
     * @param register 上位バイトレジスタアドレス
     * @return 16bitで合成した値
     */
    private int readData(int register) {
        try {
            int highByte = i2c.readRegister(register);
            int lowByte  = i2c.readRegister(register + 1);
            return (highByte << 8) | lowByte;
        } catch (Exception e) {
            System.err.println("ColorSensorReader: I2Cデータ取得エラー - " + e.getMessage());
            return -1; // 異常値を返す
        }
    }
}
