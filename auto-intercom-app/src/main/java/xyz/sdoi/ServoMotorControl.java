package xyz.sdoi;

import java.io.IOException;

public class ServoMotorControl {
    private static final String SERVO_PUSH = "pigs s 18 900";  // ボタン押し
    private static final String SERVO_RELEASE = "pigs s 18 1500"; // ボタン戻し
    private static final String SERVO_UNLOCK = "pigs s 18 2300"; // 開錠

    private static final int SHORT_DELAY_MS = 200;
    private static final int LONG_DELAY_MS = 2000;

    /**
     * インターホンを押し、開錠操作を実行
     */
    public static void intercomPush() {
        try {
            executePigsCommand(SERVO_PUSH, "通話push");
            Thread.sleep(SHORT_DELAY_MS);

            executePigsCommand(SERVO_RELEASE, "ボタン戻し");
            Thread.sleep(SHORT_DELAY_MS);

            Thread.sleep(LONG_DELAY_MS);

            executePigsCommand(SERVO_UNLOCK, "開錠push");
            Thread.sleep(SHORT_DELAY_MS);

            executePigsCommand(SERVO_RELEASE, "ボタン戻し");
            Thread.sleep(SHORT_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("ServoMotorControl: スレッドが中断されました");
        } catch (IOException e) {
            System.err.println("ServoMotorControl: PIGSコマンド実行エラー - " + e.getMessage());
        }
    }

    /**
     * PIGSコマンドを実行する
     */
    private static void executePigsCommand(String command, String actionName) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command);
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("ServoMotorControl: " + actionName + " 成功");
        } else {
            throw new IOException("PIGSコマンドが異常終了 (exit code: " + exitCode + ")");
        }
    }

    /**
     * テスト用のメインメソッド
     */
    public static void main(String[] args) {
        intercomPush();
    }
}
