package org.example.menu;

import org.example.data.Book;
import org.example.data.Shelf;
import org.example.reader.BookFileReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookMenuTerminal {
    private final Scanner scanner;
    private Shelf shelf;
    private final BookFileReader fileReader;

    public BookMenuTerminal() {
        this.scanner = new Scanner(System.in);
        shelf = new Shelf();
        this.fileReader = new BookFileReader();
    }

    public BookMenuTerminal(Scanner scanner, List<Book> books, BookFileReader fileReader) {
        this.scanner = scanner;
        this.shelf = new Shelf(books);
        this.fileReader = fileReader;
    }

    public void run() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Выберите пункт меню: ");

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    addBooksFromFile();
                    break;
                case 3:
                    showAllBooks();
                    break;
                case 4:
                    showTopGenderByAuthor();
                    break;
                case 5:
                    running = false;
                    System.out.println("Выход из программы...");
                    break;
                default:
                    System.out.println("Ошибка: неверный пункт меню!");
            }
        }

        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n===== МЕНЮ =====");
        System.out.println("1. Добавить книгу вручную");
        System.out.println("2. Добавить книги из файла");
        System.out.println("3. Показать все книги");
        System.out.println("4. Количество книг в каждом жанре автора");
        System.out.println("5. Выход");
        System.out.println("================");
    }

    private void addBook() {
        try {
            String name = readString("Введите название книги: ");
            String author = readString("Введите автора книги: ");
            List<String> genders = readGenderList();

            Book book = new Book(name, author, genders);
            shelf.addBook(book);
            System.out.println("Книга успешно добавлена!");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении книги: " + e.getMessage());
        }
    }

    private String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Ошибка: поле не может быть пустым. Попробуйте снова.");
                continue;
            }
            return input;
        }
    }

    private List<String> readGenderList() {
        List<String> genders = new ArrayList<>();
        System.out.println("Введите жанры (для завершения введите пустую строку):");
        while (true) {
            System.out.print("Жанр: ");
            String genre = scanner.nextLine().trim();
            if (genre.isEmpty()) {
                break;
            }
            genders.add(genre);
        }
        if (genders.isEmpty()) {
            System.out.println("Предупреждение: список жанров пуст.");
        }
        return genders;
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число!");
            }
        }
    }

    private void addBooksFromFile() {
        System.out.print("Введите путь к файлу: ");
        String filePath = scanner.nextLine().trim();

        try {
            BookFileReader.ReadResult result = fileReader.read(filePath);

            shelf = new Shelf(result.getBooks());
            System.out.printf("Успешно добавлено книг: %d%n", result.getBooks().size());

            if (result.hasErrors()) {
                System.out.println("Обнаружены ошибки в строках файла:");
                for (String error : result.getErrors()) {
                    System.out.println("  " + error);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: файл не найден - " + filePath);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Непредвиденная ошибка: " + e.getMessage());
        }
    }

    private void showAllBooks() {
        if (shelf.isEmpty()) {
            System.out.println("Список книг пуст.");
            return;
        }
        System.out.println("\n--- Список книг ---");
        System.out.printf(shelf.toString());

    }

    private void showTopGenderByAuthor() {
        if (shelf.isEmpty()) {
            System.out.println("Список книг пуст.");
            return;
        }

        String authorQuery = readString("Введите автора для поиска: ");

        if (!shelf.authorExists(authorQuery)){
            System.out.println("Книги автора '" + authorQuery + "' не найдены.");
            return;
        }

        List<String> result = shelf.getTopGendersByAuthor(authorQuery);

        if (result.isEmpty()) {
            System.out.println("Книги автора '" + authorQuery + "' не найдены.");
            return;
        }

        System.out.print(String.join("\n", result));
    }
}