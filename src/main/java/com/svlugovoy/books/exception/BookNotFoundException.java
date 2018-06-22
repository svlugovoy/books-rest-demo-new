package com.svlugovoy.books.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException() {
        super("Custom message");
    }

}
