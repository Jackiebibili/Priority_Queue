public interface Queue<E extends Comparable<E>> {
   public boolean push(E val);

   public E pop();

   public E peek();

   public boolean isEmpty();

   public int getLength();
}
