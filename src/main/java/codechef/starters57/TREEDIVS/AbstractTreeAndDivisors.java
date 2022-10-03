package codechef.starters57.TREEDIVS;

import arithmetics.ModularArithmetics;
import graph.tree.traversal.AdjacencyListDFSTraversal;
import graph.tree.traversal.Traversal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractTreeAndDivisors implements TreeAndDivisors {
    private static final int MOD = 1_000_000_007;
    public static final int MAX_A = 1_000_000;
    private static final int[] PRIME_FACTORS = getPrimeFactors(MAX_A);
    private static final ModularArithmetics MODULAR_ARITHMETICS = new ModularArithmetics(MOD);

    protected final int N;
    protected final int[] A;
    protected final int[] divisorCount;

    protected final Map<Integer, Integer>[] primeExponents;
    protected final List<List<Integer>> adjacency;


    public AbstractTreeAndDivisors(int N, int[] A) {
        this.N = N;
        this.A = A;
        divisorCount = new int[N];
        primeExponents = new Map[N];

        adjacency = IntStream.range(0, N)
                .mapToObj(i -> new ArrayList<Integer>())
                .collect(Collectors.toList());
    }

    public static String listToString(int[] divisors) {
        return Arrays.stream(divisors)
                .boxed()
                .collect(Collector.of(
                            StringBuilder::new,
                            (sb, i) -> sb.append(i).append(" "),
                            (sb1, sb2) -> sb1.append(sb2.toString()),
                            StringBuilder::toString));
    }

    private static int[] getPrimeFactors(int N) {
        int[] p = new int[N + 1];
        for (int i = 2; i <= N; i++)
            if (p[i] == 0)
                for (int j = i; j <= N; j += i) p[j] = i;
        return p;
    }

    @Override
    public void addEdge(int u, int v) {
        addNeighbor(u, v);
        addNeighbor(v, u);
    }

    @Override
    public int[] divisors() {
        dfs();
        return divisorCount;
    }

    private void addNeighbor(int u, int v) {
        adjacency.get(u).add(v);
    }


    protected void dfs() {
        Traversal traversal = new AdjacencyListDFSTraversal(adjacency);
        traversal.traverse(this::updateNode);
    }

    protected abstract void updateNode(int current, int parent, List<Integer> children);

    protected void mergeExponents(Map<Integer, Integer> parentExponents, Map<Integer, Integer> childExponents) {
        for (var entry: childExponents.entrySet()) {
            parentExponents.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }

    protected int mergeMultiplyExponents(int dc, Map<Integer, Integer> parentExponents, Map<Integer, Integer> childExponents) {
        for (var entry: childExponents.entrySet()) {
            int prime = entry.getKey();

            int parentExponent = parentExponents.getOrDefault(prime, 0);
            if (parentExponent != 0) {
                dc = divide(dc, parentExponent+1);
            }

            int exponent = entry.getValue();
            parentExponent += exponent;

            dc = multiply(dc, parentExponent+1);
            parentExponents.put(prime, parentExponent);
        }
        return dc;
    }

    public static Map<Integer, Integer> primeFactors(int a) {
        Map<Integer, Integer> primeExponents = new HashMap<>();

        while (a != 1) {
            int primeDivisor = PRIME_FACTORS[a];
            int count = 0;
            while ((a % primeDivisor) == 0) {
                count++;
                a /= primeDivisor;
            }
            primeExponents.put(primeDivisor, count);
        }
        return primeExponents;
    }

    public static int divisorCount(Map<Integer, Integer> primeExponents) {
        int exponentProduct = 1;
        for (int exponent: primeExponents.values()) {
            exponentProduct = multiply(exponentProduct, exponent+1);
        }

        return exponentProduct;
    }

    protected static int multiply(int a, int b) {
        return MODULAR_ARITHMETICS.multiply(a, b);
    }

    protected static int divide(int a, int b) {
        return multiply(a, MODULAR_ARITHMETICS.inversePrime(b));
    }
}
