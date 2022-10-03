package codechef.starters57.TREEDIVS;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TreeAndDivisors2 extends AbstractTreeAndDivisors {

    public TreeAndDivisors2(int N, int[] A) {
        super(N, A);
    }

    @Override
    protected void updateNode(int current, int parent, List<Integer> children) {
        Map<Integer, Integer> valueExponents =  primeFactors(A[current]);

        Optional<Integer> maxOptional = children.stream()
                .filter(child -> child != parent)
                .max(Comparator.comparing(child -> primeExponents[child].size()));

        Map<Integer, Integer> parentExponents;

        if (maxOptional.isPresent()) {
            int maxChild = maxOptional.get();

            parentExponents = primeExponents[maxChild];
            mergeExponents(parentExponents, valueExponents);
            children.stream()
                    .filter(child -> !(child == parent || child == maxChild))
                    .map(child -> primeExponents[child])
                    .forEach(childExponents -> mergeExponents(parentExponents, childExponents));
        } else {
            parentExponents = valueExponents;
        }

        primeExponents[current] = parentExponents;
        divisorCount[current] = divisorCount(parentExponents);
    }
}
