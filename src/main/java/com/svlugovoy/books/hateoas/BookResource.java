package com.svlugovoy.books.hateoas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.svlugovoy.books.controller.BookHateoas2Controller;
import com.svlugovoy.books.domain.Book;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class BookResource extends ResourceSupport {

    @JsonProperty("id")
    private Long bookId;

    private String author;

    private String name;

    private boolean rented;

    public BookResource(Book book) {
        bookId = book.getId();
        author = book.getAuthor();
        name = book.getName();
        rented = book.isRented();
        add(linkTo(methodOn(BookHateoas2Controller.class)
                .findBookById(String.valueOf(bookId))).withSelfRel());
        if (!rented) {
            add(linkTo(methodOn(BookHateoas2Controller.class)
                    .rentBook(String.valueOf(bookId))).withRel("rent"));
        }
    }

    public Long getBookId() {
        return bookId;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public boolean isRented() {
        return rented;
    }
}
