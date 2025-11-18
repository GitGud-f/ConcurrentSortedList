import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWLockList extends SortList {
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public RWLockList() {
        super();
    }

    @Override
    public boolean add(Integer obj) {
        try {
            lock.writeLock().lock();

            Entry prev = this.head;
            Entry curr = prev.next;
            while (curr.object.compareTo(obj) < 0) {
                prev = curr;
                curr = prev.next;
            }
            if (curr.object.equals(obj) || prev.object.equals(obj)) {
                return false;
            } else {
                Entry newEntry = new Entry(obj);
                newEntry.next = curr;
                prev.next = newEntry;
                return true;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Integer obj) {
        try {
            lock.writeLock().lock();
            Entry prev = this.head;
            Entry curr = prev.next;
            while (curr.object.compareTo(obj) < 0) {
                prev = curr;
                curr = prev.next;
            }
            if (curr.object.equals(obj)) {
                prev.next = curr.next;
                return true;
            } else {
                return false;
            }
        } finally {
            lock.writeLock().unlock();
        }

    }

    @Override
    public boolean contain(Integer obj) {
        try {
            lock.readLock().lock();
            Entry prev = this.head;
            Entry curr = prev.next;
            while (curr.object.compareTo(obj) < 0) {
                prev = curr;
                curr = prev.next;
            }
            if (curr.object.equals(obj) || prev.object.equals(obj)) {
                return true;
            } else {
                return false;
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int getSize() {
        try {
            lock.readLock().lock();
            int count = 0;
            Entry curr = head.next;  // Skip MIN_VALUE sentinel
            while (curr != null && !curr.object.equals(Integer.MAX_VALUE)) {
                count++;
                curr = curr.next;
            }
            return count;
        } finally {
            lock.readLock().unlock();
        }
    }

}
