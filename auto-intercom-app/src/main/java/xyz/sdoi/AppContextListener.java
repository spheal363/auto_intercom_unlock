package xyz.sdoi;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    private Thread backgroundThread;
    private volatile boolean running = true;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== AppContextListener: init ===");

        backgroundThread = new Thread(() -> {
            try {
                App.init();
                // mainLoopの中で無限ループするなら、ここで呼び出すだけ
                // 途中で止める必要があるなら、interrupt()などの仕組みを検討
                App.mainLoop();
            } catch (InterruptedException e) {
                System.out.println("バックグラウンドスレッドがInterrupted");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                App.shutdown();
            }
        });

        backgroundThread.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("=== AppContextListener: destroy ===");
        running = false;
        if (backgroundThread != null && backgroundThread.isAlive()) {
            backgroundThread.interrupt();
            try {
                backgroundThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}