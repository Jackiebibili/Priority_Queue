public class PQWithTree<E extends Comparable<E>> implements Queue<E> {
   private HeapNode<E> root;
   private HeapNode<E> last;
   private HeapNode<E> lastLevelFirst;
   private int length;
   private int level;

   public PQWithTree() {
      this(null);
   }

   public PQWithTree(E[] arr) {
      root = last = lastLevelFirst = null;
      length = 0;
      level = -1;
      for (int i = 0; arr != null && i < arr.length; i++) {
         push(arr[i]);
      }
   }

   public boolean push(E val) {
      HeapNode<E> node = add(val);
      percolateUpTree(node);
      return true;
   }

   public E pop() {
      swapRootWithLast();
      Node<E> delete = remove();
      percolateDownTree(root);
      return delete.getPayload();
   }

   public int getLength() {
      return length;
   }

   public E peek() {
      return root.getPayload();
   }

   public boolean isEmpty() {
      return length <= 0;
   }

   private void percolateUpTree(HeapNode<E> node) {
      HeapNode<E> parent = node.getParent();
      while (parent != null) {
         if (parent.compareTo(node) < 0) {
            swapNode(parent, node);
            parent = node.getParent();
         } else {
            return;
         }
      }
   }

   private void percolateDownTree(HeapNode<E> node) {
      HeapNode<E> child = node.getLeft();
      while (child != null) {
         HeapNode<E> max = node;
         HeapNode<E> rightChild = node.getRight();
         if (child.compareTo(max) > 0) {
            max = child;
         }
         if (rightChild != null && rightChild.compareTo(max) > 0) {
            max = rightChild;
         }

         if (max == node) {
            return;
         } else {
            swapNode(node, max);
            child = node.getLeft();
         }
      }
   }

   private HeapNode<E> add(E val) {
      HeapNode<E> node = new HeapNode<>(val);
      if (root == null) {
         // empty heap
         root = last = lastLevelFirst = node;
         level++;
      } else if (root == last) {
         // one element in heap
         root.setLeft(node);
         node.setParent(root);
         lastLevelFirst = last = node;
         level++;
      } else {
         // more elements in heap
         // get the last's parent
         HeapNode<E> lastParent = last.getParent();
         // set the right child -- if right child is null
         if (lastParent.getRight() == null) {
            lastParent.setRight(node);
            node.setParent(lastParent);
         } else {
            // get the next last node
            lastParent = getNextLastNodeParent(last);
            if (lastParent != null) {
               lastParent.setLeft(node);
               node.setParent(lastParent);
            } else {
               // the last level of the tree is full -- move on to the next level
               lastLevelFirst.setLeft(node);
               node.setParent(lastLevelFirst);
               lastLevelFirst = node;
               level++;
            }
         }
         last = node;
      }
      length++;
      return node;
   }

   private HeapNode<E> remove() {
      HeapNode<E> delete = null;
      if (root == null) {
         return null;
      } else if (root == last) {
         delete = root;
         root = last = lastLevelFirst = null;
         length--;
         return delete;
      } else {
         HeapNode<E> lastParent = last.getParent();
         if (lastParent.getRight() != null) {
            // clear the right child
            delete = lastParent.getRight();
            delete.setParent(null);
            lastParent.setRight(null);
            last = lastParent.getLeft();
         } else {
            // only the left child exists
            delete = lastParent.getLeft();
            delete.setParent(null);
            lastParent = getPreviousLastNodeParent(last);
            if (lastParent != null) {
               last.getParent().setLeft(null);
               last = lastParent.getRight();
            } else {
               // the last level of the tree is empty -- move on to the previous level
               lastLevelFirst = last.getParent();
               last = root;
               while (last.getRight() != null) {
                  last = last.getRight();
               }
               level--;
            }
         }
         length--;
         return delete;
      }
   }

   private HeapNode<E> getNextLastNodeParent(HeapNode<E> node) {
      HeapNode<E> parent = node.getParent();
      // going up the tree
      while (parent != null && parent.getRight() == node) {
         node = parent;
         parent = node.getParent();
      }
      if (parent == null) {
         return null;
      }
      // switch branches
      parent = parent.getRight();
      // going down the tree
      while (parent.getLeft() != null) {
         parent = parent.getLeft();
      }
      return parent;
   }

   private HeapNode<E> getPreviousLastNodeParent(HeapNode<E> node) {
      HeapNode<E> parent = node.getParent();
      // going up the tree
      while (parent != null && parent.getLeft() == node) {
         node = parent;
         parent = node.getParent();
      }
      if (parent == null) {
         return null;
      }
      // switch branches
      parent = parent.getLeft();
      // going down the tree
      while (parent.getRight() != null) {
         parent = parent.getRight();
      }
      return parent;

   }

   private void swapProperties(HeapNode<E> parent, HeapNode<E> node) {
      if (node == last) {
         last = parent;
      }
      if (node == lastLevelFirst) {
         lastLevelFirst = parent;
      }
   }

   private void swapNode(HeapNode<E> parent, HeapNode<E> node) {
      HeapNode<E> grand = parent.getParent();
      HeapNode<E> left = node.getLeft();
      HeapNode<E> right = node.getRight();

      swapProperties(parent, node);

      if (grand != null) {
         if (grand.getRight() == parent) {
            // parent is the right node of grandparent
            grand.setRight(node);
         } else {
            grand.setLeft(node);
         }
         node.setParent(grand);
      } else {
         root = node;
         node.setParent(null);
      }

      if (parent.getLeft() == node) {
         node.setLeft(parent);
         node.setRight(parent.getRight());
         if (parent.getRight() != null)
            parent.getRight().setParent(node);
      } else {
         node.setRight(parent);
         node.setLeft(parent.getLeft());
         if (parent.getLeft() != null)
            parent.getLeft().setParent(node);
      }
      parent.setLeft(left);
      if (left != null)
         left.setParent(parent);
      parent.setRight(right);
      if (right != null)
         right.setParent(parent);
      parent.setParent(node);
   }

   private void swapRootWithLast() {
      HeapNode<E> lastParent = last.getParent();
      HeapNode<E> lastNode = last;
      HeapNode<E> rootNode = root;
      root = last;
      root.setLeft(rootNode.getLeft());
      if (rootNode.getLeft() != null)
         rootNode.getLeft().setParent(root);
      root.setRight(rootNode.getRight());
      if (rootNode.getRight() != null)
         rootNode.getRight().setParent(root);
      root.setParent(null);
      rootNode.setLeft(null);
      rootNode.setRight(null);
      if (lastParent.getLeft() == lastNode) {
         // left child
         lastParent.setLeft(rootNode);
      } else {
         lastParent.setRight(rootNode);
      }
      rootNode.setParent(lastParent);
      last = rootNode;
   }

}
