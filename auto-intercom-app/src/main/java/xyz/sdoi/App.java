package xyz.sdoi;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            // トークン読み込み
            String token = TokenLoader.loadToken();

            // インターホンが押されたときのメッセージ作成
            String jsonMessage = MessageCreate.LINEMessageCreate("IntercomNotify");

            // ブロードキャストで一斉送信
            LineBroadcaster broadcaster = new LineBroadcaster();
            broadcaster.sendBroadcastMessage(token, jsonMessage);

            System.out.println("LINEに通知を送信しました。");

        } catch (IOException e) {
            System.err.println("ファイルの読み込み中にエラーが発生: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("予期せぬエラーが発生: " + e.getMessage());
        }
    }
}