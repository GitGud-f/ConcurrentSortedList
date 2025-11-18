public abstract class TestThread {
    SortList list;

    Integer [] nums;

    protected int successCount = 0;

    public TestThread(RandomSeq seq, int seqPart, SortList setList) {
        this.list = setList;
        this.nums = new Integer[seqPart];
        for (int i=0;i<nums.length;i++){
            nums[i] = seq.next();
        }
    }

    public int getSuccessCount() {
        return successCount;
    }
}
