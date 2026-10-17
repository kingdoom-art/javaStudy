package org.example.reader;

import org.example.data.Book;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Отвечает за чтение книг из файла.
 * Формат строки: name;author;жанр1,жанр2,жанр3
 */
public class BookFileReader {

    /**
     * Чтение данных из файла по переданному пути.
     * Ошибочные строки завернуты в возвращаемый результат, не нам с ними разбираться
     * @param filePath путь до файла
     * @return результат чтения файла
     */
    public ReadResult read(String filePath) throws IOException {
        List<Book> books = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            //построчный обход содержимого файла, ошибочная строка кидается в буфер, пусть пользователь решает, что с этим делать
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;

                try {
                    Book book = parseLine(trimmed);
                    books.add(book);
                } catch (IllegalArgumentException e) {
                    errors.add(String.format("Строка %d: %s", lineNumber, e.getMessage()));
                }
            }
        }

        return new ReadResult(books, errors);
    }

    /**
     * Разбирает одну строку файла в объект Book.
     * Провиди валидации выходных данных
     * IllegalArgumentException при ошибке обработки данных
     */
    private Book parseLine(String line) {
        String[] parts = line.split(";");
        if (parts.length < 2) {
            throw new IllegalArgumentException("неверный формат (ожидается: название;автор;жанры)");
        }

        String name = parts[0].trim();
        String author = parts[1].trim();

        if (name.isEmpty()) {
            throw new IllegalArgumentException("название не может быть пустым");
        }
        if (author.isEmpty()) {
            throw new IllegalArgumentException("автор не может быть пустым");
        }

        List<String> genders = new ArrayList<>();
        if (parts.length >= 3 && !parts[2].trim().isEmpty()) {
            for (String g : parts[2].split(",")) {
                String genre = g.trim();
                if (!genre.isEmpty()) {
                    genders.add(genre);
                }
            }
        }

        return new Book(name, author, genders);
    }

    //Класс - обертка чтения данных, чтобы возвращать еще и ошибочные строки
    public static class ReadResult {
        private final List<Book> books;
        private final List<String> errors;

        public ReadResult(List<Book> books, List<String> errors) {
            this.books = books;
            this.errors = errors;
        }

        public List<Book> getBooks() {
            return books;
        }

        public List<String> getErrors() {
            return errors;
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }
    }
}