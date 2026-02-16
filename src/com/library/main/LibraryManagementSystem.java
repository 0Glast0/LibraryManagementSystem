package com.library.main;

import com.library.ui.LibraryUI;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class LibraryManagementSystem {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Start the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LibraryUI();
        });
    }
}