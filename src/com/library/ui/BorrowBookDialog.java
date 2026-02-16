package com.library.ui;

import com.library.model.*;
import com.library.util.LibraryUtils;
import javax.swing.*;
import java.awt.*;

public class BorrowBookDialog {
    private LibraryManager libraryManager;
    private LibraryUI libraryUI;
    private JFrame borrowFrame;
    private static String lastStudentId = "";
    private static String lastName = "";

    public BorrowBookDialog(LibraryManager libraryManager, LibraryUI libraryUI) {
        this.libraryManager = libraryManager;
        this.libraryUI = libraryUI;
    }

    public void show() {
        String bookId = JOptionPane.showInputDialog(null, 
            "Enter Book ID to borrow:", 
            "Borrow Book", 
            JOptionPane.QUESTION_MESSAGE);

        if (bookId != null && !bookId.trim().isEmpty()) {
            Book book = libraryManager.findBookById(bookId.trim());
            if (book != null) {
                if (book.getQuantity() > 0) {
                    showBorrowForm(book);
                } else {
                    JOptionPane.showMessageDialog(null,
                        "This book is currently unavailable.",
                        "Book Unavailable",
                        JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                    "Book not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showBorrowForm(Book book) {
        borrowFrame = new JFrame("Borrow Book");
        borrowFrame.setSize(400, 300);
        borrowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        borrowFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Book details panel
        JPanel bookDetailsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        bookDetailsPanel.add(new JLabel("Book ID: " + book.getId()));
        bookDetailsPanel.add(new JLabel("Title: " + book.getTitle()));
        bookDetailsPanel.add(new JLabel("Author: " + book.getAuthor()));
        bookDetailsPanel.add(new JLabel("Available Quantity: " + book.getQuantity()));
        mainPanel.add(bookDetailsPanel, BorderLayout.NORTH);

        // Borrower details panel
        JPanel borrowerPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField studentIdField = new JTextField(lastStudentId);
        JTextField nameField = new JTextField(lastName);
        JTextField borrowDateField = new JTextField(LibraryUtils.getCurrentDate());
        JTextField returnDateField = new JTextField(LibraryUtils.getReturnDate());

        borrowerPanel.add(new JLabel("Student ID:"));
        borrowerPanel.add(studentIdField);
        borrowerPanel.add(new JLabel("Name:"));
        borrowerPanel.add(nameField);
        borrowerPanel.add(new JLabel("Borrow Date:"));
        borrowerPanel.add(borrowDateField);
        borrowerPanel.add(new JLabel("Return Date:"));
        borrowerPanel.add(returnDateField);

        borrowDateField.setEditable(false);
        returnDateField.setEditable(false);

        mainPanel.add(borrowerPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton borrowButton = new JButton("Borrow");
        JButton cancelButton = new JButton("Cancel");

        borrowButton.addActionListener(e -> handleBorrow(book, 
            studentIdField.getText(), 
            nameField.getText(), 
            borrowDateField.getText(), 
            returnDateField.getText()));
            
        cancelButton.addActionListener(e -> borrowFrame.dispose());

        buttonPanel.add(borrowButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        borrowFrame.add(mainPanel);
        borrowFrame.setVisible(true);
    }

    private void handleBorrow(Book book, String studentId, String name, 
                            String borrowDate, String returnDate) {
        try {
            if (studentId.trim().isEmpty()) {
                throw new IllegalArgumentException("Student ID cannot be empty");
            }
            if (!LibraryUtils.isValidName(name)) {
                throw new IllegalArgumentException("Please enter a valid name");
            }

            int confirm = JOptionPane.showConfirmDialog(borrowFrame,
                "Confirm borrowing:\n" +
                "Student ID: " + studentId + "\n" +
                "Name: " + name + "\n" +
                "Book: " + book.getTitle() + "\n" +
                "Borrow Date: " + borrowDate + "\n" +
                "Return Date: " + returnDate,
                "Confirm Borrow",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                BorrowRecord record = libraryManager.createBorrowRecord(
                    studentId, name, book.getId());
                
                if (record != null) {
                    lastStudentId = studentId;
                    lastName = name;
                    libraryUI.updateBookDisplay();
                    JOptionPane.showMessageDialog(borrowFrame,
                        "Book borrowed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    borrowFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(borrowFrame,
                        "Failed to borrow book. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(borrowFrame,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}