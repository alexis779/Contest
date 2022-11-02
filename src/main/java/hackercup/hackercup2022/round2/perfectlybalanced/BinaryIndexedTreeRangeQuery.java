package hackercup.hackercup2022.round2.perfectlybalanced;

import org.tech.vineyard.binarytree.binaryindexedtree.AbstractBinaryIndexedTree;

public class BinaryIndexedTreeRangeQuery extends AbstractModularSumHash {

    /**
     * Data structure to perform sum range queries in O(log(N)).
     */
    private final AbstractBinaryIndexedTree<HashElement> binaryIndexedTree;

    BinaryIndexedTreeRangeQuery(int[] A) {
        super(A);

        int N = A.length;

        binaryIndexedTree = new AbstractBinaryIndexedTree<>(N) {
            @Override
            public HashElement neutralElement() {
                return AbstractModularSumHash.ZERO;
            }

            @Override
            public HashElement add(HashElement x, HashElement y) {
                return BinaryIndexedTreeRangeQuery.this.add(x, y);
            }
        };

        for (int i = 1; i <= N; i++) {
            binaryIndexedTree.change(i, a[i]);
        }
    }
    protected HashElement rangeQuery(int l, int r) {
        HashElement prefixSum = binaryIndexedTree.get(r);
        if (l == 1) {
            return prefixSum;
        }

        return subtract(prefixSum, binaryIndexedTree.get(l-1));
    }

    protected void update(int i, HashElement y) {
        binaryIndexedTree.change(i, subtract(y, a[i]));
    }
}
