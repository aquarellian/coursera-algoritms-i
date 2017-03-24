import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Project: Sonar-Gerrit Plugin
 * Author:  Tatiana Didik
 * Created: 12.02.2016 16:50
 * <p/>
 * $Id$
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] arr;
    private int last = -1;
    private int first = -1;


    public RandomizedQueue() {                             // construct an empty deque
        arr = (Item[]) new Object[0];
    }

    public boolean isEmpty() {                // is the deque empty?
        return last == -1;
    }

    public int size() {                        // return the number of items on the deque
        return first == -1 ? 0 : last + 1 - first;
    }

    public void enqueue(Item item)           // add the item
    {
        if (item == null) {
            throw new NullPointerException("cannot add null to a deque");
        }
        if (last <= 0 || last == arr.length - 1) {
            int size = arr.length == 0 ? 1 : arr.length * 2;
            resize(size);
//        }  else if (last - first <= arr.length / 4) {
//            resize(arr.length / 2);
        }
        arr[++last] = item;
        if (first == -1) {
            first = 0;
        }
    }

    public Item dequeue()                    // remove and return a random item
    {
        int size = size();
        if (size == 0) {
            throw new NoSuchElementException("Cannot dequeue from empty queue");
        }
        int index = StdRandom.uniform(first, last + 1);
        Item toBeDeleted = arr[index];
        arr[index] = arr[first];
        arr[first] = toBeDeleted;
        first++;
        if (last - first <= arr.length / 4) {
            resize(arr.length / 2);
        }
        return toBeDeleted;
    }

    public Item sample() {                     // return (but do not remove) a random item
        int size = size();
        if (size == 0) {
            throw new NoSuchElementException("Cannot sample from empty queue");
        }
        int index = StdRandom.uniform(first, last + 1);
        return arr[index];
    }

    private void resize(int capacity) {
        Item[] newArr = (Item[]) new Object[capacity];
        int j = -1;
        if (first != -1) {
            for (int i = first; i <= last; i++) {
                newArr[++j] = arr[i];
            }
            first = 0;
            last = j;
        }
        arr = newArr;
    }

    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new Iterator<Item>() {

            int used;
            int[] indexes = new int[size()];

            {
                for (int i = 0; i < size(); i++) {
                    indexes[i] = i;
                }
            }

            @Override
            public boolean hasNext() {
                return used < size();
            }

            @Override  //will fail if same queue is used in several threads
            public synchronized Item next() {
                int size = size();
                if (used >= size) {
                    throw new NoSuchElementException("Cannot iterate in empty queue");
                }
                int index = StdRandom.uniform(size - used);
                int i = first + indexes[index];
                Item item = arr[i];
                indexes[index] = indexes[size - 1 - used];
                indexes[size - 1 - used] = index;
                used++;
                return item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static void main(String[] args) {  // unit testing
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
//        RandomizedQueue<Long> deq = new RandomizedQueue<Long>();
        RandomizedQueue<String> deq = new RandomizedQueue<String>();

//        for (long i = 0; i < 100L; i++) {
//            if (deq.last < (deq.arr.length - 1)) {
//                deq.enqueue(i);
//                while (deq.last - deq.first > deq.arr.length / 4){
//                    deq.dequeue();
//                }
//            }
//            System.out.println("i = ");
//            printQueue(deq);
//        }

        while (!"exit".equals(input)) {
            if (input.startsWith("++")) {     // add to beginning
                deq.enqueue(input);
                printQueue(deq);
            } else if (input.startsWith("+-") || input.startsWith("-+")) {      // add to end
                System.out.println("sample = " + deq.sample());
                printQueue(deq);
            } else if (input.startsWith("--")) {      // remove from end
                System.out.println("removed: " + deq.dequeue());
                printQueue(deq);
            }
            input = scanner.next();
        }
//        Iterator<Long> iterator = deq.iterator();
//        Iterator<String> iterator = deq.iterator();
//        for (int i = 0; i < deq.size(); i++) {
//            iterator.next();
//        }
//        for (int i = 0; i < 100; i++) {
//            deq.dequeue();
//            printQueue(deq);
//        }
    }

    private static void printQueue(RandomizedQueue deq) {
        for (Object s : deq) {
            System.out.print(s + " ");
        }
        System.out.println();
        System.out.println("size = " + deq.size());
        System.out.println(deq.arr.length);
    }

}