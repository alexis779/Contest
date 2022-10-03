package codechef.starters57.TREEDIVS;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TreeAndDivisors3 extends AbstractTreeAndDivisors {

    public TreeAndDivisors3(int N, int[] A) {
        super(N, A);
    }

    @Override
    protected void updateNode(int current, int parent, List<Integer> children) {
        Map<Integer, Integer> valueExponents =  primeFactors(A[current]);

        Optional<Integer> maxOptional = children.stream()
                .filter(child -> child != parent)
                .max(Comparator.comparing(child -> primeExponents[child].size()));

        Map<Integer, Integer> parentExponents;
        int dc;

        if (maxOptional.isPresent()) {
            int maxChild = maxOptional.get();

            parentExponents = primeExponents[maxChild];
            dc = divisorCount[maxChild];

            dc = mergeMultiplyExponents(dc, parentExponents, valueExponents);

            for (int child: children) {
                if (child == parent || child == maxChild) {
                    continue;
                }
                dc = mergeMultiplyExponents(dc, parentExponents, primeExponents[child]);
            }
        } else {
            parentExponents = valueExponents;
            dc = divisorCount(valueExponents);
        }

        primeExponents[current] = parentExponents;
        divisorCount[current] = dc;
    }
}
