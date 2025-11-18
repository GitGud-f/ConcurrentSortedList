public class ContainThread extends TestThread implements Runnable{
    public ContainThread(RandomSeq seq, int seqPart, SortList setList) {
        super(seq, seqPart, setList);
    }

    @Override
    public void run() {
        for (int i = 0; i < nums.length; i++) {
            if (list.contain(nums[i])) {
                successCount++;
            }
        }
    }
}
