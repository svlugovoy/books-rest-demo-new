package com.svlugovoy.books.controller;

import com.svlugovoy.books.domain.Book;
import com.svlugovoy.books.exception.BookIdNotNumberException;
import com.svlugovoy.books.exception.BookNotFoundException;
import com.svlugovoy.books.repository.BookRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/cache/books")
public class BookCacheController {

    private final BookRepository bookRepository;

    public BookCacheController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public Iterable<Book> findBooks() {
        return bookRepository.findAll();
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    @CacheResult(cacheName = "books")
    public Book findBookById(@PathVariable String id) {

        Optional<Book> optional = bookRepository.findById(Long.valueOf(id));
        return optional.orElseThrow(BookNotFoundException::new);
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    @CachePut(cacheName = "books")
    public Book updateBook(@PathVariable String id, @CacheValue @Valid @RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    @CacheEvict("books")
    public void deleteBook(@PathVariable String id) {
        bookRepository.deleteById(Long.valueOf(id));
    }

}
