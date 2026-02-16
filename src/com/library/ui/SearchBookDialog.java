package com.library.ui;

import com.library.model.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class SearchBookDialog {
    private LibraryManager libraryManager;
    private JFrame searchFrame;
    private JPanel suggestionListPanel;
    private JTextField searchField;

    public SearchBookDialog(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
    }

    public void show() {
        searchFrame = new JFrame("Search Books");
        searchFrame.setSize(550, 300);
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setLocationRelativeTo(null);

        JPanel mainPanel = createMainPanel();
        searchFrame.add(mainPanel);
        searchFrame.setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Suggestions panel
        suggestionListPanel = new JPanel();
        suggestionListPanel.setLayout(new BoxLayout(suggestionListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(suggestionListPanel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");

        // Add components
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add listeners
        setupListeners(searchButton, closeButton);

        return mainPanel;
    }

    private void setupListeners(JButton searchButton, JButton closeButton) {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateSearch(); }
            public void removeUpdate(DocumentEvent e) { updateSearch(); }
            public void insertUpdate(DocumentEvent e) { updateSearch(); }
        });

        searchButton.addActionListener(e -> performSearch());
        closeButton.addActionListener(e -> searchFrame.dispose());
    }

    private void updateSearch() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        updateSuggestions(searchTerm);
    }

    private void updateSuggestions(String searchTerm) {
        suggestionListPanel.removeAll();
        if (!searchTerm.isEmpty()) {
            for (Book book : libraryManager.getAllBooks()) {
                if (matchesSearch(book, searchTerm)) {
                    addSuggestionLabel(book);
                }
            }
        }
        suggestionListPanel.revalidate();
        suggestionListPanel.repaint();
    }

    private boolean matchesSearch(Book book, String searchTerm) {
        return book.getId().toLowerCase().contains(searchTerm) ||
               book.getTitle().toLowerCase().contains(searchTerm) ||
               book.getAuthor().toLowerCase().contains(searchTerm) ||
               book.getGenre().toLowerCase().contains(searchTerm);
    }

    private void addSuggestionLabel(Book book) {
        JLabel label = new JLabel(formatBookInfo(book));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showBookDetails(book);
            }
            public void mouseEntered(MouseEvent e) {
                label.setForeground(Color.BLUE);
            }
            public void mouseExited(MouseEvent e) {
                label.setForeground(Color.BLACK);
            }
        });
        suggestionListPanel.add(label);
    }

    private String formatBookInfo(Book book) {
        return String.format("<html><b>%s</b> - %s by %s (%s)</html>",
            book.getId(), book.getTitle(), book.getAuthor(), book.getGenre());
    }

    private void showBookDetails(Book book) {
        String details = String.format(
            "Book Details:\n\n" +
            "ID: %s\n" +
            "Title: %s\n" +
            "Author: %s\n" +
            "Genre: %s\n" +
            "Availability: %s\n" +
            "Quantity: %d",
            book.getId(), book.getTitle(), book.getAuthor(),
            book.getGenre(), book.getAvailability(), book.getQuantity()
        );
        
        JOptionPane.showMessageDialog(searchFrame,
            details,
            "Book Details",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(searchFrame,
                "Please enter a search term",
                "Search Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        updateSuggestions(searchTerm.toLowerCase());
    }
}