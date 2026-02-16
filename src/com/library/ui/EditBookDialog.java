package com.library.ui;

import com.library.model.*;
import com.library.util.LibraryConstants;
import com.library.util.LibraryUtils;
import javax.swing.*;
import java.awt.*;

public class EditBookDialog {
    private LibraryManager libraryManager;
    private UpdateManager updateManager;
    private LibraryUI libraryUI;
    private JFrame editFrame;

    public EditBookDialog(LibraryManager libraryManager, LibraryUI libraryUI) {
        this.libraryManager = libraryManager;
        this.updateManager = UpdateManager.getInstance();
        this.libraryUI = libraryUI;
    }

    public void show() {
        String bookId = JOptionPane.showInputDialog(null, 
            "Enter Book ID to edit:", 
            "Edit Book", 
            JOptionPane.QUESTION_MESSAGE);

        if (bookId != null && !bookId.trim().isEmpty()) {
            Book book = libraryManager.findBookById(bookId.trim());
            if (book != null) {
                showEditForm(book);
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Book not found.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditForm(Book book) {
        editFrame = new JFrame("Edit Book");
        editFrame.setSize(400, 300);
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Book details panel
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField titleField = new JTextField(book.getTitle());
        JTextField authorField = new JTextField(book.getAuthor());
        JComboBox<String> genreCombo = new JComboBox<>(LibraryConstants.GENRES);
        genreCombo.setSelectedItem(book.getGenre());
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(
            book.getQuantity(), 0, 100, 1));

        detailsPanel.add(new JLabel("Book ID:"));
        detailsPanel.add(new JLabel(book.getId()));
        detailsPanel.add(new JLabel("Title:"));
        detailsPanel.add(titleField);
        detailsPanel.add(new JLabel("Author:"));
        detailsPanel.add(authorField);
        detailsPanel.add(new JLabel("Genre:"));
        detailsPanel.add(genreCombo);
        detailsPanel.add(new JLabel("Quantity:"));
        detailsPanel.add(quantitySpinner);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        JButton viewHistoryButton = new JButton("View History");

        saveButton.addActionListener(e -> handleSave(
            book,
            titleField.getText(),
            authorField.getText(),
            (String) genreCombo.getSelectedItem(),
            (Integer) quantitySpinner.getValue()
        ));

        cancelButton.addActionListener(e -> editFrame.dispose());
        
        viewHistoryButton.addActionListener(e -> showUpdateHistory(book.getId()));

        buttonPanel.add(saveButton);
        buttonPanel.add(viewHistoryButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Current Status: " + book.getAvailability()));
        mainPanel.add(statusPanel, BorderLayout.NORTH);

        editFrame.add(mainPanel);
        editFrame.setVisible(true);
    }

    private void handleSave(Book book, String title, String author, String genre, int quantity) {
        try {
            // Validate inputs before proceeding
            if (title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty");
            }
            if (author.trim().isEmpty()) {
                throw new IllegalArgumentException("Author cannot be empty");
            }
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }

            // Show confirmation dialog with changes
            String changes = String.format(
                "Current Values:\n" +
                "Title: %s\n" +
                "Author: %s\n" +
                "Genre: %s\n" +
                "Quantity: %d\n\n" +
                "New Values:\n" +
                "Title: %s\n" +
                "Author: %s\n" +
                "Genre: %s\n" +
                "Quantity: %d",
                book.getTitle(), book.getAuthor(), book.getGenre(), book.getQuantity(),
                title, author, genre, quantity
            );

            int confirm = JOptionPane.showConfirmDialog(editFrame,
                "Confirm these changes?\n\n" + changes,
                "Confirm Edit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // Use UpdateManager to handle the update
                boolean updateSuccess = updateManager.updateBook(book, title, author, genre, quantity);
                
                if (updateSuccess) {
                    libraryUI.updateBookDisplay();
                    JOptionPane.showMessageDialog(editFrame,
                        "Book updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    showUpdateLog(book.getId());
                    editFrame.dispose();
                }
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(editFrame,
                "Error: " + e.getMessage(),
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(editFrame,
                "An unexpected error occurred: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showUpdateLog(String bookId) {
        java.util.List<String> updateLog = updateManager.getUpdateLog();
        String recentUpdates = updateLog.stream()
            .filter(log -> log.contains("Book ID: " + bookId))
            .reduce((first, second) -> second)
            .orElse("No updates found");

        JOptionPane.showMessageDialog(editFrame,
            "Update Details:\n" + recentUpdates,
            "Update Log",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showUpdateHistory(String bookId) {
        java.util.List<String> updateLog = updateManager.getUpdateLog();
        java.util.List<String> bookUpdates = updateLog.stream()
            .filter(log -> log.contains("Book ID: " + bookId))
            .toList();

        if (bookUpdates.isEmpty()) {
            JOptionPane.showMessageDialog(editFrame,
                "No update history found for this book.",
                "Update History",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog historyDialog = new JDialog(editFrame, "Update History", true);
        historyDialog.setLayout(new BorderLayout(5, 5));

        JTextArea historyArea = new JTextArea(15, 40);
        historyArea.setEditable(false);
        historyArea.setMargin(new Insets(5, 5, 5, 5));
        
        for (String update : bookUpdates) {
            historyArea.append(update + "\n\n");
        }

        JScrollPane scrollPane = new JScrollPane(historyArea);
        historyDialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> historyDialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        historyDialog.add(buttonPanel, BorderLayout.SOUTH);

        historyDialog.pack();
        historyDialog.setLocationRelativeTo(editFrame);
        historyDialog.setVisible(true);
    }
}