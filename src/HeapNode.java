public class HeapNode<E extends Comparable<E>> extends Node<E> {
   private Node<E> parent;

   public HeapNode() {
      super();
      parent = null;
   }

   public HeapNode(E load) {
      super(load);
      parent = null;
   }

   public void setParent(Node<E> node) {
      parent = node;
   }

   public HeapNode<E> getParent() {
      return (HeapNode<E>) parent;
   }

   public HeapNode<E> getLeft() {
      return (HeapNode<E>) super.getLeft();
   }

   public HeapNode<E> getRight() {
      return (HeapNode<E>) super.getRight();
   }

}
