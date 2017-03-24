import edu.princeton.cs.algs4.StdIn;

/**
 * Project: Algorithms I
 * Author:  Tatiana Didik
 * Created: 12.02.2016 15:47
 * <p/>
 * $Id$
 */
public class Subset {
    public static void main(String[] args) {
        int k = Integer.valueOf(args[0]);
        RandomizedQueue<String> s = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            s.enqueue(item);
        }

        for (int i = 0; i < k; i++) {
            System.out.println(s.dequeue());
        }
    }
}
