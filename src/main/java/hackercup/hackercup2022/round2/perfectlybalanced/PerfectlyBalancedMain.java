package hackercup.hackercup2022.round2.perfectlybalanced;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

public class PerfectlyBalancedMain {

    /**
     * Max value for all the array elements.
     */
    public static final int MAX = 1_000_000;

    public static void main(String[] args) throws Exception {
        RWBalancedFactory factory = BinaryIndexedTreeRangeQuery::new;

        //InputStream inputStream = System.in;
        InputStream inputStream = new FileInputStream("perfectly_balanced_chapter_2_808919960251121_input.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        //OutputStream outputStream = System.out;
        OutputStream outputStream = new FileOutputStream("perfectly_balanced_chapter_2_808919960251121_output.txt");
        PrintWriter writer = new PrintWriter(new BufferedOutputStream(outputStream));

        String[] tokens;

        tokens = bufferedReader.readLine().split(" ");
        int T = Integer.parseInt(tokens[0]);
        for (int t = 1; t <= T; t++) {
            tokens = bufferedReader.readLine().split(" ");
            int N = Integer.parseInt(tokens[0]);
            tokens = bufferedReader.readLine().split(" ");
            int[] A = new int[N];
            for (int i = 0; i < N; i++) {
                A[i] = Integer.parseInt(tokens[i]);
            }

            tokens = bufferedReader.readLine().split(" ");
            int Q = Integer.parseInt(tokens[0]);

            int balanced = 0;
            RWBalanced perfectlyBalanced = factory.create(A);
            for (int i = 0; i < Q; i++) {
                tokens = bufferedReader.readLine().split(" ");
                int[] query = Arrays.stream(tokens)
                        .mapToInt(Integer::parseInt)
                        .toArray();
                RWQuery rwQuery = new RWQuery(QueryType.fromType(query[0]), new Query(query[1], query[2]));
                if (rwQuery(perfectlyBalanced, rwQuery)) {
                    balanced++;
                }
            }

            writer.println(String.format("Case #%d: %d", t, balanced));
        }

        writer.close();
        //outputStream.close();
        inputStream.close();
    }

    public static boolean rwQuery(RWBalanced perfectlyBalanced, RWQuery rwQuery) {
        Query query = rwQuery.query();
        switch (rwQuery.type()) {
            case SET:
                perfectlyBalanced.update(query.left(), query.right());
                return false;
            case GET:
                return perfectlyBalanced.balanced(query);
            default:
                return false;
        }
    }
}
