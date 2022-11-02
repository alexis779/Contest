package hackercup.hackercup2022.round2.perfectlybalanced;

import java.util.Arrays;
import java.util.stream.IntStream;

public class PerfectlyBalancedSmallAlphabet implements ROBalanced {
    public static final int MAX = 26;

    /**
     * Array which value set has a small cardinality.
     */
    int[] A;

    /**
     * Prefix count array.
     */
    int[][] counts;

    /**
     * Frequency count of elements in the range [l, r], which middle element is m.
     *
     * - if i <= m, elements are counted as +1
     * - if i > m, elements are counted as -1
     */
    int[] diff = new int[MAX];

    public PerfectlyBalancedSmallAlphabet(int[] A) {
        this.A = A;
        counts = prefixCounts(A);
    }

    private static int[][] prefixCounts(int[] A) {
        int N = A.length;

        int[][] counts = new int[MAX][N];
        for (int c = 0; c < MAX; c++) {
            int countChar = 0;
            for (int i = 0; i < N; i++) {
                if (A[i] == c) {
                    countChar++;
                }
                counts[c][i] = countChar;
            }
        }

        return counts;
    }

    @Override
    public int balanced(Query[] queries) {
        return (int) Arrays.stream(queries)
                .filter(this::balanced)
                .count();
    }

    public boolean balanced(Query query) {
        int l = query.left();
        int r = query.right();

        l--;
        r--;

        int n = r-l+1;
        if (n % 2 == 0) {
            return false;
        }
        // assert n is odd

        int m = (l+r)/2;

        clear();

        addRange(l, m, r);
        return getAnswer(m);
    }

    private void clear() {
        IntStream.range(0, MAX)
                .forEach(i -> diff[i] = 0);
    }

    private void addRange(int l, int m, int r) {
        IntStream.range(0, MAX)
                .forEach(i -> {
                    addUnits(i, countRangeQuery(i, l, m));
                    addUnits(i, -countRangeQuery(i, m+1, r));
                });
    }

    private void addUnits(int i, int units) {
        diff[i] += units;
    }

    private boolean getAnswer(int m) {
        if (unitVector(1)) {
            return true;
        }
        addUnits(A[m], -2);
        if (unitVector(-1)) {
            return true;
        }
        return false;
    }

    private boolean unitVector(int one) {
        int ones = 0;
        int zeros = 0;

        for (int i: diff) {
            if (i == 0) {
                zeros++;
            } else if (i == one) {
                ones++;
            }
        }

        return ones == 1 && zeros == MAX-1;
    }

    private int countRangeQuery(int i, int l, int r) {
        return counts[i][r] - (l == 0 ? 0 : counts[i][l-1]);
    }
}
