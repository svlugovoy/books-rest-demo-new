package com.svlugovoy.books.controller;

import com.svlugovoy.books.domain.Book;
import com.svlugovoy.books.exception.BookIdNotNumberException;
import com.svlugovoy.books.exception.BookNotFoundException;
import com.svlugovoy.books.repository.BookRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/ehcache/books")
public class BookEhCacheController {

    private final BookRepository bookRepository;

    public BookEhCacheController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public Iterable<Book> findBooks() {
        return bookRepository.findAll();
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    @CacheResult(cacheName = "books")
    public ResponseEntity<Book> findBookById(@PathVariable String id) {

        int bookId = NumberUtils.toInt(id);
        if (bookId <= 0) {
            throw new BookIdNotNumberException(); //400
        }

        Book book = bookRepository.findById((long) bookId).orElseThrow(BookNotFoundException::new);

        return ResponseEntity.ok(book); //200
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
//    @CachePut(cacheNames = {"books"}) // works wrong
//    @CacheEvict("books")
    public  ResponseEntity<Book> updateBook(@PathVariable String id, @CacheValue @Valid @RequestBody Book book) {

        return  ResponseEntity.ok(bookRepository.save(book));
    }

    @DeleteMapping("/{id}")
    @CacheEvict("books")
    public void deleteBook(@PathVariable String id) {
        bookRepository.deleteById(Long.valueOf(id));
    }

}
