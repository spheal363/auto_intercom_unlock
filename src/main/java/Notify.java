public class Notify {
    public static void main(String[] args) {
        try {
            // トークン読み込み
            TokenLoader tokenLoader = new TokenLoader("resources/config.properties");
            String token = tokenLoader.loadToken();

            // メッセージ作成
            String jsonMessage = """
                    {
                      "messages": [
                        {
                          "type": "text",
                          "text": "Hello, this is a test message from Java!"
                        }
                      ]
                    }""";

            // 送信処理
            LineBroadcaster broadcaster = new LineBroadcaster();
            broadcaster.sendBroadcastMessage(token, jsonMessage);

            System.out.println("LINEに通知を送信しました。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}