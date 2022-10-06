package codechef.starters57.TREEDIVS;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class TreeAndDivisorsMain {
    public static void main(String[] args) throws IOException {
        TreeAndDivisorsFactory factory = TreeAndDivisors4::new;

        //InputStream inputStream = System.in;
        InputStream inputStream = new FileInputStream("TREEDIVS");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter writer = new PrintWriter(new BufferedOutputStream(System.out));

        String[] tokens;

        tokens = bufferedReader.readLine().split(" ");
        int T = Integer.parseInt(tokens[0]);
        while (T > 0) {
            tokens = bufferedReader.readLine().split(" ");
            int N = Integer.parseInt(tokens[0]);
            tokens = bufferedReader.readLine().split(" ");

            int[] A = new int[N];
            for (int i = 0; i < N; i++) {
                A[i] = Integer.parseInt(tokens[i]);
            }

            TreeAndDivisors treeAndDivisors = factory.createTreeAndDivisors(N, A);
            for (int i = 0; i < N-1; i++) {
                tokens = bufferedReader.readLine().split(" ");
                int u = Integer.parseInt(tokens[0]);
                int v = Integer.parseInt(tokens[1]);
                treeAndDivisors.addEdge(u-1, v-1);
            }

            int[] divisorCount = treeAndDivisors.divisors();
            String divisorLine = AbstractTreeAndDivisors.listToString(divisorCount);
            writer.println(divisorLine);

            T--;
        }

        writer.close();
        inputStream.close();
    }
}
