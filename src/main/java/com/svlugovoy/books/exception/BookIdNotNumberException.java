package com.svlugovoy.books.exception;

public class BookIdNotNumberException extends RuntimeException {

    public BookIdNotNumberException() {
        super("Custom message");
    }

}
