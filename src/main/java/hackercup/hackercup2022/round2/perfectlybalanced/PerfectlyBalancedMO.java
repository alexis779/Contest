package hackercup.hackercup2022.round2.perfectlybalanced;

import java.util.Arrays;
import java.util.Comparator;

/**
 * MO's algorithm: offline algorithm to efficiently run Q queries over a data set of size N.
 * It reduces complexity from a naive O(Q * N) down to O((Q + N)*N^{1/2}) by rearranging queries beforehand.
 *
 * See <a href="https://cp-algorithms.com/data_structures/sqrt_decomposition.html#mos-algorithm">...</a>
 */
public class PerfectlyBalancedMO extends AbstractPerfectlyBalanced implements ROBalanced {

    private static final int BLOCK_SIZE = 1000;
    private static final Comparator<Query> COMPARATOR = Comparator.<Query>comparingInt(query -> query.left() / BLOCK_SIZE)
            .thenComparingInt(Query::right);

    /**
     * Comparator used to preprocess the list of queries by sorting them.
     */
    private final Comparator<Query> comparator;

    public PerfectlyBalancedMO(int[] A) {
        this(A, COMPARATOR);
    }

    public PerfectlyBalancedMO(int[] A, Comparator<Query> comparator) {
        super(A);
        this.comparator = comparator;
    }

    @Override
    public int balanced(Query[] queries) {
        queries = Arrays.stream(queries)
                .filter(q -> (q.right() - q.left() + 1) % 2 == 1)
                .sorted(comparator)
                .toArray(Query[]::new);

        int balanced = 0;

        int cur_l = 0;
        int cur_r = -1;
        int cur_m = 0;
        for (Query q: queries) {
            int l = q.left();
            int r = q.right();

            l--;
            r--;

            while (cur_l > l) {
                cur_l--;
                add(cur_m, cur_l);
                if ((cur_l + cur_r) / 2 == cur_m-1) {
                    addUnits(cur_m, -2);
                    cur_m--;
                }
            }
            while (cur_r < r) {
                cur_r++;
                add(cur_m, cur_r);
                if ((cur_l + cur_r) / 2 == cur_m+1) {
                    cur_m++;
                    addUnits(cur_m, +2);
                }
            }
            while (cur_l < l) {
                remove(cur_m, cur_l);
                cur_l++;
                if ((cur_l + cur_r) / 2 == cur_m+1) {
                    cur_m++;
                    addUnits(cur_m, +2);
                }
            }
            while (cur_r > r) {
                remove(cur_m, cur_r);
                cur_r--;
                if ((cur_l + cur_r) / 2 == cur_m-1) {
                    addUnits(cur_m, -2);
                    cur_m--;
                }
            }

            if (getAnswer(cur_m)) {
                balanced++;
            }
        }

        return balanced;
    }
}
