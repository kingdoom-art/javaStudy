package org.example.data;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String name;
    private String author;
    private List<String> gender;

    public Book() {
        gender = new ArrayList<>();
    }

    public Book(String name, String author, List<String> gender) {
        this.name = name;
        this.author = author;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return String.format("Книга: '%s', Автор: '%s', Жанры: %s",
                name, author, gender);
    }
}
