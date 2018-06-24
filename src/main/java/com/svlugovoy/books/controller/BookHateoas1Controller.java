package com.svlugovoy.books.controller;

import com.svlugovoy.books.domain.Book;
import com.svlugovoy.books.exception.BookNotFoundException;
import com.svlugovoy.books.hateoas.BookResource;
import com.svlugovoy.books.repository.BookRepository;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/hateoas1/books")
public class BookHateoas1Controller {

    private final BookRepository bookRepository;

    public BookHateoas1Controller(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Resource<Book>> findBooks() {
        List<Book> books = (List<Book>) bookRepository.findAll();

        List<Resource<Book>> resources = new ArrayList<>();
        for (Book book : books) {
            Resource<Book> resource = new Resource<>(book);
            resource.add(linkTo(methodOn(BookHateoas1Controller.class)
                    .findBookById(String.valueOf(book.getId()))).withSelfRel());
            if (!book.isRented()) {
                resource.add(linkTo(methodOn(BookHateoas1Controller.class)
                        .rentBook(String.valueOf(book.getId()))).withRel("rent"));
            }
            resources.add(resource);
        }
        return resources;
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
