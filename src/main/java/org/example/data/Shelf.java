package org.example.data;

import org.example.collections.MyList;

import java.util.*;
import java.util.stream.Collectors;

//Обертка над коллекцией книг
public class Shelf {
    private MyList<Book> shelf;

    public Shelf() {
        this.shelf = new MyList<>();
    }

    public Shelf(List<Book> books) {
        this.shelf = new MyList<>(books);
    }

    public List<Book> getShelf() {
        return shelf.toList();
    }

    public void addBook(Book book) {
        shelf.add(book);
    }

    public boolean authorExists(String author){
        for(Book book : shelf.toList()){
            if (book.getAuthor().equals(author)){
                return true;
            }
        }
        return false;
    }

    /**
     * Обход книг автора и получение жанров.
     * С последующим подсчетом количества книг в каждом жанре.
     * Сортировкой вхождений
     */
    public List<String> getTopGendersByAuthor(String author){
        if (!authorExists(author)) return List.of();

        var tmp = shelf.toList().stream()
                .filter(e -> e.getAuthor().equals(author))
                .flatMap(e -> e.getGender().stream())
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        return tmp.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, Long>::getValue))
                .map(e -> String.format("%s:%d", e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public boolean isEmpty(){
        return shelf.isEmpty();
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Book book : shelf.toList()){
            stringBuilder.append(book + "\n");
        }
        return stringBuilder.toString();
    }
}
