package com.library.util;

public class LibraryConstants {
    // Login credentials
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "password";
    public static final String SUPPORT_EMAIL = "bosskimrudsguston@gmail.com";

    // UI Constants
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 400;
    public static final String WINDOW_TITLE = "Library Management System - Book Catalog";

    // Book Status
    public static final String STATUS_AVAILABLE = "Available";
    public static final String STATUS_OUT_OF_STOCK = "Out of Stock";

    // Borrow Status
    public static final String STATUS_BORROWED = "Borrowed";
    public static final String STATUS_RETURNED = "Returned";

    // Genre Options
    public static final String[] GENRES = {
        "Action",
        "Adventure Novel",
        "Biography",
        "Children Fiction",
        "Comedy",
        "Drama",
        "Epic Poem",
        "Existential Novel",
        "Gothic Novel",
        "Historical",
        "Horror",
        "Literary Fiction",
        "Modernist Fiction",
        "Mystery",
        "Non-Fiction",
        "Novella",
        "Poetry",
        "Psychological Fiction",
        "Psychological Novel",
        "Romance",
        "Satirical Novel",
        "Science Fiction",
        "Tragedy",
        "Thriller"
    };

    // Error Messages
    public static final String ERROR_INVALID_LOGIN = "No matching username and password found.";
    public static final String ERROR_BOOK_NOT_FOUND = "Book not found.";
    public static final String ERROR_EMPTY_TITLE = "Book title must not be empty.";
    public static final String ERROR_EMPTY_AUTHOR = "Author name must not be empty.";
    public static final String ERROR_INVALID_QUANTITY = "Invalid quantity. Please enter a valid number.";
    public static final String ERROR_EMPTY_STUDENT_ID = "Student ID must not be empty.";
    public static final String ERROR_INVALID_NAME = "Invalid name. Name cannot contain numbers.";
    public static final String ERROR_BOOK_UNAVAILABLE = "This book is currently unavailable.";

    // Success Messages
    public static final String SUCCESS_BOOK_ADDED = "Book added successfully!";
    public static final String SUCCESS_BOOK_UPDATED = "Book updated successfully!";
    public static final String SUCCESS_BOOK_DELETED = "Book deleted successfully!";
    public static final String SUCCESS_BOOK_BORROWED = "Book borrowed successfully!";
    public static final String SUCCESS_BOOK_RETURNED = "Book returned successfully!";

    // Date Format
    public static final String DATE_FORMAT = "yyyy/MM/dd";
    public static final int DEFAULT_BORROW_DAYS = 14;
}