import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Entry {

    public Integer object;

    public Entry next;

    public final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Entry(Integer object) {
        this.object = object;
    }
}
