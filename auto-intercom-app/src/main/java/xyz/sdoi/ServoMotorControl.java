package xyz.sdoi;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServoMotorControl {
    public static void IntercomPush() {
        try {
            // pigsコマンドを実行して押す
            Process process = Runtime.getRuntime().exec("pigs s 18 900");
            int exitCode = process.waitFor();
            System.out.println("通話push");
            Thread.sleep(200);

            // pigsコマンドを実行して戻す
            process = Runtime.getRuntime().exec("pigs s 18 1500");
            exitCode = process.waitFor();
            Thread.sleep(200);

            Thread.sleep(2000);

            // pigsコマンドを実行して押す
            process = Runtime.getRuntime().exec("pigs s 18 2300");
            exitCode = process.waitFor();
            System.out.println("開錠push");
            Thread.sleep(200);

            // pigsコマンドを実行して戻す
            process = Runtime.getRuntime().exec("pigs s 18 1500");
            exitCode = process.waitFor();
            Thread.sleep(200);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        IntercomPush();
    }
}