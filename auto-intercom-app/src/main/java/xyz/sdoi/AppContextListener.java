package xyz.sdoi;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.atomic.AtomicBoolean;

@WebListener
public class AppContextListener implements ServletContextListener {

    private Thread backgroundThread;
    private final AtomicBoolean running = new AtomicBoolean(true);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== AppContextListener: init ===");

        backgroundThread = new Thread(() -> {
            try {
                App.init();
                while (running.get()) {
                    App.mainLoop();
                }
            } catch (Exception e) {
                System.err.println("バックグラウンドスレッドエラー: " + e.getMessage());
                e.printStackTrace();
            } finally {
                App.shutdown();
            }
        });

        backgroundThread.setDaemon(true); // JVM終了時にスレッドを自動終了
        backgroundThread.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("=== AppContextListener: destroy ===");
        running.set(false);

        if (backgroundThread != null && backgroundThread.isAlive()) {
            backgroundThread.interrupt();
            try {
                backgroundThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("AppContextListener: スレッド停止中に割り込み発生");
            }
        }
    }
}
