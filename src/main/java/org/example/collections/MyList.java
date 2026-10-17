package org.example.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class MyList<T>{

    private class Node<T> {
        private Node next;
        private Node prev;

        private T value;

        public Node(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public MyList() {
        size = 0;
    }

    public MyList(Collection<T> collection) {
        addAll(collection);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() > 0;
    }

    public boolean add(T o) {
        if (tail != null) {
            Node tmp = new Node<>(o);
            tail.setNext(tmp);
            tmp.setPrev(tail);
            tail = tmp;
            size ++;
        } else {
            Node tmp = new Node<>(o);
            head = tmp;
            tail = tmp;
            size++;
        }
        return true;
    }

    public boolean addAll(Collection<T> collection){
        size = collection.size();
        for (var el : collection){
            add(el);
        }
        return true;
    }

    private Node<T> getNode(int index) {
        Node<T> tmp = head;
        for (int i = 0; i < size; i++){
            if (i == index) {
                return tmp;
            }
            tmp = tmp.getNext();
        }
        return null;
    }

    public T get(int index) {
        Node<T> tmp = getNode(index);
        if (tmp != null){
            return tmp.getValue();
        }

        return null;
    }

    public T remove(int index) {
        Node<T> cur = getNode(index);
        if (cur != null) {
            Node<T> prev = cur.getPrev();
            Node<T> next = cur.getNext();

            if (prev != null && next != null){
                prev.setNext(next);
                next.setPrev(prev);
            } else if (prev == null && next != null) { //удаление первого элемента из списка
                head = next;
                next.setPrev(null);
            } else if (prev != null && next == null) { //Удаление последнего элемента
                tail = prev;
                prev.setNext(null);
            } else { //удаление единственного элемента
                tail = null;
                head = null;
            }
            size--;

            return cur.getValue();
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        Node<T> cur = head;
        while(cur != null){
            stringBuilder.append(cur.getValue());
            cur = cur.getNext();
            if (cur != null){
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
