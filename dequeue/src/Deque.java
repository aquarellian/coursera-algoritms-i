import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Project: Algorithms I
 * Author:  Tatiana Didik
 * Created: 12.02.2016 14:58
 * <p/>
 * $Id$
 */
public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;

    public Deque() { // construct an empty deque

    }

    public boolean isEmpty() { // is the deque empty?
        return first == null;
    }

    public int size() { // return the number of items on the deque
        int index = 0;
        for (Item i : this) {
            index++;
        }
        return index;
    }

    public void addFirst(Item item) { // add the item to the front
        if (item == null) {
            throw new NullPointerException("cannot add null to a deque");
        }
        Node oldFirst = first;
        first = new Node<Item>(item, first, null);
        if (oldFirst != null) {
            oldFirst.previous = first;
        }
        if (last == null) {
            last = first;
        }
    }

    public void addLast(Item item) { // add the item to the end
        if (item == null) {
            throw new NullPointerException("cannot add null to a deque");
        }
        Node oldLast = last;
        last = new Node<Item>(item, null, oldLast);
        if (oldLast != null) {
            oldLast.next = last;
        }
        if (first == null) {
            first = last;
        }
    }

    public Item removeFirst() { // remove and return the item from the front
        if (first != null) {
            Node<Item> oldFirst = first;
            first = first.next;
            if (first != null) {
                first.previous = null;
            } else {
                last = null; //no more items in deque
            }
            oldFirst.next = null; // should we care about that?
            return oldFirst.item;
        } else {
            throw new NoSuchElementException("attempt to remove a first item from an empty queue");
        }
    }

    public Item removeLast() { // remove and return the item from the end
        if (last != null) {
            Node<Item> oldLast = last;
            last = last.previous;
            if (last != null) {
                last.next = null;
            } else {
                first = null;  //no more items in deque
            }
            oldLast.previous = null; // should we care about that?
            return oldLast.item;
        } else {
            throw new NoSuchElementException("attempt to remove a last item from an empty queue");
        }
    }

    public Iterator<Item> iterator() {       // return an iterator over items in order from front to end

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
                    if (started) {
                        throw new NoSuchElementException("Cannot iterate further");
                    }
                    current = first;
                    started = true;
                } else {
                    current = current.next;
                }
                if (current == null) {
                    throw new NoSuchElementException("Cannot iterate further");
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
        Deque<String> deq = new Deque<String>();

        while (!"exit".equals(input)) {
            if (input.startsWith("++")) {     // add to beginning
                deq.addFirst(input);
                printQueue(deq);
            } else if (input.startsWith("+-")) {      // add to end
                deq.addLast(input);
                printQueue(deq);
            } else if (input.startsWith("-+")) {    // remove from beginning
                deq.removeFirst();
                printQueue(deq);
            } else if (input.startsWith("--")) {      // remove from end
                deq.removeLast();
                printQueue(deq);
            }
            input = scanner.next();
        }

        Iterator<String> iterator = deq.iterator();
        for (int i = 0; i <= deq.size(); i++) {
            iterator.next();
        }
    }

    private static void printQueue(Deque<String> deq) {
        for (String s : deq) {
            System.out.print(s + " ");
        }
        System.out.println();
        System.out.println("size = " + deq.size());
    }

    private class Node<Item> {
        public Node(Item item, Node<Item> next, Node<Item> previous) {
            this.item = item;
            this.next = next;
            this.previous = previous;
        }

        Item item;
        Node<Item> next;
        Node<Item> previous;
    }

}