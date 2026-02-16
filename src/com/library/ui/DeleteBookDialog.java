package com.library.ui;

import com.library.model.*;
import javax.swing.*;

public class DeleteBookDialog {
    private LibraryManager libraryManager;
    private LibraryUI libraryUI;

    public DeleteBookDialog(LibraryManager libraryManager, LibraryUI libraryUI) {
        this.libraryManager = libraryManager;
        this.libraryUI = libraryUI;
    }

    public void show() {
        String bookId = JOptionPane.showInputDialog(null, 
            "Enter Book ID to delete:", 
            "Delete Book", 
            JOptionPane.QUESTION_MESSAGE);

        if (bookId != null && !bookId.trim().isEmpty()) {
            Book book = libraryManager.findBookById(bookId.trim());
            if (book != null) {
                confirmAndDelete(book);
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Book not found.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void confirmAndDelete(Book book) {
        int confirm = JOptionPane.showConfirmDialog(null,
            "Are you sure you want to delete this book?\n\n" +
            "ID: " + book.getId() + "\n" +
            "Title: " + book.getTitle() + "\n" +
            "Author: " + book.getAuthor() + "\n" +
            "Genre: " + book.getGenre() + "\n" +
            "Quantity: " + book.getQuantity(),
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            libraryManager.deleteBook(book);
            libraryUI.updateBookDisplay();
            JOptionPane.showMessageDialog(null,
                "Book deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}