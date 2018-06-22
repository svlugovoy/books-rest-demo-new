package com.svlugovoy.books.exception;

import com.svlugovoy.books.domain.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BookNotFoundException.class})
    protected ResponseEntity<Book> handleNotFound(BookNotFoundException ex, WebRequest request) {

        // some logic may be here
        System.out.println("Book not found! my_header=" + request.getHeader("my_header"));

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookIdNotNumberException.class)
    protected ResponseEntity<Book> handleIdNotNumber(BookIdNotNumberException ex, WebRequest request) {

        // some logic may be here
        System.out.println("Book id not a number! my_header=" + request.getHeader("my_header"));

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
