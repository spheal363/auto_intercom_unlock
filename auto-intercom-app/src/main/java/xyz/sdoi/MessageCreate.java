package xyz.sdoi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MessageCreate {
    private static final Map<String, String> MESSAGE_TEMPLATES = new HashMap<>();

    static {
        MESSAGE_TEMPLATES.put("IntercomNotify", "/IntercomNotify.json");
    }

    /**
     * 指定されたメッセージタイプに対応するLINEメッセージを作成
     *
     * @param messageType メッセージタイプ
     * @return メッセージ内容（JSON）
     * @throws IOException ファイル読み込みエラー時の例外
     */
    public static String lineMessageCreate(String messageType) throws IOException {
        String fileName = getResourceFilePath(messageType);
        if (fileName == null) {
            throw new IOException("無効なメッセージタイプ: " + messageType);
        }

        try (InputStream in = MessageCreate.class.getResourceAsStream(fileName)) {
            if (in == null) {
                throw new IOException("リソースが見つかりません: " + fileName);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * メッセージタイプに対応するリソースファイルのパスを取得
     */
    private static String getResourceFilePath(String messageType) {
        return MESSAGE_TEMPLATES.get(messageType);
    }
}
