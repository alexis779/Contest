package hackercup.hackercup2022.round2.perfectlybalanced;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tech.vineyard.recurrence.HilbertCurve;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PerfectlyBalancedTest {

    private static final String S = "singingbanana";
    private static final int N = PerfectlyBalancedMain.MAX;
    private static final int Q = PerfectlyBalancedMain.MAX;

    private Random random;

    @BeforeEach
    public void initSeed() {
        random = new Random(0);
    }

    @ParameterizedTest
    @MethodSource("roFactories")
    public void roFactoriesCorrectness(ROBalancedFactory factory) throws IOException {
        int[] A = getIntArray();
        Query[] queries = getROQueries();

        ROBalanced perfectlyBalanced = factory.create(A);
        assertEquals(4, perfectlyBalanced.balanced(queries));
    }

    @ParameterizedTest
    @MethodSource("rwFactories")
    public void rwFactoriesCorrectness(RWBalancedFactory factory) throws IOException {
        int[] A = new int[] { 2, 1, 5, 1, 5, 1 };
        RWQuery[] queries = getRWQueries();

        RWBalanced perfectlyBalanced = factory.create(A);
        int balanced = (int) Arrays.stream(queries)
                .filter(query -> PerfectlyBalancedMain.rwQuery(perfectlyBalanced, query))
                .count();
        assertEquals(7, balanced);
    }

    @ParameterizedTest
    @MethodSource
    @Disabled
    public void naiveLoad(int n) {
        int[] A = generateArray(n);
        PerfectlyBalancedNaive perfectlyBalanced = new PerfectlyBalancedNaive(A);
        Query[] queries = generateROQueries(n, Q);
        RWQuery[] rwQueries = Arrays.stream(queries)
                .map(query -> new RWQuery(QueryType.GET, query))
                .toArray(RWQuery[]::new);

        int count = (int) Arrays.stream(rwQueries)
                .filter(query -> PerfectlyBalancedMain.rwQuery(perfectlyBalanced, query))
                .count();
        System.err.println(String.format("N=%d, Naive %d operations, %d", n, perfectlyBalanced.operations, count));
    }

    @ParameterizedTest
    @MethodSource("moLoad")
    @Disabled
    public void moRightAscending(int n) {
        int q = Q;
        int blockSize = n / ((int) Math.sqrt(q));
        Comparator<Query> comparator = Comparator.<Query>comparingInt(query -> query.left() / blockSize)
                .thenComparingInt(Query::right);
        runMo(n, q, comparator);
    }

    @ParameterizedTest
    @MethodSource("moLoad")
    @Disabled
    public void moRightAscendingDescending(int n) {
        int q = Q;
        int blockSize = n / ((int) Math.sqrt(q));
        Comparator<Query> comparator = Comparator.<Query>comparingInt(query -> query.left() / blockSize)
                .thenComparingInt(query -> (query.left() / blockSize) % 2 == 0 ? query.right() : -query.right());
        runMo(n, q, comparator);
    }

    @ParameterizedTest
    @MethodSource("moBlockSize")
    @Disabled
    public void moBlockSize(int blockSize) {
        int n = N/2;
        int q = Q;
        Comparator<Query> comparator = Comparator.<Query>comparingInt(query -> query.left() / blockSize)
                //.thenComparingInt(Query::right);
                .thenComparingInt(query -> (query.left() / blockSize) % 2 == 0 ? query.right() : -query.right());
        runMo(n, q, comparator);
    }

    @ParameterizedTest
    @MethodSource("moLoad")
    @Disabled
    public void moHilbert(int n) {
        int q = Q;
        runMo(n, q, getHilbertCurveComparator(n));
    }

    @ParameterizedTest
    @MethodSource("moQuerySize")
    @Disabled
    public void moHilbertQuerySize(int q) {
        int n = N;
        runMo(n, q, getHilbertCurveComparator(n));
    }

    @ParameterizedTest
    @MethodSource
    public void rwFactoriesLoad(RWBalancedFactory factory) {
        int[] A = generateArray(N);
        RWBalanced perfectlyBalanced = factory.create(A);
        RWQuery[] rwQueries = generateRWQueries(N, Q);
        Arrays.stream(rwQueries)
                .filter(query -> PerfectlyBalancedMain.rwQuery(perfectlyBalanced, query))
                .count();
    }

    private int[] generateArray(int n) {
        return IntStream.range(0, n)
                .map(i -> 1+random.nextInt(n))
                .toArray();
    }

    private Query[] generateROQueries(int n, int q) {
        return IntStream.range(0, q)
                .mapToObj(i -> generateROQuery(n))
                .toArray(Query[]::new);
    }

    private Query generateROQuery(int n) {
        int left = random.nextInt(n);
        int right = left + 2 * random.nextInt((n-left+1)/2);
        return new Query(left+1, right+1);
    }

    private void runMo(int n, int q, Comparator<Query> comparator) {
        int[] A = generateArray(n);
        PerfectlyBalancedMO perfectlyBalanced = new PerfectlyBalancedMO(A, comparator);
        Query[] queries = generateROQueries(n, q);
        perfectlyBalanced.balanced(queries);
        System.err.println(String.format("MO %d %d operations", n, perfectlyBalanced.operations));
    }

    private int[] getIntArray() {
        return toIntArray(S);
    }

    private Query[] getROQueries() throws IOException {
        return loadQueries("ro_queries.csv").stream()
                .map(query -> new Query(query[0], query[1]))
                .toArray(Query[]::new);
    }

    private RWQuery[] getRWQueries() throws IOException {
        return loadQueries("rw_queries.csv")
                .stream()
                .map(row -> new RWQuery(QueryType.fromType(row[0]), new Query(row[1], row[2])))
                .toArray(RWQuery[]::new);
    }

    private static int[] toIntArray(String S) {
        int[] A = new int[S.length()];
        for (int i = 0; i < S.length(); i++) {
            A[i] = S.charAt(i) - 'a';
        }
        return A;
    }

    private RWQuery[] generateRWQueries(int n, int q) {
        return IntStream.range(0, q)
                .mapToObj(i -> generateRWQuery(n))
                .toArray(RWQuery[]::new);
    }

    private RWQuery generateRWQuery(int n) {
        int type = 1+random.nextInt(2);
        return new RWQuery(QueryType.fromType(type), generateROQueryForRW(type, n));
    }

    private Query generateROQueryForRW(int type, int n) {
        switch (type) {
            case 1 -> {
                int x = random.nextInt(n);
                int y = random.nextInt(n);
                return new Query(x+1, y+1);
            }
            case 2 -> {
                return generateROQuery(n);
            }
        }
        return null;
    }

    private static List<int[]> loadQueries(String csvFile) throws IOException {
        List<int[]> queries = new ArrayList<>();
        InputStream inputStream = getInputStream(csvFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(" ");
            int[] query = Arrays.stream(tokens)
                    .mapToInt(Integer::parseInt)
                    .toArray();
            queries.add(query);
        }
        return queries;
    }

    private static InputStream getInputStream(String csvFile) {
        return PerfectlyBalancedTest.class.getResourceAsStream(csvFile);
    }

    private Comparator<Query> getHilbertCurveComparator(int n) {
        int maxn = Integer.highestOneBit(n) << 1;
        HilbertCurve hilbertCurve = new HilbertCurve(maxn);
        return Comparator.comparingLong(query -> hilbertCurve.hilbertCurveOrder(query.left()-1, query.right()-1));
    }

    static Stream<ROBalancedFactory> roFactories() {
        return Stream.of(
                PerfectlyBalancedSmallAlphabet::new,
                PerfectlyBalancedMO::new
        );
    }

    static Stream<RWBalancedFactory> rwFactories() {
        return Stream.concat(
                Stream.of(PerfectlyBalancedNaive::new),
                rwFactoriesLoad()
        );
    }

    static Stream<RWBalancedFactory> rwFactoriesLoad() {
        return Stream.of(
                BinaryIndexedTreeRangeQuery::new,
                SegmentTreeRangeQuery::new
        );
    }

    static Stream<Integer> naiveLoad() {
        return Stream.of(100, 1000, 5000, 10000, 15000);
    }
    static Stream<Integer> moLoad() {
        return Stream.of(1000, 5000, 10000, 15000, 50000, 100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000);
    }
    static Stream<Integer> moBlockSize() {
        return Stream.of(500, 1000, 1500, 2000, 2500, 3000, 3500, 4000);
    }

    static Stream<Integer> moQuerySize() {
        return Stream.of(1000, 10000, 100000, 500000, 1000000);
    }
}