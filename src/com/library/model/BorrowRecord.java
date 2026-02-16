package com.library.model;

public class BorrowRecord {
    private String listNo;
    private String borrowerId;
    private String borrowerName;
    private String bookId;
    private String borrowDate;
    private String returnDate;
    private String status;

    public BorrowRecord(String listNo, String borrowerId, String borrowerName, 
                       String bookId, String borrowDate, String returnDate) {
        this.listNo = listNo;
        this.borrowerId = borrowerId;
        this.borrowerName = borrowerName;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = "Borrowed";
    }

    // Getters
    public String getListNo() { return listNo; }
    public String getBorrowerId() { return borrowerId; }
    public String getBorrowerName() { return borrowerName; }
    public String getBookId() { return bookId; }
    public String getBorrowDate() { return borrowDate; }
    public String getReturnDate() { return returnDate; }
    public String getStatus() { return status; }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("BorrowRecord[listNo=%s, borrowerId=%s, borrowerName=%s, " +
                           "bookId=%s, borrowDate=%s, returnDate=%s, status=%s]",
            listNo, borrowerId, borrowerName, bookId, borrowDate, returnDate, status);
    }
}