package top.luoqiz.file.thread;

import java.util.concurrent.*;

public class FileThreadPool {
    private static ExecutorService executorService = new ThreadPoolExecutor(
            2,
            Runtime.getRuntime().availableProcessors(),
            3L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    public static ExecutorService getExecutorService() {
        return executorService;
    }
}
