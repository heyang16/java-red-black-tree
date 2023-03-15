package redblacktree;

import java.util.NoSuchElementException;

public class RedBlackTree<K extends Comparable<? super K>, V> {

  Node<K, V> root;

  public RedBlackTree() {
    this.root = null;
  }

  RedBlackTree(Node<K, V> root) {
    this.root = root;
  }

  public void put(K key, V value) {

    Tuple<Node<K, V>, Node<K, V>> pair = findNode(key);

    Node<K, V> parent = pair.getX();
    Node<K, V> current = pair.getY();

    if (current != null) { // found an existing key
      current.setValue(value);
      return;
    }

    if (parent == null) { // empty tree
      root = new Node<K, V>(key, value, Colour.BLACK);
      return;
    }
    /* create a new key */
    int comparison = key.compareTo(parent.getKey());
    Node<K, V> newNode = new Node<K, V>(key, value, Colour.RED);
    if (comparison < 0) {
      parent.setLeft(newNode);
    } else {
      assert comparison > 0;
      parent.setRight(newNode);
    }

    insertCaseOne(newNode);
  }

  private void insertCaseOne(Node<K, V> current) {
    if (current.getParent() == null) {
      current.setBlack();
      return;
    }
    insertCaseTwo(current);
  }

  private void insertCaseTwo(Node<K, V> current) {
    if (current.getParent().isBlack()) {
      return;
    }
    insertCaseThree(current);
  }

  private void insertCaseThree(Node<K, V> current) {
    if (current.getUncle() != null && current.getUncle().isRed()) {
      current.getUncle().setBlack();
      current.getParent().setBlack();
      current.getGrandparent().setRed();
      insertCaseOne(current.getGrandparent());
    } else {
      insertCaseFour(current);
    }
  }

  private void insertCaseFour(Node<K, V> current) {
    if (current.isLeftChild() && current.getParent().isRightChild()) {
      current.getParent().rotateRight();
      insertCaseFive(current.getRight());
      return;
    }
    if (current.isRightChild() && current.getParent().isLeftChild()) {
      current.getParent().rotateLeft();
      insertCaseFive(current.getLeft());
      return;
    }
    insertCaseFive(current);
  }

  private void insertCaseFive(Node<K, V> current) {
    Node<K, V> newGrandparent;
    boolean changeRoot = current.getGrandparent() == root;
    if (current.isLeftChild()) {
      current.getParent().setBlack();
      current.getGrandparent().setRed();
      newGrandparent = current.getGrandparent().rotateRight();
    } else {
      current.getParent().setBlack();
      current.getGrandparent().setRed();
      newGrandparent = current.getGrandparent().rotateLeft();
    }
    if (changeRoot) {
      root = newGrandparent;
    }
  }

  private Tuple<Node<K, V>, Node<K, V>> findNode(K key) {
    Node<K, V> current = root;
    Node<K, V> parent = null;

    while (current != null) {
      parent = current;

      int comparison = key.compareTo(current.getKey());
      if (comparison < 0) {
        current = current.getLeft();
      } else if (comparison == 0) {
        break;
      } else {
        assert comparison > 0;
        current = current.getRight();
      }
    }
    return new Tuple<Node<K, V>, Node<K, V>>(parent, current);
  }

  public boolean contains(K key) {
    Tuple<Node<K, V>, Node<K, V>> pair = findNode(key);
    return pair.getY() != null;
  }

  public V get(K key) {
    Tuple<Node<K, V>, Node<K, V>> pair = findNode(key);
    Node<K, V> current = pair.getY();
    if (current == null) {
      throw new NoSuchElementException();
    }
    return current.getValue();
  }

  public void clear() {
    this.root = null;
  }

  public String toString() {
    return "RBT " + root + " ";
  }
}
