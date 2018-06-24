package com.svlugovoy.books.controller;

import com.svlugovoy.books.domain.Book;
import com.svlugovoy.books.exception.BookNotFoundException;
import com.svlugovoy.books.hateoas.BookResource;
import com.svlugovoy.books.repository.BookRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hateoas2/books")
public class BookHateoas2Controller {

    private final BookRepository bookRepository;

    public BookHateoas2Controller(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<BookResource> findBooks() {
        List<Book> books = (List<Book>) bookRepository.findAll();
        return books.stream()
                .map(BookResource::new)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Book findBookById(@PathVariable String id) {

        Optional<Book> optional = bookRepository.findById(Long.valueOf(id));

        return optional.orElseThrow(BookNotFoundException::new);
    }

    @PutMapping("/rent/{id}")
    public ResponseEntity<Void> rentBook(@PathVariable String id) {
        Optional<Book> optional = bookRepository.findById(Long.valueOf(id));

        optional.ifPresent(book -> {
            book.setRented(true);
            bookRepository.save(book);
        });
        return ResponseEntity.ok().build();
    }

}
