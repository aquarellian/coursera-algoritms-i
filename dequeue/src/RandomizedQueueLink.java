import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Project: Algorithms I
 * Author:  Tatiana Didik
 * Created: 12.02.2016 15:47
 * <p/>
 * $Id$
 */
public class RandomizedQueueLink<Item> implements Iterable<Item> {
    private Node<Item> first;

    public RandomizedQueueLink() {           // construct an empty randomized queue
    }

    public boolean isEmpty()                 // is the queue empty?
    {
        return first == null;
    }

    public int size()                        // return the number of items on the queue
    {
        int index = 0;
        for (Item i : this) {
            index++;
        }
        return index;
    }

    public void enqueue(Item item)           // add the item
    {
        if (item == null) {
            throw new NullPointerException("cannot add null to a deque");
        }
        Node oldFirst = first;
        first = new Node<Item>(item, first);
    }

    public Item dequeue()                    // remove and return a random item
    {
        if (first == null){
            throw new NoSuchElementException("Cannot dequeue from empty queue");
        }
        int index = StdRandom.uniform(size());
        if (index == 0) {
            Node<Item> value = first;
            first = first.next;
            value.next = null; // should I care?
            return value.item;
        }
        int i = 0;
        Node<Item> current = first;
        while (i + 1 < index) {
            current = current.next;
            i++;
        }
        Node<Item> toBeDeleted = current.next;
        Node<Item> next = toBeDeleted.next;
        current.next = next;
        toBeDeleted.next = null; // should I care?
        return toBeDeleted.item;
    }

    public Item sample()                     // return (but do not remove) a random item
    {
        if (first == null){
            throw new NoSuchElementException("Cannot sample from empty queue");
        }
        int index = StdRandom.uniform(size());
        if (index == 0) {
            return first.item;
        }
        int i = 0;
        Node<Item> current = first;
        while (i < index) {
            current = current.next;
            i++;
        }
        return current.item;
    }

    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new Iterator<Item>() {
            private Node<Item> current = null;
            private boolean started = false;

            @Override
            public boolean hasNext() {
                if (started) {
                    return current != null && current.next != null;
                } else {
                    return first != null;
                }

            }

            @Override
            public Item next() {
                if (current == null) {
                    if (started || first == null) {
                        throw new NoSuchElementException("Cannot iterate further");
                    }
                    current = first;
                    started = true;
                } else {
                    current = current.next;
                }
                return current.item;
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
        RandomizedQueueLink<String> deq = new RandomizedQueueLink<String>();
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
    }

    private static void printQueue(RandomizedQueueLink<String> deq) {
        for (String s : deq) {
            System.out.print(s + " ");
        }
        System.out.println();
        System.out.println("size = " + deq.size());
    }

    private class Node<Item> {
        public Node(Item item, Node<Item> next) {
            this.item = item;
            this.next = next;

        }

        Item item;
        Node<Item> next;
    }
}
