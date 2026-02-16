package com.library.ui;

import com.library.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class TrackBorrowsDialog {
    private LibraryManager libraryManager;
    private UpdateManager updateManager;
    private JFrame trackFrame;
    private JTable borrowTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public TrackBorrowsDialog(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
        this.updateManager = UpdateManager.getInstance();
    }

    public void show() {
        trackFrame = new JFrame("Track Borrowed Books");
        trackFrame.setSize(1000, 500);
        trackFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        trackFrame.setLocationRelativeTo(null);

        setupTrackingUI();
        trackFrame.setVisible(true);
    }

    private void setupTrackingUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search Panel
        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        trackFrame.add(mainPanel);
        refreshTableData();
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JLabel searchLabel = new JLabel("Search: ");
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        
        return searchPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        createBorrowTable();
        JScrollPane scrollPane = new JScrollPane(borrowTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private void createBorrowTable() {
        String[] columnNames = {
            "List No", "Student ID", "Name", "Book ID", 
            "Book Title", "Borrow Date", "Return Date", "Status"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        borrowTable = new JTable(tableModel);
        borrowTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        borrowTable.getTableHeader().setReorderingAllowed(false);
        
        // Set up the sorter
        sorter = new TableRowSorter<>(tableModel);
        borrowTable.setRowSorter(sorter);
        
        // Customize table appearance
        borrowTable.setFillsViewportHeight(true);
        borrowTable.setShowGrid(true);
        borrowTable.setGridColor(Color.LIGHT_GRAY);
        
        // Set column widths
        borrowTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // List No
        borrowTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Student ID
        borrowTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Name
        borrowTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Book ID
        borrowTable.getColumnModel().getColumn(4).setPreferredWidth(200); // Book Title
        borrowTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Borrow Date
        borrowTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Return Date
        borrowTable.getColumnModel().getColumn(7).setPreferredWidth(80);  // Status
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton returnButton = new JButton("Return Book");
        JButton refreshButton = new JButton("Refresh");
        JButton viewLogButton = new JButton("View Update Log");
        JButton exportButton = new JButton("Export Records");
        JButton closeButton = new JButton("Close");

        returnButton.addActionListener(e -> handleReturnBook());
        refreshButton.addActionListener(e -> refreshTableData());
        viewLogButton.addActionListener(e -> showAllUpdates());
        exportButton.addActionListener(e -> exportRecords());
        closeButton.addActionListener(e -> trackFrame.dispose());

        buttonPanel.add(returnButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(viewLogButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(closeButton);

        return buttonPanel;
    }

    private void filter() {
    String text = searchField.getText();
    if (text.trim().length() == 0) {
        sorter.setRowFilter(null);
    } else {
        try {
            // Escape special regex characters in the search text
            String escapedText = Pattern.quote(text);
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + escapedText));
        } catch (PatternSyntaxException e) {
            // Handle invalid regex pattern
            JOptionPane.showMessageDialog(trackFrame,
                "Invalid search pattern",
                "Search Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

    private void refreshTableData() {
        tableModel.setRowCount(0);
        List<BorrowRecord> records = libraryManager.getAllBorrowRecords();
        
        for (BorrowRecord record : records) {
            Book book = libraryManager.findBookById(record.getBookId());
            String bookTitle = book != null ? book.getTitle() : "Unknown Book";
            
            tableModel.addRow(new Object[]{
                record.getListNo(),
                record.getBorrowerId(),
                record.getBorrowerName(),
                record.getBookId(),
                bookTitle,
                record.getBorrowDate(),
                record.getReturnDate(),
                record.getStatus()
            });
        }
    }

    private void handleReturnBook() {
        int selectedRow = borrowTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(trackFrame,
                "Please select a record to return",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convert view index to model index
        int modelRow = borrowTable.convertRowIndexToModel(selectedRow);
        
        String listNo = tableModel.getValueAt(modelRow, 0).toString();
        String status = tableModel.getValueAt(modelRow, 7).toString();

        if ("Returned".equals(status)) {
            JOptionPane.showMessageDialog(trackFrame,
                "This book has already been returned",
                "Already Returned",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(trackFrame,
            "Confirm return of book?",
            "Confirm Return",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                BorrowRecord record = libraryManager.findRecordByListNo(listNo);
                if (record != null) {
                    boolean updateSuccess = updateManager.updateBorrowStatus(record, "Returned");
                    
                    if (updateSuccess) {
                        refreshTableData();
                        showUpdateLog(record.getListNo());
                        JOptionPane.showMessageDialog(trackFrame,
                            "Book returned successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(trackFrame,
                    "Error returning book: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showUpdateLog(String listNo) {
        List<String> updateLog = updateManager.getUpdateLog();
        String recentUpdates = updateLog.stream()
            .filter(log -> log.contains("Borrow Record ID: " + listNo))
            .reduce((first, second) -> second)
            .orElse("No updates found");

        JOptionPane.showMessageDialog(trackFrame,
            "Update Details:\n" + recentUpdates,
            "Update Log",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAllUpdates() {
        List<String> updateLog = updateManager.getUpdateLog();
        if (updateLog.isEmpty()) {
            JOptionPane.showMessageDialog(trackFrame,
                "No updates found in the log",
                "Update Log",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog logDialog = new JDialog(trackFrame, "Update Log", true);
        logDialog.setLayout(new BorderLayout(5, 5));

        JTextArea logArea = new JTextArea(20, 50);
        logArea.setEditable(false);
        logArea.setMargin(new Insets(5, 5, 5, 5));
        
        for (String log : updateLog) {
            logArea.append(log + "\n\n");
        }

        JScrollPane scrollPane = new JScrollPane(logArea);
        logDialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        JButton exportLogButton = new JButton("Export Log");
        
        closeButton.addActionListener(e -> logDialog.dispose());
        exportLogButton.addActionListener(e -> exportUpdateLog(updateLog));
        
        buttonPanel.add(exportLogButton);
        buttonPanel.add(closeButton);
        logDialog.add(buttonPanel, BorderLayout.SOUTH);

        logDialog.pack();
        logDialog.setLocationRelativeTo(trackFrame);
        logDialog.setVisible(true);
    }

    private void exportRecords() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = sdf.format(new Date());
            String filename = "borrow_records_" + timestamp + ".csv";

            StringBuilder csv = new StringBuilder();
            // Add header
            csv.append("List No,Student ID,Name,Book ID,Book Title,Borrow Date,Return Date,Status\n");

            // Add data
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    csv.append(tableModel.getValueAt(i, j));
                    if (j < tableModel.getColumnCount() - 1) {
                        csv.append(",");
                    }
                }
                csv.append("\n");
            }

            // Write to file
            java.nio.file.Files.writeString(java.nio.file.Path.of(filename), csv.toString());
            
            JOptionPane.showMessageDialog(trackFrame,
                "Records exported successfully to " + filename,
                "Export Success",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(trackFrame,
                "Error exporting records: " + e.getMessage(),
                "Export Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportUpdateLog(List<String> updateLog) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = sdf.format(new Date());
            String filename = "update_log_" + timestamp + ".txt";

            StringBuilder log = new StringBuilder();
            for (String entry : updateLog) {
                log.append(entry).append("\n\n");
            }

            java.nio.file.Files.writeString(java.nio.file.Path.of(filename), log.toString());
            
            JOptionPane.showMessageDialog(trackFrame,
                "Update log exported successfully to " + filename,
                "Export Success",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(trackFrame,
                "Error exporting update log: " + e.getMessage(),
                "Export Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}