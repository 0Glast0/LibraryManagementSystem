package com.library.model;

import com.library.util.LibraryUtils;
import java.util.ArrayList;
import java.util.List;

public class UpdateManager {
    private static UpdateManager instance;
    private LibraryManager libraryManager;
    private List<String> updateLog;

    private UpdateManager() {
        this.libraryManager = LibraryManager.getInstance();
        this.updateLog = new ArrayList<>();
    }

    public static UpdateManager getInstance() {
        if (instance == null) {
            instance = new UpdateManager();
        }
        return instance;
    }

    public boolean updateBook(Book book, String newTitle, String newAuthor, String newGenre, int newQuantity) {
        try {
            // Validate inputs
            LibraryUtils.validateBookInputs(newTitle, newAuthor);
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }

            // Log the old values before update
            String oldValues = String.format("Old values - Title: %s, Author: %s, Genre: %s, Quantity: %d",
                book.getTitle(), book.getAuthor(), book.getGenre(), book.getQuantity());

            // Update the book
            book.setTitle(newTitle);
            book.setAuthor(newAuthor);
            book.setGenre(newGenre);
            book.setQuantity(newQuantity);

            // Log the update
            String updateMessage = String.format("[%s] Updated Book ID: %s - %s -> New values - Title: %s, Author: %s, Genre: %s, Quantity: %d",
                LibraryUtils.getCurrentDate(), book.getId(), oldValues, newTitle, newAuthor, newGenre, newQuantity);
            updateLog.add(updateMessage);

            return true;
        } catch (IllegalArgumentException e) {
            logError("Update failed for Book ID: " + book.getId() + " - " + e.getMessage());
            throw e;
        }
    }

    public boolean updateBorrowStatus(BorrowRecord record, String newStatus) {
        try {
            if (!isValidStatus(newStatus)) {
                throw new IllegalArgumentException("Invalid status: " + newStatus);
            }

            String oldStatus = record.getStatus();
            record.setStatus(newStatus);

            // Log the status change
            String updateMessage = String.format("[%s] Updated Borrow Record ID: %s - Status changed from %s to %s",
                LibraryUtils.getCurrentDate(), record.getListNo(), oldStatus, newStatus);
            updateLog.add(updateMessage);

            // Update book quantity if necessary
            if (newStatus.equals("Returned")) {
                Book book = libraryManager.findBookById(record.getBookId());
                if (book != null) {
                    book.increaseQuantity();
                }
            }

            return true;
        } catch (IllegalArgumentException e) {
            logError("Update failed for Borrow Record ID: " + record.getListNo() + " - " + e.getMessage());
            throw e;
        }
    }

    public boolean updateBookQuantity(Book book, int quantityChange) {
        try {
            int newQuantity = book.getQuantity() + quantityChange;
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Resulting quantity would be negative");
            }

            int oldQuantity = book.getQuantity();
            book.setQuantity(newQuantity);

            // Log the quantity change
            String updateMessage = String.format("[%s] Updated Book ID: %s - Quantity changed from %d to %d",
                LibraryUtils.getCurrentDate(), book.getId(), oldQuantity, newQuantity);
            updateLog.add(updateMessage);

            return true;
        } catch (IllegalArgumentException e) {
            logError("Quantity update failed for Book ID: " + book.getId() + " - " + e.getMessage());
            throw e;
        }
    }

    public List<String> getUpdateLog() {
        return new ArrayList<>(updateLog);
    }

    public void clearUpdateLog() {
        updateLog.clear();
    }

    private boolean isValidStatus(String status) {
        return status.equals("Borrowed") || status.equals("Returned") || status.equals("Overdue");
    }

    private void logError(String errorMessage) {
        String logEntry = String.format("[%s] ERROR: %s", LibraryUtils.getCurrentDate(), errorMessage);
        updateLog.add(logEntry);
    }

    // Additional utility methods for specific update scenarios
    public boolean updateBookAvailability(Book book, boolean makeAvailable) {
        try {
            String oldStatus = book.getAvailability();
            if (makeAvailable) {
                book.increaseQuantity();
            } else {
                if (book.getQuantity() > 0) {
                    book.decreaseQuantity();
                } else {
                    throw new IllegalStateException("Cannot make unavailable: quantity already zero");
                }
            }

            String updateMessage = String.format("[%s] Updated Book ID: %s - Availability changed from %s to %s",
                LibraryUtils.getCurrentDate(), book.getId(), oldStatus, book.getAvailability());
            updateLog.add(updateMessage);

            return true;
        } catch (IllegalStateException e) {
            logError("Availability update failed for Book ID: " + book.getId() + " - " + e.getMessage());
            throw e;
        }
    }
}