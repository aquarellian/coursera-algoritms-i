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
public class RandomizedQueuePrev<Item> implements Iterable<Item> {
    private Item[] arr;
    private int last = -1;
    private int first = -1;


    public RandomizedQueuePrev() {                             // construct an empty deque
        arr = (Item[]) new Object[0];
    }

    public boolean isEmpty() {                // is the deque empty?
        return last == -1;
    }

    public int size() {                        // return the number of items on the deque
        return last + 1;
    }

    public void enqueue(Item item)           // add the item
    {
        if (item == null) {
            throw new NullPointerException("cannot add null to a deque");
        }
        if (last <= 0 || last == arr.length - 1) {
            int size = arr.length == 0 ? 1 : arr.length * 2;
            resize(size);
        }
        arr[++last] = item;
        if (first == -1){
            first = 0;
        }
    }

    public Item dequeue()                    // remove and return a random item
    {
        int size = size();
        if (size() == 0) {
            throw new NoSuchElementException("Cannot dequeue from empty queue");
        }
        int index = StdRandom.uniform(size);
        Item toBeDeleted = arr[index];

        int newSize;
        if (last <= arr.length / 4) {
            newSize = arr.length / 2;
            Item[] newArr = (Item[]) new Object[newSize];
            for (int i = 0, j = 0; i <= last; i++) {
                if (i != index) {
                    newArr[j++] = arr[i];
                }
            }
            arr = newArr;
        } else {
            for (int i = index; i < size - 1; i++) {
                if (arr[i] == null) {
                    break;
                }
                arr[i] = arr[i + 1];
            }
            arr[size - 1] = null;
        }
        last--;
        return toBeDeleted;
    }

    public Item sample()                     // return (but do not remove) a random item
    {
        if (size() == 0) {
            throw new NoSuchElementException("Cannot sample from empty queue");
        }
        int index = StdRandom.uniform(size());
        return arr[index];
    }

    private void resize(int capacity){
//        int size = arr.length == 0 ? 1 : arr.length * 2;
        Item[] newArr = (Item[]) new Object[capacity];
        int i = 0;
        while (i <= last) {     // we do not allow nulls -> that means values in structure has ended
            newArr[i] = arr[i];
            i++;
        }
        arr = newArr;
        //last = i - 1;
    }

//    private void incrementSize() {
//        int size = arr.length == 0 ? 1 : arr.length * 2;
//        Item[] newArr = (Item[]) new Object[size];
//        int i = 0;
//        while (i < arr.length && arr[i] != null) {     // we do not allow nulls -> that means values in structure has ended
//            newArr[i] = arr[i];
//            i++;
//        }
//        arr = newArr;
//        last = i - 1;
//    }

    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new Iterator<Item>() {
//            int[] unused = new int[size()];
            int used;
            int[] indexes = new int[size()];

            {
                for (int i = 0; i < size(); i++) {
                    indexes[i] = i;
                }
            }

//            {
//                for (int i = 0; i < size(); i++) {
//                    unused[i] = i;
//                }
//            }

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
                int i = indexes[index];
                Item item = arr[i];
                indexes[index] = indexes[size - 1 - used];
                indexes[size - 1 - used] = index;
                used++;
                return item;
            }

//            @Override
//            public boolean hasNext() {
//                return unused.length > 0;
//            }
//
//
//            @Override
//            public Item next() {
//                if (unused.length == 0) {
//                    throw new NoSuchElementException("Cannot iterate in empty queue");
//                }
//                int index = StdRandom.uniform(unused.length);
//                Item item = arr[unused[index]];
//
//                int[] newUnused = new int[unused.length - 1];
//                for (int i = 0, j = -1; i < unused.length; i++) {
//                    if (i != index) {
//                        newUnused[++j] = unused[i];
//                    }
//                }
//
//                unused = newUnused;
//                return item;
//            }

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
        RandomizedQueuePrev<String> deq = new RandomizedQueuePrev<String>();

//        for (long i = 0; i < 500L; i++) {
//            deq.enqueue(i);
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
                deq.dequeue();
                printQueue(deq);
            }
            input = scanner.next();
        }
//        Iterator<Long> iterator = deq.iterator();
        Iterator<String> iterator = deq.iterator();
        for (int i = 0; i < deq.size(); i++) {
            iterator.next();
        }
        for (int i = 0; i < 500; i++) {
            deq.dequeue();
            printQueue(deq);
        }
    }

    private static void printQueue(RandomizedQueuePrev deq) {
        for (Object s : deq) {
            System.out.print(s + " ");
        }
        System.out.println();
        System.out.println("size = " + deq.size());
        System.out.println(deq.arr.length);
    }

}