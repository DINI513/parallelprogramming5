
import java.util.concurrent.*;

public class Main {

    private static int threads = 10;

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(2);
        testSem(semaphore);
    }

    private static void testSem(Semaphore semaphore) {
        ExecutorService executorService = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(String.format("Thread %s waits for semaphore", threadName));
                        semaphore.acquire();
                        System.out.println(String.format("Thread %s acquired semaphore, %d permints available", threadName, semaphore.availablePermits()));
                        Thread.sleep(1000);
                        System.out.println(String.format("Thread %s releases semaphore", threadName));
                        semaphore.release();
                        System.out.println(String.format("Thread %s done", threadName));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
