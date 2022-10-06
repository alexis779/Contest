package codechef.starters57.TREEDIVS;

import java.util.List;
import java.util.Map;

public class TreeAndDivisors4 extends AbstractTreeAndDivisors {

    public TreeAndDivisors4(int N, int[] A) {
        super(N, A);
    }

    @Override
    protected void updateNode(int current, int parent, List<Integer> children) {
        Map<Integer, Integer> parentExponents =  primeFactors(A[current]);

        children.stream()
                .filter(child -> !(child == parent))
                .forEach(child -> {
                    mergeExponents(parentExponents, primeExponents[child]);
                    primeExponents[child] = null;
                });

        primeExponents[current] = parentExponents;
        divisorCount[current] = divisorCount(parentExponents);
    }
}
