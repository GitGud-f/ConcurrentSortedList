public class AddThread extends TestThread implements Runnable {
    public AddThread(RandomSeq seq, int seqPart, SortList setList) {
        super(seq, seqPart, setList);
    }

    @Override
    public void run() {
        for (int i = 0; i < nums.length; i++) {
            if (list.add(nums[i])) {
                successCount++;
            }
        }
    }
}
