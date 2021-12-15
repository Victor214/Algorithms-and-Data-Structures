import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private Integer head, tail;
    private Integer size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[2];
        head = 0;
        tail = 0;
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // method to verify if its full
    private boolean isFull() {
        return size == queue.length;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // Sums one to the index, taking into account array boundaries.
    private int sumOne(int number) {
        return (number + 1) % queue.length;
    }

    // doubles array size and adjusts head and tail indexes.
    private void extend() {
        /*
        System.out.println("-------------------------");
        for (Item item : queue) {
            System.out.print(item + " ");
        }
        System.out.println();
         */

        Item[] newQueue = (Item[]) new Object[queue.length * 2];
        int count = 0;
        for (int i = head; count < size; i = sumOne(i), count++) {
            newQueue[count] = queue[i];
            // System.out.println("newQueue[" + count + "] = " + queue[i] + " (i = " + i + ")");
        }
        queue = newQueue;
        head = 0;
        tail = size;

        /*
        System.out.println("New Array Formation :");
        for (Item item : queue) {
            System.out.print(item + " ");
        }
        System.out.println();
        System.out.println("Head : " + head + " / Tail : " + tail);
        System.out.println("-------------------------");
         */
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        if (isFull()) {
            extend();
        }

        queue[tail] = item;
        tail = sumOne(tail);
        this.size += 1;

        /*
        System.out.println("-------------------------");
        System.out.println("Enqueue :");
        for (Item loopItem : queue)
            System.out.print(loopItem + " ");
        System.out.println();
        System.out.println("-------------------------");
         */
    }

    private int getRandomIndex() {
        int randomInt = StdRandom.uniform(size);
        return (head + randomInt) % queue.length; // Mod queue.length means, if the calculated index goes beyond the limit, start at the index 0 again.
    }

    private void swapQueueElement(int indexA, int indexB) {
        Item temp = queue[indexA];
        queue[indexA] = queue[indexB];
        queue[indexB] = temp;
    }

    // remove and return a random item
    // For dequeue, pick a random element, remove it, get the head element onto this new position, and increment one into head (thats it right?)
    // implement sample first, and use it to get the random element
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        /*
        if (isFourthPart()) {
            shrink();
        }
        */

        swapQueueElement(getRandomIndex(), head); // Swap content of head with random index within head and tail boundaries.
        Item result = queue[head];
        head = sumOne(head);
        this.size -= 1;
        return result;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();

        return queue[getRandomIndex()];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    private class RandomQueueIterator implements Iterator<Item> {
        private final int[] shuffleIndex;
        private int currentIndex;

        public RandomQueueIterator() {
            shuffleIndex = new int[size()];
            int count = 0;
            for (int i = head; count < size; i = sumOne(i), count++) {
                shuffleIndex[count] = i;
            }
            currentIndex = 0;
        }

        private void swapShuffleElement(int indexA, int indexB) {
            int temp = shuffleIndex[indexA];
            shuffleIndex[indexA] = shuffleIndex[indexB];
            shuffleIndex[indexB] = temp;
        }

        public boolean hasNext() {
            return currentIndex != shuffleIndex.length;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            int randomIndex = currentIndex + StdRandom.uniform(shuffleIndex.length - currentIndex);
            swapShuffleElement(currentIndex, randomIndex);
            Item result = queue[shuffleIndex[currentIndex]];
            currentIndex++;
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);
        queue.enqueue(6);
        queue.enqueue(7);
        queue.enqueue(8);
        queue.enqueue(9);

        System.out.println("Random (1) : " + queue.dequeue());
        System.out.println("Random (2) : " + queue.dequeue());
        for (Integer i : queue) {
            System.out.println("Loop element : " + i);
        }

        for (int i = 0; i < 5; i++)
            System.out.println("Sample : " + queue.sample());
    }

}
