package com.library.model;

import com.library.util.LibraryUtils;
import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private static LibraryManager instance;
    private List<Book> books;
    private List<BorrowRecord> borrowRecords;
    private int bookIdCounter;
    private int listNoCounter;

    private LibraryManager() {
        books = new ArrayList<>();
        borrowRecords = new ArrayList<>();
        bookIdCounter = 1;
        listNoCounter = 1;
        initializeDefaultBooks();
    }

    public static LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    private void initializeDefaultBooks() {
        addBook("The Wings", "Yi Sang", "Modernist Fiction", 1);
        addBook("Faust", "Johann Wolfgang von Goethe", "Tragedy", 1);
        addBook("Don Quixote", "Miguel de Cervantes", "Satirical Novel", 1);
        addBook("Hell Screen", "Ryūnosuke Akutagawa", "Psychological Fiction", 1);
        addBook("The Stranger", "Albert Camus", "Existential Novel", 1);
        addBook("Dream of the Red Chamber", "Cao Xueqin", "Literary Fiction", 1);
        addBook("Wuthering Heights", "Emily Brontë", "Gothic Novel", 1);
        addBook("Moby Dick", "Herman Melville", "Adventure Novel", 1);
        addBook("Crime and Punishment", "Fyodor Dostoevsky", "Psychological Novel", 1);
        addBook("Demian", "Hermann Hesse", "Psychological Fiction", 1);
        addBook("The Odyssey", "Homer", "Epic Poem", 1);
        addBook("The Metamorphosis", "Franz Kafka", "Novella", 1);
        addBook("The Divine Comedy", "Dante Alighieri", "Epic Poem", 1);
    }

    public Book addBook(String title, String author, String genre, int quantity) {
        String id = generateBookId();
        Book book = new Book(id, title, author, genre, quantity);
        books.add(book);
        return book;
    }

    public void deleteBook(Book book) {
        books.remove(book);
    }

    public Book findBookById(String id) {
        return books.stream()
            .filter(book -> book.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public BorrowRecord createBorrowRecord(String studentId, String name, String bookId) {
        Book book = findBookById(bookId);
        if (book != null && book.getQuantity() > 0) {
            String listNo = generateListNo();
            BorrowRecord record = new BorrowRecord(
                listNo,
                studentId,
                name,
                bookId,
                LibraryUtils.getCurrentDate(),
                LibraryUtils.getReturnDate()
            );
            borrowRecords.add(record);
            book.decreaseQuantity();
            return record;
        }
        return null;
    }

    public BorrowRecord findRecordByListNo(String listNo) {
        return borrowRecords.stream()
            .filter(record -> record.getListNo().equals(listNo))
            .findFirst()
            .orElse(null);
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return new ArrayList<>(borrowRecords);
    }

    private String generateBookId() {
        return "24-" + bookIdCounter++;
    }

    private String generateListNo() {
        return String.valueOf(listNoCounter++);
    }
}