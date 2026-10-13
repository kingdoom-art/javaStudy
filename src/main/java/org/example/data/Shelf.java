package org.example.data;

import java.util.*;
import java.util.stream.Collectors;

public class Shelf {
    private List<Book> shelf;

    public Shelf() {
        shelf = new ArrayList<>();
    }

    public Shelf(List<Book> books) {
        this.shelf = books;
    }

    public List<Book> getShelf() {
        return shelf;
    }

    public void addBook(Book book) {
        shelf.add(book);
    }

    public boolean authorExists(String author){
        for(Book book : shelf){
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

        var tmp = shelf.stream()
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
        for (Book book : shelf){
            stringBuilder.append(book + "\n");
        }
        return stringBuilder.toString();
    }
}
