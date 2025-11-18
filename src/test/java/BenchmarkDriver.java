import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkDriver extends TestCase {

    // Configurable parameters
    private static final int[] RAND_LENS = {10_000, 20_000, 50_000};
    private static final int[] ADD_THREAD_COUNTS = {1, 4, 7, 16};
    private static final int[] CONTAIN_REMOVE_THREAD_COUNTS = {1, 4, 8, 16};
    private static final int RANDOM_NUMS_RANGE = 80_000;
    private static final int SET_SEED = 0;  // Fixed seed for reproducibility

    public void benchmark(SortList list, String label, int randLen, int numAddThreads, int numContainThreads, int numRemoveThreads) {
        RandomSeq seq = new RandomSeq(SET_SEED, RANDOM_NUMS_RANGE);  // New seq per benchmark for independence

        // Print config header
        System.out.println("=== Benchmark Config: Size=" + randLen + ", Add Threads=" + numAddThreads +
                ", Contain Threads=" + numContainThreads + ", Remove Threads=" + numRemoveThreads + " ===");

        // ADD phase
        int addBase = randLen / numAddThreads;
        int addRem = randLen % numAddThreads;
        List<AddThread> addRunnables = new ArrayList<>();
        List<Thread> addThreads = new ArrayList<>();
        for (int i = 0; i < numAddThreads; i++) {
            int part = addBase + (i < addRem ? 1 : 0);
            AddThread addThread = new AddThread(seq, part, list);
            addRunnables.add(addThread);
            addThreads.add(new Thread(addThread));
        }

        long startA = System.currentTimeMillis();
        addThreads.forEach(Thread::start);
        addThreads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        long endA = System.currentTimeMillis() - startA;
        System.out.println("ADD" + label + " execution task: " + endA + " ms");
        System.out.println("List length after add " + list.getSize());

        // CONTAIN phase
        int containBase = randLen / numContainThreads;
        int containRem = randLen % numContainThreads;
        List<ContainThread> containRunnables = new ArrayList<>();
        List<Thread> containThreads = new ArrayList<>();
        for (int i = 0; i < numContainThreads; i++) {
            int part = containBase + (i < containRem ? 1 : 0);
            ContainThread containThread = new ContainThread(seq, part, list);
            containRunnables.add(containThread);
            containThreads.add(new Thread(containThread));
        }

        long startC = System.currentTimeMillis();
        containThreads.forEach(Thread::start);
        containThreads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        long endC = System.currentTimeMillis() - startC;
        System.out.println("Contain" + label + " execution task: " + endC + " ms");
        long totalSuccessFound = containRunnables.stream().mapToInt(ContainThread::getSuccessCount).sum();
        long totalFailuresFound = randLen - totalSuccessFound;
        System.out.println("Total number of successes found: " + totalSuccessFound + ", failures found:" + totalFailuresFound);

        // REMOVE phase
        int removeBase = randLen / numRemoveThreads;
        int removeRem = randLen % numRemoveThreads;
        List<RemoveThread> removeRunnables = new ArrayList<>();
        List<Thread> removeThreads = new ArrayList<>();
        for (int i = 0; i < numRemoveThreads; i++) {
            int part = removeBase + (i < removeRem ? 1 : 0);
            RemoveThread removeThread = new RemoveThread(seq, part, list);
            removeRunnables.add(removeThread);
            removeThreads.add(new Thread(removeThread));
        }

        long startR = System.currentTimeMillis();
        removeThreads.forEach(Thread::start);
        removeThreads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        long endR = System.currentTimeMillis() - startR;
        System.out.println("Remove" + label + " execution task: " + endR + " ms");
        System.out.println("List length after remove " + list.getSize());
        long totalSuccessRemoved = removeRunnables.stream().mapToInt(RemoveThread::getSuccessCount).sum();
        long totalFailuresRemoved = randLen - totalSuccessRemoved;
        System.out.println("Total number of successes removed: " + totalSuccessRemoved + ", failures removed: " + totalFailuresRemoved);
    }

    public void runBenchmarks() {
        for (int randLen : RAND_LENS) {
            for (int addThreads : ADD_THREAD_COUNTS) {
                for (int crThreads : CONTAIN_REMOVE_THREAD_COUNTS) {  // Same for contain and remove
                    // Run for SyncList
                    SyncList syncList = new SyncList();
                    benchmark(syncList, " Synchronization", randLen, addThreads, crThreads, crThreads);
                    System.out.println("==============");

                    // Run for RWLockList (assuming fine-grained version; swap code if coarse needed)
                    RWLockList rwLockList = new RWLockList();
                    benchmark(rwLockList, " RWLock", randLen, addThreads, crThreads, crThreads);
                    System.out.println("==============");

                    // Run for LockList
                    LockList lockList = new LockList();
                    benchmark(lockList, " Lock", randLen, addThreads, crThreads, crThreads);
                    System.out.println("==============");
                }
            }
        }
    }

    // Entry point: Run via IDE or command line
    public static void main(String[] args) {
        new BenchmarkDriver().runBenchmarks();
    }
}