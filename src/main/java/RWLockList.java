import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWLockList extends SortList {

    public RWLockList() {
        super();
    }

    @Override
    public boolean add(Integer obj) {
        Entry prev = head;
        ///
        prev.lock.writeLock().lock();
        ///
        try {
            Entry curr = prev.next;
            ///
            curr.lock.writeLock().lock();
            ///
            try {
                while (curr.object.compareTo(obj) < 0) {
                    prev.lock.writeLock().unlock();
                    prev = curr;
                    curr = curr.next;
                    ///
                    curr.lock.writeLock().lock();
                    ///
                }
                if (curr.object.equals(obj)) {
                    return false;
                } else {
                    Entry newEntry = new Entry(obj);
                    newEntry.next = curr;
                    prev.next = newEntry;
                    return true;
                }
            } finally {
                ///
                curr.lock.writeLock().unlock();
                ///
            }
        } finally {
            ///
            prev.lock.writeLock().unlock();
            ///
        }
    }

    @Override
    public boolean remove(Integer obj) {
        Entry prev = head;
        prev.lock.writeLock().lock();
        try {
            Entry curr = prev.next;
            curr.lock.writeLock().lock();
            try {
                while (curr.object.compareTo(obj) < 0) {
                    prev.lock.writeLock().unlock();
                    prev = curr;
                    curr = curr.next;
                    curr.lock.writeLock().lock();
                }
                if (curr.object.equals(obj)) {
                    prev.next = curr.next;
                    return true;
                } else {
                    return false;
                }
            } finally {
                curr.lock.writeLock().unlock();
            }
        } finally {
            prev.lock.writeLock().unlock();
        }
    }


    @Override
    public boolean contain(Integer obj) {
        Entry prev = head;
        prev.lock.readLock().lock();
        try {
            Entry curr = prev.next;
            curr.lock.readLock().lock();
            try {
                while (curr.object.compareTo(obj) < 0) {
                    prev.lock.readLock().unlock();
                    prev = curr;
                    curr = curr.next;
                    curr.lock.readLock().lock();
                }
                return curr.object.equals(obj);
            } finally {
                curr.lock.readLock().unlock();
            }
        } finally {
            prev.lock.readLock().unlock();
        }
    }

    @Override
    public int getSize() {
        int count = 0;
        Entry prev = head;
        prev.lock.readLock().lock();
        try {
            Entry curr = prev.next;
            curr.lock.readLock().lock();
            try {
                while (!curr.object.equals(Integer.MAX_VALUE)) {
                    count++;
                    prev.lock.readLock().unlock();
                    prev = curr;
                    curr = curr.next;
                    curr.lock.readLock().lock();
                }
                return count;
            } finally {
                curr.lock.readLock().unlock();
            }
        } finally {
            prev.lock.readLock().unlock();
        }
    }

}
