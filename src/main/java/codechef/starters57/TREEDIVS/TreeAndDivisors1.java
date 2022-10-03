package codechef.starters57.TREEDIVS;

import java.util.List;
import java.util.Map;

public class TreeAndDivisors1 extends AbstractTreeAndDivisors {

    public TreeAndDivisors1(int N, int[] A) {
        super(N, A);
    }

    @Override
    protected void updateNode(int current, int parent, List<Integer> children) {
        Map<Integer, Integer> parentExponents =  primeFactors(A[current]);

        children.stream()
                .filter(child -> !(child == parent))
                .map(child -> primeExponents[child])
                .forEach(childExponents -> mergeExponents(parentExponents, childExponents));

        primeExponents[current] = parentExponents;
        divisorCount[current] = divisorCount(parentExponents);
    }
}
