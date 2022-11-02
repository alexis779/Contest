package hackercup.hackercup2022.round2.perfectlybalanced;

import org.tech.vineyard.arithmetics.ModularArithmetics;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractModularSumHash implements RWBalanced {
    /**
     * Large prime number.
     */
    private static final int MOD = 1_000_000_009;

    /**
     * Add / substract integers modulo the large prime number.
     */
    private static final ModularArithmetics MODULAR_ARITHMETICS = new ModularArithmetics(MOD);

    /**
     * Neutral element for the sum operation:
     *      a + 0 = 0 + a = a
     */
    protected static final HashElement ZERO = new HashElement(0, 0);

    /**
     * Random Number Generator.
     */
    private static final Random RANDOM = new Random();

    /**
     * The sum over hashes will be less likely to have collisions than the sum over integers.
     *
     * Our hash function is implemented via an array of random elements h assigned to each possible values in A.
     *
     * But the alphabet size of hashes is much larger than the original alphabet size:
     *      10^6 -> (2^31)^2
     *
     * Given an element A[x], 0 < x <= N and 0 < A[x] <= N,
     *      hash(A[x]) = a[A[x]]
     */
    private final HashElement[] h;

    /**
     * Array of random values assigned to each array elements.
     */
    protected final HashElement[] a;

    /**
     * The set of all valid hashes mapped to a possible valid value.
     */
    private final Set<HashElement> all;

    AbstractModularSumHash(int[] A) {
        int size = PerfectlyBalancedMain.MAX;
        h = new HashElement[size+1];
        for (int i = 0; i < size; i++) {
            h[i+1] = generateRandomElement();
        }

        int N = A.length;
        a = new HashElement[N+1];
        for (int i = 0; i < N; i++) {
            a[i+1] = h[A[i]];
        }

        all = Arrays.stream(h)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean balanced(Query query) {
        int l = query.left();
        int r = query.right();
        if (l == r) {
            return true;
        }
        int n = r-l+1;
        if (n % 2 == 0) {
            return false;
        }
        // assert n is odd

        return balanced(l, r);
    }

    @Override
    public void update(int i, int v) {
        HashElement y = h[v];
        update(i, y);
        a[i] = y;
    }

    private HashElement generateRandomElement() {
        return new HashElement(nextInt(), nextInt());
    }

    private int nextInt() {
        return RANDOM.nextInt(MODULAR_ARITHMETICS.getMod());
    }

    protected HashElement add(HashElement x, HashElement y) {
        return new HashElement(
                MODULAR_ARITHMETICS.add(x.h1(), y.h1()),
                MODULAR_ARITHMETICS.add(x.h2(), y.h2())
        );
    }
    protected HashElement subtract(HashElement x, HashElement y) {
        return new HashElement(
                MODULAR_ARITHMETICS.subtract(x.h1(), y.h1()),
                MODULAR_ARITHMETICS.subtract(x.h2(), y.h2())
        );
    }

    private boolean contains(HashElement a) {
        return all.contains(a);
    }

    private boolean balanced(int l, int r) {
        int m = (l+r)/2;

        HashElement vm = rangeQuery(m, m);
        HashElement diff = subtract(rangeQuery(l, m-1), rangeQuery(m+1, r));

        return contains(add(vm, diff)) || contains(subtract(vm, diff));
    }

    protected abstract HashElement rangeQuery(int l, int r);
    protected abstract void update(int i, HashElement y);
}
