package xyz.sdoi;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServoMotorControl {
    public static void IntercomPush() {
        try {
            // pigsコマンドを実行して押す
            Process process = Runtime.getRuntime().exec("pigs s 18 1000");
            int exitCode = process.waitFor();
            System.out.println("push");
            Thread.sleep(200);

            // pigsコマンドを実行して戻す
            process = Runtime.getRuntime().exec("pigs s 18 500");
            exitCode = process.waitFor();
            System.out.println("pull");
            Thread.sleep(200);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        IntercomPush();
    }
}