package codechef.starters57.TREEDIVS;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TreeAndDivisorsTest {
    private static final int T = 100;

    @ParameterizedTest
    @MethodSource
    public void correctness(int[] A, int[][] edges, int[] expectedDivisors) {
        TreeAndDivisorsFactory factory = TreeAndDivisors3::new;
        TreeAndDivisors treeAndDivisors = factory.createTreeAndDivisors(A.length, A);
        Arrays.stream(edges)
                .forEach(edge -> treeAndDivisors.addEdge(edge[0], edge[1]));
        Assertions.assertArrayEquals(expectedDivisors, treeAndDivisors.divisors());
    }

    static Object[][] correctness() {
        return new Object[][] {
                new Object[] {
                        new int[] { 100, 101, 102, 103 },
                        new int[][] { { 0, 1 }, { 0, 2 }, { 0, 3 } },
                        new int[] { 192, 2, 8, 2 } }
        };
    }

    @ParameterizedTest(name = "{0}, {1}, N={2}")
    @MethodSource
    void run(String inputType, String factoryType, int N, TreeGenerator generator, TreeAndDivisorsFactory factory) {
        runBatch(T, N, factory, generator);
    }

    static Stream<Arguments> run() {
        TreeAndDivisorsFactory factory1 = TreeAndDivisors1::new;
        TreeAndDivisorsFactory factory2 = TreeAndDivisors2::new;
        TreeAndDivisorsFactory factory3 = TreeAndDivisors3::new;
        TreeAndDivisorsFactory factory4 = TreeAndDivisors4::new;

        return Stream.of(
                    run(factory1, "Original"),
                    run(factory4, "Null out children maps"),
                    run(factory2, "Reuse max child Map"),
                    run(factory3, "50% less Map scans")
                )
                .flatMap(Function.identity());
    }

    static Stream<Arguments> run(TreeAndDivisorsFactory factory, String factoryDescription) {
        TreeGenerator randomGenerator = TreeAndDivisorsTest::setRandomAdjacency;
        TreeGenerator thickGenerator = TreeAndDivisorsTest::setThickAdjacency;
        TreeGenerator slimGenerator = TreeAndDivisorsTest::setSlimAdjacency;

        return Stream.of(
                arguments("random", factoryDescription, 30_000, randomGenerator, factory),
                arguments("slim", factoryDescription, 4_000, slimGenerator, factory)
        );
    }

    private static Random random;

    @BeforeEach
    public void initSeed() {
        random = new Random(0);
    }

    private void runBatch(int T, int N, TreeAndDivisorsFactory factory, TreeGenerator generator) {
        while (T > 0) {
            int[] A = new int[N];

            for (int i = 0; i < N; i++) {
                A[i] = 1 + random.nextInt(AbstractTreeAndDivisors.MAX_A);
            }

            runInput(factory, generator, A);
            T--;
        }
    }

    private void runInput(TreeAndDivisorsFactory factory, TreeGenerator generator, int[] A) {
        int N = A.length;

        List<List<Integer>> adjacency = IntStream.range(0, N)
                .mapToObj(i -> new ArrayList<Integer>())
                .collect(Collectors.toList());

        generator.generate(adjacency);

        TreeAndDivisors treeAndDivisors = factory.createTreeAndDivisors(N, A);
        for (int i = 0; i < N; i++) {
            for (int child: adjacency.get(i)) {
                treeAndDivisors.addEdge(i, child);
            }
        }

        treeAndDivisors.divisors();
    }

    static void setThickAdjacency(List<List<Integer>> adjacency) {
        for (int i = 1; i < adjacency.size(); i++) {
            adjacency.get(0).add(i);
        }
    }

    static void setSlimAdjacency(List<List<Integer>> adjacency) {
        for (int i = 1; i < adjacency.size(); i++) {
            adjacency.get(i-1).add(i);
        }
    }

    static void setRandomAdjacency(List<List<Integer>> adjacency) {
        for (int i = 1; i < adjacency.size(); i++) {
            int parent = random.nextInt(i);
            adjacency.get(parent).add(i);
        }
    }
}
