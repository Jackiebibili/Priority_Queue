import java.util.*;

public class PriorityQueue<E extends Comparable<E>> implements Queue<E> {
   private List<E> array;
   private int length;
   private static final double LOAD_FACTOR = 0.50;

   public PriorityQueue() {
      this(null);
   }

   public PriorityQueue(E[] arr) {
      array = new ArrayList<E>();
      length = 0;
      for (int i = 0; arr != null && i < arr.length; i++) {
         push(arr[i]);
      }
   }

   public boolean push(E val) {
      // append to the last element
      array.add(val);
      length++;
      // percolate up the heap
      percolateUpTree(length - 1);
      return true;
   }

   public E pop() {
      if (length > 1) {
         // swap the root and the last element
         resizeArray();
         swap(0, length - 1);
         // remove the last element
         E removed = array.get(length - 1);
         length--;
         // percolate down the heap
         percolateDownTree(0, length);
         return removed;
      } else if (length == 1) {
         // one element in the list
         length--;
         E removed = array.get(0);
         array.clear();
         return removed;
      }
      return null;
   }

   public E peek() {
      // return the head of the list
      if (length > 0) {
         return array.get(0);
      }
      return null;
   }

   public boolean isEmpty() {
      return length == 0;
   }

   public int getLength() {
      return length;
   }

   private void resizeArray() {
      if (Double.compare(array.size() * LOAD_FACTOR, length) > 0) {
         // the array size is large but there are few substantive elements
         int i = 0;
         List<E> temp = new ArrayList<>(length);
         for (E item : array) {
            if (i >= length) {
               break;
            }
            temp.add(item);
            i++;
         }
         array = temp;
      }
   }

   private void percolateUpTree(int index) {
      E value = array.get(index);
      while (index > 0) {
         int parent = (index - 1) / 2;
         if (array.get(parent).compareTo(value) < 0) {
            // swap
            swap(parent, index);
            // reassign the index
            index = parent;
         } else {
            return;
         }
      }
   }

   private void percolateDownTree(int index, int maxSize) {
      int child = 1 + index * 2;
      E value = array.get(index);
      while (child < maxSize) {
         E max = value;
         int maxIndex = -1;
         for (int i = 0; i < 2 && i + child < maxSize; i++) {
            // iterate for 2 children
            if (array.get(i + child).compareTo(max) > 0) {
               max = array.get(i + child);
               maxIndex = i + child;
            }
         }
         if (maxIndex == -1) {
            return;
         } else {
            // swap the parent and the maxChild
            swap(index, maxIndex);
            // reassign the child index
            index = maxIndex;
            child = 1 + index * 2;
         }
      }
   }

   private void swap(int i, int j) {
      E temp = array.get(i);
      array.set(i, array.get(j));
      array.set(j, temp);
   }
}
