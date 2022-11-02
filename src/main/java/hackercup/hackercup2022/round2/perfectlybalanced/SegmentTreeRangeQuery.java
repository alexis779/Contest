package hackercup.hackercup2022.round2.perfectlybalanced;

import org.tech.vineyard.binarytree.segmenttree.AbstractSegmentTree;

public class SegmentTreeRangeQuery extends AbstractModularSumHash {

    /**
     * Data structure to perform sum range queries in O(log(N)).
     */
    private final AbstractSegmentTree<HashElement> segmentTree;

    SegmentTreeRangeQuery(int[] A) {
        super(A);

        segmentTree = new AbstractSegmentTree<>(a) {
            @Override
            public HashElement neutralElement() {
                return AbstractModularSumHash.ZERO;
            }

            @Override
            public HashElement query(HashElement x, HashElement y) {
                return add(x, y);
            }
        };
    }
    protected HashElement rangeQuery(int l, int r) {
        return segmentTree.rangeQuery(l, r);
    }

    protected void update(int i, HashElement y) {
        segmentTree.update(i, y);
    }
}
