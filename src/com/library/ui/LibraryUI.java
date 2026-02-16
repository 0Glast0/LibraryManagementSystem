package com.library.ui;

import com.library.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LibraryUI {
    private LibraryManager libraryManager;
    private JFrame mainFrame;
    private JPanel gridPanel;
    private static final String SUPPORT_EMAIL = "bosskimrudsguston@gmail.com";

    public LibraryUI() {
        this.libraryManager = LibraryManager.getInstance();
        initializeUI();
    }

    private void initializeUI() {
        showLoginDialog();
    }

    private void showLoginDialog() {
        JFrame loginFrame = new JFrame();
        JPanel panel = createLoginPanel();
        
        while (true) {
            int input = JOptionPane.showConfirmDialog(loginFrame, panel, 
                "Enter username and password:", JOptionPane.OK_CANCEL_OPTION);
            
            if (handleLoginResult(input, panel)) {
                break;
            }
        }
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        // Username/Password fields
        JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
        label.add(new JLabel("Username:", SwingConstants.RIGHT));
        label.add(new JLabel("Password:", SwingConstants.RIGHT));
        panel.add(label, BorderLayout.WEST);

        JPanel inputFields = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField usernameInput = new JTextField();
        inputFields.add(usernameInput);
        JPasswordField passwordInput = new JPasswordField();
        inputFields.add(passwordInput);
        panel.add(inputFields, BorderLayout.CENTER);

        // Forgot Password link
        JLabel forgotPasswordLabel = new JLabel("<html><a href=''>Forgot your password?</a></html>");
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(forgotPasswordLabel, BorderLayout.SOUTH);
        
        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, 
                    "Please contact support at \"" + SUPPORT_EMAIL + "\".");
            }
        });

        return panel;
    }

    private boolean handleLoginResult(int input, JPanel panel) {
        if (input == JOptionPane.CANCEL_OPTION) {
            System.exit(0);
            return true;
        }

        JTextField usernameInput = (JTextField) ((JPanel)panel.getComponent(1)).getComponent(0);
        JPasswordField passwordInput = (JPasswordField) ((JPanel)panel.getComponent(1)).getComponent(1);
        
        String username = usernameInput.getText();
        String password = new String(passwordInput.getPassword());

        if (username.equals("admin") && password.equals("password") && 
            input == JOptionPane.OK_OPTION) {
            initializeMainWindow();
            return true;
        } else if (input == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(null, "No matching username and password found.");
            return false;
        }

        System.exit(0);
        return true;
    }

    private void initializeMainWindow() {
        mainFrame = new JFrame();
        mainFrame.setTitle("Library Management System - Book Catalog");
        mainFrame.setSize(800, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        setupGridPanel();
        setupButtonPanel();

        mainFrame.setVisible(true);
    }

    private void setupGridPanel() {
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 6, 10, 0));
        JScrollPane scrollPane = new JScrollPane(gridPanel);

        // Add headers
        gridPanel.add(new JLabel("ID"));
        gridPanel.add(new JLabel("Title"));
        gridPanel.add(new JLabel("Author"));
        gridPanel.add(new JLabel("Genre"));
        gridPanel.add(new JLabel("Availability"));
        gridPanel.add(new JLabel("Quantity"));

        updateBookDisplay();
        mainFrame.add(scrollPane);
    }

    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel();
        String[] buttonLabels = {"Add", "Edit", "Delete", "Search", "Borrow", "Track", "Log Out"};
        
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(e -> handleButtonClick(label));
            buttonPanel.add(button);
        }

        mainFrame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleButtonClick(String action) {
        switch (action) {
            case "Add": new AddBookDialog(libraryManager, this).show(); break;
            case "Edit": new EditBookDialog(libraryManager, this).show(); break;
            case "Delete": new DeleteBookDialog(libraryManager, this).show(); break;
            case "Search": new SearchBookDialog(libraryManager).show(); break;
            case "Borrow": new BorrowBookDialog(libraryManager, this).show(); break;
            case "Track": new TrackBorrowsDialog(libraryManager).show(); break;
            case "Log Out": handleLogout(); break;
        }
    }

    private void handleLogout() {
        int logOutConfirmation = JOptionPane.showConfirmDialog(mainFrame, 
            "Are you sure you want to log out?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
            
        if (logOutConfirmation == JOptionPane.YES_OPTION) {
            mainFrame.dispose();
            System.exit(0);
        }
    }

    public void updateBookDisplay() {
        gridPanel.removeAll();

        // Add headers
        gridPanel.add(new JLabel("ID"));
        gridPanel.add(new JLabel("Title"));
        gridPanel.add(new JLabel("Author"));
        gridPanel.add(new JLabel("Genre"));
        gridPanel.add(new JLabel("Availability"));
        gridPanel.add(new JLabel("Quantity"));

        // Add book data
        for (Book book : libraryManager.getAllBooks()) {
            gridPanel.add(new JLabel(book.getId()));
            gridPanel.add(new JLabel(book.getTitle()));
            gridPanel.add(new JLabel(book.getAuthor()));
            gridPanel.add(new JLabel(book.getGenre()));
            gridPanel.add(new JLabel(book.getAvailability()));
            gridPanel.add(new JLabel(String.valueOf(book.getQuantity())));
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }
}