import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MySemaphore extends Semaphore {
    private ReentrantLock lock = new ReentrantLock(true);
    private Condition condition = lock.newCondition();
    private int permits = 0;
    public MySemaphore(int permits) {
        super(permits);
        this.permits = permits;
    }

    @Override
    public void acquire() {
        try {
            lock.lock();
            while (permits <= 0) {
                condition.await();
            }

            permits--;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }
    @Override
    public void release() {
        try {
            lock.lock();
            permits++;
            condition.signal();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public int availablePermits(){
        return permits;
    }
}
