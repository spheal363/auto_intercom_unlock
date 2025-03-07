package xyz.sdoi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TokenLoader {
    private static final String TOKEN_FILE_PATH = "src/main/resources/config.properties";
    private static final String TOKEN_KEY = "token";

    /**
     * 設定ファイルからトークンを読み込む
     *
     * @return 読み込んだトークン
     * @throws IOException 読み込み失敗時の例外
     */
    public static String loadToken() throws IOException {
        Properties properties = loadProperties();

        String token = properties.getProperty(TOKEN_KEY);
        if (token == null || token.isEmpty()) {
            throw new IOException("設定ファイルにトークンが定義されていません");
        }

        return token;
    }

    /**
     * 設定ファイルを読み込む
     */
    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();

        try (InputStream input = new FileInputStream(TOKEN_FILE_PATH)) {
            properties.load(input);
        } catch (IOException e) {
            throw new IOException("TokenLoader: 設定ファイル読み込みエラー - " + e.getMessage(), e);
        }

        return properties;
    }
}
