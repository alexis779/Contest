package hackercup.hackercup2022.round2.perfectlybalanced;

import java.util.HashMap;
import java.util.Map;

public class AbstractPerfectlyBalanced {

    protected int[] A;

    protected Map<Integer, Integer> diff = new HashMap<>();

    protected int operations;

    AbstractPerfectlyBalanced(int[] A) {
        this.A = A;
    }

    protected void add(int m, int i) {
        operations++;
        addUnits(i, i <= m ? +1 : -1);
    }

    protected void remove(int m, int i) {
        operations++;
        addUnits(i, i <= m ? -1 : +1);
    }

    protected void addUnits(int i, int units) {
        diff.merge(A[i], units, (ov, nv) -> (ov + nv == 0) ? null : ov + nv);
    }

    protected boolean getAnswer(int m) {
        if (unitVector(1)) {
            return true;
        }
        addUnits(m, -2);
        if (unitVector(-1)) {
            addUnits(m, +2);
            return true;
        }
        addUnits(m, +2);

        return false;
    }

    private boolean unitVector(int one) {
        return diff.size() == 1 && diff.containsValue(one);
    }
}
