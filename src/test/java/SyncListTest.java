import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class SyncListTest extends TestCase {

    private final int randomNumsRange = 80_000;

    public void testAddList(){

        SyncList syncList = new SyncList();
//        syncList.remove(Integer.MAX_VALUE);
        syncList.add(1);
        syncList.add(2);
        syncList.add(3);
        syncList.add(Integer.MIN_VALUE);
        syncList.add(3);
        System.out.println(syncList.contain(5));
        System.out.println(syncList.contain(2));
        syncList.remove(3);
    }

    public void testRandSeq() {
        RandomSeq randomSeq = new RandomSeq(0, randomNumsRange);
        for (int i = 0; i < 10; i++) {
            System.out.print(randomSeq.next() + " ");
        }
    }

   public void benchmarkHelp(SortList list, String label) {
        RandomSeq seq = new RandomSeq(0, randomNumsRange);
        int numAddThreads = 7;
       int randLen = 40_501;
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
//        List<Thread> addThreads = new ArrayList<>();
//        List<Thread> containThreads = new ArrayList<>();
//        List<Thread> removeThreads = new ArrayList<>();
//        for (int i = 0; i < 8; i++) {
//            AddThread addThread = new AddThread(seq, randLen / 8, list);
//            ContainThread containThread = new ContainThread(seq, randLen / 8, list);
//            RemoveThread removeThread = new RemoveThread(seq, randLen / 8, list);
//            Thread threadA = new Thread(addThread);
//            addThreads.add(threadA);
//            Thread threadC = new Thread(containThread);
//            containThreads.add(threadC);
//            Thread threadR = new Thread(removeThread);
//            removeThreads.add(threadR);
//        }

        long startA = System.currentTimeMillis();

        addThreads.forEach(Thread::start);
        addThreads.forEach(e -> {
            try {
                e.join();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        long endA = System.currentTimeMillis() - startA;

        System.out.println("ADD "+label+" execution task: "+endA+" ms");
        System.out.println("List length after add " + list.getSize());


       int numContainThreads = 8;
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
       containThreads.forEach(e -> {
           try {
               e.join();
           } catch (InterruptedException ex) {
               throw new RuntimeException(ex);
           }
       });
       long endC = System.currentTimeMillis() - startC;
       System.out.println("Contain" + label + " execution task: " + endC + " ms");
       long totalSuccessFound = containRunnables.stream().mapToInt(ContainThread::getSuccessCount).sum();
       long totalFailuresFound = randLen - totalSuccessFound;
       System.out.println("Total number of successes found: " + totalSuccessFound + ", failures found:" + totalFailuresFound);

       int numRemoveThreads = 8;
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
       removeThreads.forEach(e -> {
           try {
               e.join();
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

    public void testRun(){
        SyncList syncList = new SyncList();
        benchmarkHelp(syncList,"Synchronization");
        System.out.println("==============");
        RWLockList rwLockList = new RWLockList();
        benchmarkHelp(rwLockList, "RWLock");
        System.out.println("==============");
        LockList list = new LockList();
        benchmarkHelp(list,"Lock");
    }
}
