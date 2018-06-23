package com.svlugovoy.books.controller;

import com.svlugovoy.books.domain.Book;
import com.svlugovoy.books.event.BookSavedEvent;
import com.svlugovoy.books.exception.BookIdNotNumberException;
import com.svlugovoy.books.exception.BookNotFoundException;
import com.svlugovoy.books.monitoring.metric.BookMetricsBean;
import com.svlugovoy.books.monitoring.metric.BookMetricsBeanEvent;
import com.svlugovoy.books.repository.BookRepository;
import io.micrometer.core.annotation.Timed;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookMetricsBean metricsBean;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Timed("books.findAll")
    @GetMapping
    public Iterable<Book> findBooks() {
        return bookRepository.findAll();
    }

//    @GetMapping(path = "/{id}",
//            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public Book findBookById(@PathVariable String id) {
//        return bookRepository.findById(Long.valueOf(id)).get();
//    }

//    @GetMapping(path = "/{id}",
//            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<Book> findBookById(@PathVariable String id) {
//
//        int bookId = NumberUtils.toInt(id);
//        if(bookId <= 0) {
//            return ResponseEntity.badRequest().build(); //400
//        }
//
//        Optional<Book> optional = bookRepository.findById((long) bookId);
//        if (!optional.isPresent()) {
//            return ResponseEntity.notFound().build(); //404
//        }
//
//        return ResponseEntity.ok(optional.get()); //200
//    }

//    @GetMapping(path = "/{id}",
//            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<Book> findBookById(@PathVariable String id) {
//
//        int bookId = NumberUtils.toInt(id);
//        if(bookId <= 0) {
//            return ResponseEntity.badRequest().build(); //400
//        }
//
//        Book book = bookRepository.findById((long) bookId).orElseThrow(BookNotFound2Exception::new);
//
//        return ResponseEntity.ok(book); //200
//    }

    @GetMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Book> findBookById(@PathVariable String id) {

        int bookId = NumberUtils.toInt(id);
        if (bookId <= 0) {
            throw new BookIdNotNumberException(); //400
        }

        Book book = bookRepository.findById((long) bookId).orElseThrow(BookNotFoundException::new);

        return ResponseEntity.ok(book); //200
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void createBook(@Valid @RequestBody Book book) {
        bookRepository.save(book);
        eventPublisher.publishEvent(new BookSavedEvent(this, book));
        metricsBean.onSaveBook(book);
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void updateBook(@PathVariable String id, @Valid @RequestBody Book book) {
        bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable String id) {
        bookRepository.deleteById(Long.valueOf(id));
    }

    // /api/books/search?page=0&size=5
    @GetMapping(path = "/search")
    public ResponseEntity<Page<Book>> searchBooks(Pageable pageable) {

        Page<Book> books = bookRepository.findAll(pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(bookRepository.count()));

        return ResponseEntity.ok().headers(headers).body(books);
    }
}
