package org.example.collections;

import java.util.*;

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
        return size() == 0;
    }

    public boolean add(T o) {
        /*
            Добавление элемента в не пустую коллекцию:
            - добавляем указатель у текущего последнего элемента на новый
            - новый элемент ссылаем на текущий последний
            - устанавливаем новый элемент "хвостовым"

            Пустая коолекция требует установки ссылки для головы и хвоста.
         */
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

    //вспомогоательный метод поиска элемента
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
        //убегаем от НПЕ
        if (tmp != null){
            return tmp.getValue();
        }

        return null;
    }

    public T remove(int index) {
        //Изем элемент для удаления
        Node<T> cur = getNode(index);
        if (cur != null) {
            Node<T> prev = cur.getPrev();
            Node<T> next = cur.getNext();

            /*
                Есть несколько вариантов удаления элемента,
                кждый требует разного подхода очиски и смещения указателей
             */
            if (prev != null && next != null){ //удаляем из середины (рядом с удаляемым есть другие ноды)
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

    public List<T> toList(){
        if (size() == 0) {
            return List.of();
        }

        List<T> result = new ArrayList<>(size);
        Node<T> cur = head;
        while(cur != null){
            result.add(cur.getValue());
            cur = cur.getNext();
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        Node<T> cur = head;
        while(cur != null){
            stringBuilder.append(cur.getValue());
            cur = cur.getNext();
            if (cur != null){ //для последнего пропускаем запятую
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
