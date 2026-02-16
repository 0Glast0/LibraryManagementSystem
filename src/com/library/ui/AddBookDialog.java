package com.library.ui;

import com.library.model.*;
import com.library.util.LibraryConstants;
import com.library.util.LibraryUtils;
import javax.swing.*;
import java.awt.*;

public class AddBookDialog {
    private LibraryManager libraryManager;
    private LibraryUI libraryUI;
    private JFrame addFrame;

    public AddBookDialog(LibraryManager libraryManager, LibraryUI libraryUI) {
        this.libraryManager = libraryManager;
        this.libraryUI = libraryUI;
    }

    public void show() {
        addFrame = new JFrame("Add Book");
        addFrame.setSize(400, 300);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input fields panel
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JComboBox<String> genreCombo = new JComboBox<>(LibraryConstants.GENRES);
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(authorField);
        inputPanel.add(new JLabel("Genre:"));
        inputPanel.add(genreCombo);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantitySpinner);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        addButton.addActionListener(e -> handleAddBook(
            titleField.getText(),
            authorField.getText(),
            (String) genreCombo.getSelectedItem(),
            (Integer) quantitySpinner.getValue()
        ));

        cancelButton.addActionListener(e -> addFrame.dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addFrame.add(mainPanel);
        addFrame.setVisible(true);
    }

    private void handleAddBook(String title, String author, String genre, int quantity) {
        try {
            LibraryUtils.validateBookInputs(title, author);

            int confirm = JOptionPane.showConfirmDialog(addFrame,
                "Confirm adding book:\n" +
                "Title: " + title + "\n" +
                "Author: " + author + "\n" +
                "Genre: " + genre + "\n" +
                "Quantity: " + quantity,
                "Confirm Add",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                libraryManager.addBook(title, author, genre, quantity);
                libraryUI.updateBookDisplay();
                JOptionPane.showMessageDialog(addFrame,
                    "Book added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                addFrame.dispose();
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(addFrame,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}