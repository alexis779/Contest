package hackercup.hackercup2022.round2.perfectlybalanced;

import java.util.stream.IntStream;

public class PerfectlyBalancedNaive extends AbstractPerfectlyBalanced implements RWBalanced {

    public PerfectlyBalancedNaive(int[] A) {
        super(A);
    }

    @Override
    public boolean balanced(Query query) {
        int l = query.left();
        int r = query.right();

        l--;
        r--;
        int n = r - l + 1;
        if (n % 2 == 0) {
            return false;
        }

        int m = (l+r)/2;

        diff.clear();
        IntStream.range(l, r+1)
                .forEach(i -> add(m, i));

        return getAnswer(m);
    }

    @Override
    public void update(int i, int y) {
        A[i - 1] = y;
    }
}
