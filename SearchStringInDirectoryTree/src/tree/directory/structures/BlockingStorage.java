package tree.directory.structures;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Damyan Manev on 17-Jan-16.
 */
public class BlockingStorage<T> {

    public static final int UNLIMITED = -1;

    private final int max_size;

    private int producersCount;
    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();
    private final Queue<T> data;

    public BlockingStorage(int maxSize, int producersCount) {
        this.producersCount = producersCount;
        this.max_size = maxSize;
        this.data = new ArrayDeque<>();
    }

    public BlockingStorage(int producersCount) {
        this(UNLIMITED, producersCount);
    }

    /**
     * This method blocks current thread until a space is available for the new element.
     */
    public void add(T element) throws InterruptedException {
        try {
            lock.lock();
            while (max_size != UNLIMITED && data.size() >= max_size) {
                this.notFull.await();
            }

            this.notEmpty.signal();
            data.add(element);
        } finally {
            lock.unlock();
        }
    }

    /**
     * This method blocks current thread until there is no data.
     */
    public T get() throws InterruptedException {
        try {
            lock.lock();
            while (data.size() == 0 && producersCount != 0) {
                this.notEmpty.await();
            }

            this.notFull.signal();
            return data.poll();
        } finally {
            lock.unlock();
        }
    }

    public void decrementProducers() {
        try {
            lock.lock();
            this.producersCount--;
            if (producersCount == 0) {
                this.notEmpty.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
}

