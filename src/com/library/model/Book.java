package com.library.model;

public class Book {
    private String id;
    private String title;
    private String author;
    private String genre;
    private int quantity;
    private String availability;

    public Book(String id, String title, String author, String genre, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.quantity = quantity;
        updateAvailability();
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public int getQuantity() { return quantity; }
    public String getAvailability() { return availability; }

    // Setters
    public void setTitle(String title) { 
        this.title = title; 
    }
    
    public void setAuthor(String author) { 
        this.author = author; 
    }
    
    public void setGenre(String genre) { 
        this.genre = genre; 
    }
    
    public void setQuantity(int quantity) { 
        this.quantity = quantity;
        updateAvailability();
    }

    public void decreaseQuantity() {
        if (quantity > 0) {
            quantity--;
            updateAvailability();
        }
    }

    public void increaseQuantity() {
        quantity++;
        updateAvailability();
    }

    private void updateAvailability() {
        availability = quantity > 0 ? "Available" : "Out of Stock";
    }

    @Override
    public String toString() {
        return String.format("Book[id=%s, title=%s, author=%s, genre=%s, quantity=%d, availability=%s]",
            id, title, author, genre, quantity, availability);
    }
}